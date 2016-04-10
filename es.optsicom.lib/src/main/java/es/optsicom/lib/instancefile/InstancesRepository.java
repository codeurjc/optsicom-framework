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

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.Instance;
import es.optsicom.lib.expresults.model.InstanceDescription;

public abstract class InstancesRepository implements Closeable {

	public static final String DEFAULT_USE_CASE = "default";
	public static final String INSTANCE_ID_PATH_SEPARATOR = "/";
	public static final Path DEFAULT_INSTANCE_FILE_DIR = Paths.get("instance_files");
	public static final Path DEFAULT_INSTANCE_FILE_ZIP = Paths.get("instance_files.zip");

	// Key is the instance type (it may contain several levels separated by
	// INSTANCE_ID_PATH_SEPARATOR)
	private Map<String, InstanceFileSet> instanceFileSets = new HashMap<String, InstanceFileSet>();
	protected String useCase;

	public InstancesRepository(String useCase) {
		this.useCase = useCase;
	}

	public abstract Instance loadInstance(InstanceFile instanceFile) throws IOException;

	public String getUseCase() {
		return useCase;
	}

	public InstanceFileSet getInstanceFileSet(String setId) {
		return this.instanceFileSets.get(setId);
	}

	public Collection<InstanceFileSet> getInstanceFileSetList() {
		return this.instanceFileSets.values();
	}

	protected void putInstanceFileSet(InstanceFileSet instanceFileSet) {
		this.instanceFileSets.put(instanceFileSet.getId(), instanceFileSet);
	}

	public List<InstanceDescription> getAllInstanceDescriptions() {

		List<InstanceDescription> instanceDescriptions = new ArrayList<InstanceDescription>();

		for (InstanceFile instanceFile : this.getAllInstanceFiles()) {
			instanceDescriptions.add(instanceFile.createInstanceDescription());
		}

		return instanceDescriptions;
	}

	public List<InstanceDescription> getInstanceDescriptions(String instanceSetId) {

		List<InstanceDescription> instanceDescriptions = new ArrayList<InstanceDescription>();

		for (InstanceFile instanceFile : this.getInstanceFiles(instanceSetId)) {
			instanceDescriptions.add(instanceFile.createInstanceDescription());
		}

		return instanceDescriptions;
	}

	public List<InstanceFile> getAllInstanceFiles() {

		List<InstanceFile> instanceFiles = new ArrayList<InstanceFile>();

		for (InstanceFileSet ifs : this.instanceFileSets.values()) {
			instanceFiles.addAll(ifs.getInstanceFiles());
		}

		return instanceFiles;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (InstanceFileSet fileSet : this.instanceFileSets.values()) {
			sb.append(fileSet.toString());
		}
		return sb.toString();
	}

	public Instance loadInstance(String instancesSetId, int numInstance) throws IOException {
		return this.getInstanceFileSet(instancesSetId).getInstanceFile(0).loadInstance();
	}

	public List<InstanceFile> getInstanceFiles(String setId) {
		return getInstanceFileSet(setId).getInstanceFiles();
	}

	public InstanceFile getInstanceFileByPath(String instancePath) throws IOException {
		Path path = Paths.get(instancePath);
		return new InstanceFile(this, path.toFile(), null, null, path.getFileName().toString());
	}

	public InstanceFile getInstanceFileByName(String instanceName) {
		int indexOfSeparator = instanceName.indexOf(INSTANCE_ID_PATH_SEPARATOR);
		String instanceSetId = instanceName.substring(0, indexOfSeparator);
		String instanceFileName = instanceName.substring(indexOfSeparator + 1, instanceName.length());

		InstanceFileSet instanceFileSet = getInstanceFileSet(instanceSetId);

		if (instanceFileSet == null) {
			throw new RuntimeException("InstanceFileSet \"" + instanceSetId + "\" doesn't exist.");
		}

		List<InstanceFile> instanceFiles = this.getInstanceFiles(instanceSetId);

		for (InstanceFile instanceFile : instanceFiles) {
			if (instanceFile.getName().equals(instanceName)) {
				return instanceFile;
			}
		}
		throw new RuntimeException("Instance with file name \"" + instanceFileName + "\" doesn't exist in "
				+ instanceSetId + " instance set.");
	}
	
	public static Path getDefaultInstancesDirOrZip() {
		if (Files.exists(FileInstancesRepository.DEFAULT_INSTANCE_FILE_DIR)) {
			return FileInstancesRepository.DEFAULT_INSTANCE_FILE_DIR;
		} else if (Files.exists(FileInstancesRepository.DEFAULT_INSTANCE_FILE_ZIP)) {
			return FileInstancesRepository.DEFAULT_INSTANCE_FILE_ZIP;
		} else {
			throw new RuntimeException("Searching instance files in default folder ("
					+ FileInstancesRepository.DEFAULT_INSTANCE_FILE_DIR.toAbsolutePath()
					+ ") and in default .zip file ("
					+ FileInstancesRepository.DEFAULT_INSTANCE_FILE_ZIP.toAbsolutePath()
					+ "), but none of them exists");
		}
	}
	
	@Override
	public void close(){
		
	}
}
