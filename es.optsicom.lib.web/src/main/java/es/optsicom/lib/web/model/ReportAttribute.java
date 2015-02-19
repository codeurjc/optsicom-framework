package es.optsicom.lib.web.model;

import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class ReportAttribute {
private String name;
private String value;

public ReportAttribute() {
	this.name = "";
	this.value = "";
}
public ReportAttribute(String name, String value) {
	this.name = (name == null)?"":name;
	this.value = (value == null)?"null":value;
}
public ReportAttribute(Attribute attribute) {
	this.name = (attribute.getName() == null)?"":attribute.getName();
	this.value = (attribute.getTitle() == null)?"null":attribute.getTitle();
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}

}
