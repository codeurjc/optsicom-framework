package es.optsicom.lib.web.dto;

public class ReportAttributeResponseDTO {
	
	private String name;
	private String value;

	public ReportAttributeResponseDTO() {
		this.name = "";
		this.value = "";
	}

	public ReportAttributeResponseDTO(String name, String value) {
		this.name = (name == null) ? "" : name;
		this.value = (value == null) ? "null" : value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
