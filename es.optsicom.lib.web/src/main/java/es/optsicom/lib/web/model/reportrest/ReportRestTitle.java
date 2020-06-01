package es.optsicom.lib.web.model.reportrest;

import java.util.ArrayList;
import java.util.List;

public class ReportRestTitle {

	private List<ReportRestAttribute> attributes;
	private String infoTitle;

	public ReportRestTitle() {
		this.attributes = new ArrayList<ReportRestAttribute>();
		infoTitle = "";
	}

	public ReportRestTitle(List<ReportRestAttribute> attributes, String infoTitle) {
		this.infoTitle = infoTitle;
		this.attributes = attributes;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public List<ReportRestAttribute> getAttributes() {
		return attributes;
	}

}
