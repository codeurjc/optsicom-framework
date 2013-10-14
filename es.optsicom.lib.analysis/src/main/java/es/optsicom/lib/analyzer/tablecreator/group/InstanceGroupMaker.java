package es.optsicom.lib.analyzer.tablecreator.group;

import java.util.List;

import es.optsicom.lib.expresults.model.InstanceDescription;

public abstract class InstanceGroupMaker {
	
	private static final InstanceGroupMaker ONE_GROUP = new OneGroupInstanceMaker();
	
	public abstract List<InstancesGroup> createInstanceGroups(List<InstanceDescription> instances);
	
	public static InstanceGroupMaker getOneGroup() {
		return ONE_GROUP;
	}
	
	public static InstanceGroupMaker getGroupBy(String... properties){
		return new GroupByInstanceMaker(properties);
	}
	
	public static InstanceGroupMaker getGroupBy(List<String> properties){
		return getGroupBy(properties.toArray(new String[properties.size()]));
	}
	
}
