package es.optsicom.lib.web.filter;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.util.description.Properties;

public class InstancesFilter extends ElementFilter {

	private List<String> instanceFilters;

	public InstancesFilter(List<String> instanceFilters) {
		this.instanceFilters = instanceFilters;
	}

	@Override
	public boolean isAllowed(Properties properties) {
		return instanceFilters.contains(properties.getName());
	}

}
