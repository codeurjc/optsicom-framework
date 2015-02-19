package es.optsicom.lib.web.model;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class ReportTitle {
private List<ReportAttribute> attributes;
private String infoTitle;

public ReportTitle() {
	this.attributes = new ArrayList<ReportAttribute>();
	infoTitle = "";
}
public ReportTitle(List<ReportAttribute> attributes, String infoTitle) {
	this.attributes = (attributes == null)?new ArrayList<ReportAttribute>():attributes;
	this.infoTitle = (infoTitle == null)?"":infoTitle;
}
public ReportTitle(Title title) {
	this.infoTitle = ( title.getTitle() == null)?"": title.getTitle();
	this.attributes = new ArrayList<ReportAttribute>();
	for (Attribute attribute:title.getAttributes()){
		this.attributes.add(new ReportAttribute(attribute));
	}
}

public String getInfoTitle() {
	return infoTitle;
}
public void setInfoTitle(String infoTitle) {
	this.infoTitle = infoTitle;
}
public List<ReportAttribute> getAttributes() {
	return attributes;
}

public void setAttributes(List<ReportAttribute> attributes) {
	this.attributes = attributes;
}

}
