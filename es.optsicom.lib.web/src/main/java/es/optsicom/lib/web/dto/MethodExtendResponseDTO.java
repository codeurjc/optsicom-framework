package es.optsicom.lib.web.dto;

import java.util.Map;

public class MethodExtendResponseDTO {

	private Long id;
	private String methodNameExp;
	private String methodName;
	private Map<String, String> properties;

	public MethodExtendResponseDTO(Long id, String methodNameExp, String methodName, Map<String, String> properties) {
		this.id = id;
		this.methodNameExp = methodNameExp;
		this.methodName = methodName;
		this.properties = properties;
	}

	public Long getId() {
		return id;
	}

	public String getMethodNameExp() {
		return methodNameExp;
	}

	public String getMethodName() {
		return methodName;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
