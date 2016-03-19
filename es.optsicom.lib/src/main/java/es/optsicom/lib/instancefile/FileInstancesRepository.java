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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.optsicom.lib.util.Strings;

public abstract class FileInstancesRepository extends InstancesRepository {

	private File instancesDir;

	public FileInstancesRepository() {
		this(InstancesRepository.DEFAULT_INSTANCE_FILE_DIR, InstancesRepository.DEFAULT_USE_CASE);
	}

	public FileInstancesRepository(boolean populate) {
		this(InstancesRepository.DEFAULT_INSTANCE_FILE_DIR, InstancesRepository.DEFAULT_USE_CASE, populate);
	}

	public FileInstancesRepository(String useCase) {
		this(InstancesRepository.DEFAULT_INSTANCE_FILE_DIR, useCase);
	}

	public FileInstancesRepository(File instancesDir, String useCase) {
		this(instancesDir, useCase, true);
	}

	public FileInstancesRepository(File instancesDir, String useCase, boolean populate) {
		super(useCase);
		this.instancesDir = instancesDir;
		if (populate) {
			populate();
		}
	}

	public FileInstancesRepository(String useCase, boolean populate) {
		this(InstancesRepository.DEFAULT_INSTANCE_FILE_DIR, useCase, populate);
	}

	protected void populate() {

		File instanceFilesDir = new File(instancesDir, useCase);

		if (useCase.equals("default") && !instanceFilesDir.exists()) {
			// Assuming instances are not separated by useCases
			instanceFilesDir = instancesDir;
		}

		List<File> directoriesWithFiles = getAllDirsWithFiles(instanceFilesDir);
		for (File fileSetDirectory : directoriesWithFiles) {
			InstanceFileSet instanceFileSet = new InstanceFileSet(fileSetDirectory);

			String instanceSetId = fileSetDirectory.getAbsolutePath()
					.substring(instanceFilesDir.getAbsolutePath().length() + 1);
			instanceFileSet.setId(instanceSetId.replace(File.separatorChar,
					InstancesRepository.INSTANCE_ID_PATH_SEPARATOR.charAt(0)));

			File[] instanceFiles = fileSetDirectory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return !file.isHidden() && !file.isDirectory();
				}
			});

			Collections.sort(Arrays.asList(instanceFiles), new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Strings.compareNatural(f1.getName(), f2.getName());
				}
			});

			for (File file : instanceFiles) {
				InstanceFile instanceFile = new InstanceFile(this, file, useCase, instanceSetId, file.getName());

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

	/**
	 * @param instanceFilesDir
	 * @return
	 */
	private List<File> getAllDirsWithFiles(File instanceFilesDir) {
		List<File> dirsWithFiles = new ArrayList<File>();

		if (!instanceFilesDir.exists()) {
			throw new RuntimeException("The directory \"" + instanceFilesDir + "\" doesn't exist.");
		}

		File[] directories = instanceFilesDir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isDirectory() && !pathname.isHidden();
			}
		});

		File[] files = instanceFilesDir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		for (File f : directories) {
			dirsWithFiles.addAll(getAllDirsWithFiles(f));
		}

		if (files.length > 0 && directories.length == 0) {
			dirsWithFiles.add(instanceFilesDir);
		}

		return dirsWithFiles;
	}

	public void loadProperties(InstanceFile instanceFile) throws IOException {
	}

}
