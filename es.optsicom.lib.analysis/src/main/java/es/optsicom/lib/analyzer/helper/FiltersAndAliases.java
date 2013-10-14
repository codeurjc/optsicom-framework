package es.optsicom.lib.analyzer.helper;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.Alias;
import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;

public class FiltersAndAliases {

	private ElementFilter methodFilter;
	private List<Alias> methodAliases;

	public void setMethodFilter(ElementFilter methodFilter) {
		this.methodFilter = methodFilter;
	}

	public void setMethodAliases(List<Alias> methodAliases) {
		this.methodAliases = methodAliases;
	}
	
	public List<Alias> getMethodAliases() {
		return methodAliases;
	}
	
	public ElementFilter getMethodFilter() {
		return methodFilter;
	}

}
