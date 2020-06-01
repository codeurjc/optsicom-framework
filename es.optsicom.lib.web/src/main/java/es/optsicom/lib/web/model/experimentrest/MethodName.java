package es.optsicom.lib.web.model.experimentrest;

import es.optsicom.lib.expresults.model.MethodDescription;

public class MethodName {

	private String name;
	private MethodDescription method;

	public MethodName() {
	}

	public MethodName(String name, MethodDescription method) {
		this.name = name;
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MethodDescription getMethod() {
		return method;
	}

	public void setMethod(MethodDescription method) {
		this.method = method;
	}

}
