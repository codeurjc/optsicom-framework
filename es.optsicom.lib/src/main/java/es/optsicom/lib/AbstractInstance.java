package es.optsicom.lib;

import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class AbstractInstance implements Instance {

	private InstanceFile instanceFile;

	public AbstractInstance(InstanceFile instanceFile) {
		this.instanceFile = instanceFile;
	}

	@Override
	public String getId() {
		return instanceFile.getName();
	}

	@Override
	public InstanceFile getInstanceFile() {
		return instanceFile;
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	public InstanceDescription createInstanceDescription() {
		return new InstanceDescription(new DBProperties(this.getProperties().getMap()));
	}

	public abstract Problem getProblem();

}
