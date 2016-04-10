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
import java.io.Serializable;
import java.nio.file.Path;

import es.optsicom.lib.Instance;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class InstanceFile implements Serializable, Descriptive {

	private static final long serialVersionUID = 4199908568822373015L;

	protected Path file;

	private InstancesRepository repository;

	private String useCase;
	private String instanceSetId;
	private String fileName;
	private String name;

	private Properties properties;

	public InstanceFile(InstancesRepository repository, File file, String useCase, String instanceSetId, String fileName) {
		this(repository, file.toPath(), useCase, instanceSetId, fileName);
	}
	
	public InstanceFile(InstancesRepository repository, Path file, String useCase, String instanceSetId,
			String fileName) {
		this.file = file;
		this.useCase = useCase;
		this.instanceSetId = instanceSetId;
		this.fileName = fileName;
		this.repository = repository;
		this.name = instanceSetId + InstancesRepository.INSTANCE_ID_PATH_SEPARATOR + fileName;
	}

	public Instance loadInstance() throws IOException {
		return repository.loadInstance(this);
	}

	public File getFile() {
		return file.toFile();
	}
	
	public Path getPath() {
		return file;
	}

	@Id
	public String getFileName() {
		return fileName;
	}

	@Id
	public String getUseCase() {
		return useCase;
	}

	@Id
	public String getInstanceSetId() {
		return instanceSetId;
	}

	@Id
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
		if (properties != null) {
			properties.put("name", name);
		}
	}

	public Properties getProperties() {
		if (properties == null) {
			properties = DescriptiveHelper.createProperties(this);
			properties.getMap().remove("class");
		}
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return getProperties().getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceFile other = (InstanceFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public InstanceDescription createInstanceDescription() {
		return new InstanceDescription(new DBProperties(this.getProperties().getMap()));
	}

}
