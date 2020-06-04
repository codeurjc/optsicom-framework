/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.instancefile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.optsicom.lib.util.Strings;

public abstract class FileInstancesRepository extends InstancesRepository {

	private Path instancesDirOrZip;
	private FileSystem zipFs;

	public FileInstancesRepository() {
		this(getDefaultInstancesDirOrZip(), InstancesRepository.DEFAULT_USE_CASE);
	}

	public FileInstancesRepository(boolean populate) {
		this(getDefaultInstancesDirOrZip(), InstancesRepository.DEFAULT_USE_CASE, populate);
	}

	public FileInstancesRepository(String useCase) {
		this(getDefaultInstancesDirOrZip(), useCase);
	}

	public FileInstancesRepository(File instancesDirOrZip, String useCase) {
		this(instancesDirOrZip.toPath(), useCase, true);
	}

	public FileInstancesRepository(File instancesDirOrZip, String useCase, boolean populate) {
		this(instancesDirOrZip.toPath(), useCase, populate);
	}

	public FileInstancesRepository(Path instancesDirOrZip, String useCase) {
		this(instancesDirOrZip, useCase, true);
	}

	public FileInstancesRepository(Path instancesDirOrZip, String useCase, boolean populate) {
		super(useCase);
		this.instancesDirOrZip = instancesDirOrZip;
		if (populate) {
			populate();
		}
	}

	public FileInstancesRepository(String useCase, boolean populate) {
		this(InstancesRepository.DEFAULT_INSTANCE_FILE_DIR, useCase, populate);
	}

	protected void populate() {

		try {

			Path instanceFilesDir = calculateInstancesFolderPath();

			loadInstaceFiles(instanceFilesDir);

		} catch (IOException e) {
			throw new RuntimeException("Exception loading instance files", e);
		}
	}

	private Path calculateInstancesFolderPath() throws IOException {

		Path instanceFilesDir;

		if (Files.isDirectory(instancesDirOrZip)) {

			instanceFilesDir = instancesDirOrZip;

		} else {

			if (!instancesDirOrZip.getFileName().toString().endsWith(".zip")) {
				throw new RuntimeException(
						"InstanceFiles must be a folder or a .zip file but it is a file without .zip extension ("
								+ instancesDirOrZip.toAbsolutePath() + ")");
			}

			zipFs = FileSystems.newFileSystem(instancesDirOrZip, null);
			instanceFilesDir = zipFs.getPath("/");
		}

		// If useCase is default one it is possible to omit it from file system
		if (!useCase.equals(InstancesRepository.DEFAULT_USE_CASE) || Files.exists(instanceFilesDir.resolve(useCase))) {
			instanceFilesDir = instanceFilesDir.resolve(useCase);
		}

		return instanceFilesDir;
	}

	private void loadInstaceFiles(Path instanceFilesDir) throws IOException {

		List<Path> directoriesWithFiles = getAllDirsWithFiles(instanceFilesDir);

		for (Path fileSetDirectory : directoriesWithFiles) {

			InstanceFileSet instanceFileSet = new InstanceFileSet(fileSetDirectory);

			String instanceSetId = instanceFilesDir.relativize(fileSetDirectory).toString();

			if (instanceSetId.endsWith(Character.toString(File.separatorChar))) {
				instanceSetId = instanceSetId.substring(0, instanceSetId.length() - 1);
			}

			instanceFileSet.setId(instanceSetId.replace(File.separatorChar,
					InstancesRepository.INSTANCE_ID_PATH_SEPARATOR.charAt(0)));

			List<Path> instanceFiles = listFiles(fileSetDirectory,
					file -> !Files.isHidden(file) && !Files.isDirectory(file));

			Collections.sort(instanceFiles,
					(f1, f2) -> Strings.compareNatural(f1.getFileName().toString(), f2.getFileName().toString()));

			for (Path file : instanceFiles) {

				InstanceFile instanceFile = new InstanceFile(this, file, useCase, instanceSetId,
						file.getFileName().toString());

				instanceFileSet.addInstanceFile(instanceFile);

				// Load properties for this instance file
				try {
					loadProperties(instanceFile);
				} catch (IOException e) {
					System.out.println("Error loading properties of instance: " + instanceFile.getName());
					e.printStackTrace();
				}
			}

			this.putInstanceFileSet(instanceFileSet);
		}
	}

	private List<Path> listFiles(Path directory, Filter<Path> filter) throws IOException {
		List<Path> files = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory, filter)) {
			directoryStream.forEach(p -> files.add(p));
		}
		return files;
	}

	/**
	 * @param instanceFilesDir
	 * @return
	 * @throws IOException
	 */
	private List<Path> getAllDirsWithFiles(Path instanceFilesDir) throws IOException {

		List<Path> dirsWithFiles = new ArrayList<Path>();

		if (!Files.exists(instanceFilesDir)) {
			throw new RuntimeException("The directory \"" + instanceFilesDir + "\" doesn't exist.");
		}

		List<Path> directories = listFiles(instanceFilesDir, path -> Files.isDirectory(path) && !Files.isHidden(path));

		List<Path> files = listFiles(instanceFilesDir, path -> !Files.isDirectory(path) && !Files.isHidden(path));

		for (Path dir : directories) {
			dirsWithFiles.addAll(getAllDirsWithFiles(dir));
		}

		if (!files.isEmpty() && directories.isEmpty()) {
			dirsWithFiles.add(instanceFilesDir);
		}

		return dirsWithFiles;
	}

	@Override
	public void close() {
		if (zipFs != null) {
			try {
				zipFs.close();
			} catch (IOException e) {
				throw new RuntimeException("Exception closing "+this.getClass().getName(), e);
			}
		}
	}

	public void loadProperties(InstanceFile instanceFile) throws IOException {
	}

}
