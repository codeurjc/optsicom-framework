package es.optsicom.lib.analyzer.tablecreator.group;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.expresults.model.InstanceDescription;

public class OneGroupInstanceMaker extends InstanceGroupMaker {

	@Override
	public List<InstancesGroup> createInstanceGroups(
			List<InstanceDescription> instances) {
		
		List<InstancesGroup> groups = new ArrayList<InstancesGroup>();
		
		groups.add(new InstancesGroup("Instances", instances));
		
		return groups;
	}

}
