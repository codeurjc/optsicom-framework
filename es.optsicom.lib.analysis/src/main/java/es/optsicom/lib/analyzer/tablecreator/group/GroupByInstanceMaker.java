package es.optsicom.lib.analyzer.tablecreator.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.util.description.MemoryProperties;

public class GroupByInstanceMaker extends InstanceGroupMaker {

	private String[] properties;

	public GroupByInstanceMaker(String... properties) {
		this.properties = properties;
	}

	@Override
	public List<InstancesGroup> createInstanceGroups(List<InstanceDescription> instances) {

		Map<List<String>, InstancesGroup> groups = new HashMap<List<String>, InstancesGroup>();

		for (InstanceDescription instance : instances) {

			List<String> values = new ArrayList<String>();
			for (String property : properties) {
				values.add(instance.getProperties().get(property));
			}

			InstancesGroup group = groups.get(values);
			if (group == null) {

				MemoryProperties groupProperties = new MemoryProperties();
				StringBuilder name = new StringBuilder();

				for (int i = 0; i < properties.length; i++) {

					String propertyName = properties[i];
					String propertyValue = values.get(i);

					name.append(propertyName).append("=");
					name.append(propertyValue).append(" ");

					groupProperties.put(propertyName, propertyValue);
				}

				group = new InstancesGroup(name.toString(), groupProperties);
				groups.put(values, group);
			}
			group.addInstance(instance);
		}

		return new ArrayList<InstancesGroup>(groups.values());
	}

}
