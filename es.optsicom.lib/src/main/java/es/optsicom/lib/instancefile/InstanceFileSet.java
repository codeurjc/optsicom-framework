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
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;

public class InstanceFileSet {

	protected List<InstanceFile> instanceFiles;
	protected List<Instance> instances;
	private String id;
	private File instanceFilesDir;

	public InstanceFileSet() {
		this.instanceFiles = new ArrayList<InstanceFile>();
	}

	public InstanceFileSet(File instanceFilesDirectory) {
		this.instanceFilesDir = instanceFilesDirectory;
		this.instanceFiles = new ArrayList<InstanceFile>();
	}

	public void addInstanceFile(InstanceFile instanceFile) {
		this.instanceFiles.add(instanceFile);
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<InstanceFile> getInstanceFiles() {
		return instanceFiles;
	}

	public InstanceFile getInstanceFile(int index) {
		return instanceFiles.get(index);
	}

	public List<Instance> getInstances() throws IOException {
		if (instances == null) {

			this.instances = new ArrayList<Instance>();
			for (InstanceFile instanceFile : instanceFiles) {
				instances.add(instanceFile.loadInstance());
			}
		}
		return instances;
	}

	public File getInstanceFilesDir() {
		return instanceFilesDir;
	}

	public void setInstanceFilesDir(File instanceFilesDir) {
		this.instanceFilesDir = instanceFilesDir;
	}

	public InstanceFile getInstanceFile(String fileName) {
		for (InstanceFile instanceFile : instanceFiles) {
			if (fileName.equals(instanceFile.getFileName())) {
				return instanceFile;
			}
		}
		return null;
	}

	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("InstanceFileSet name: " + this.getId() + "\n");
		for (InstanceFile instanceFile : this.instanceFiles) {
			sb.append("\tInstanceFile name: " + instanceFile.getFileName() + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}
}
