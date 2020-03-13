package es.optsicom.lib.web.model.reportrest;

public class ReportRestAttribute {
	
	private String name;
	private String value;

	public ReportRestAttribute() {
		this.name = "";
		this.value = "";
	}

	public ReportRestAttribute(String name, String value) {
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
