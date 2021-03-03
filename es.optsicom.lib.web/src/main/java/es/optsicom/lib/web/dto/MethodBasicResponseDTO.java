package es.optsicom.lib.web.dto;

public class MethodBasicResponseDTO {

	private Long id;
	private String methodNameExp;
	private String methodName;

	public MethodBasicResponseDTO(Long id, String methodNameExp, String methodName) {
		this.id = id;
		this.methodNameExp = methodNameExp;
		this.methodName = methodName;
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

}
