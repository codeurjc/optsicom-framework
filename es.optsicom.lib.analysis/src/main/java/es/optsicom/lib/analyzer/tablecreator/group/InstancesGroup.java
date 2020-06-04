package es.optsicom.lib.analyzer.tablecreator.group;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.MemoryProperties;
import es.optsicom.lib.util.description.Properties;

public class InstancesGroup implements Descriptive {

	private String name;
	private MemoryProperties customProps;
	private List<InstanceDescription> instances;

	public InstancesGroup(String name, List<InstanceDescription> instances) {
		this(name, new MemoryProperties(), instances);
	}

	public InstancesGroup(String name) {
		this(name, new MemoryProperties(), new ArrayList<InstanceDescription>());
	}

	public InstancesGroup(String name, MemoryProperties customProps) {
		this(name, customProps, new ArrayList<InstanceDescription>());
	}

	public InstancesGroup(String name, MemoryProperties customProps, List<InstanceDescription> instances) {
		this.name = name;
		this.customProps = customProps;
		this.instances = instances;
	}

	@Id
	public List<InstanceDescription> getInstances() {
		return instances;
	}

	@Id
	public String getName() {
		return name;
	}

	@Id
	public Properties getCustomProps() {
		return customProps;
	}

	public void addInstance(InstanceDescription instance) {
		this.instances.add(instance);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instances == null) ? 0 : instances.hashCode());
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
		InstancesGroup other = (InstancesGroup) obj;
		if (instances == null) {
			if (other.instances != null)
				return false;
		} else if (!instances.equals(other.instances))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
