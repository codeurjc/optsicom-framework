package es.optsicom.lib.web.model;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class ReportTitle {
private List<ReportAttribute> attributes;
private String infoTitle;

public ReportTitle() {
	emptyConstructor();
}
//public ReportTitle(List<ReportAttribute> attributes, String infoTitle) {
//	this.attributes = (attributes == null)?new ArrayList<ReportAttribute>():attributes;
//	this.infoTitle = (infoTitle == null)?"":infoTitle;
//}
public ReportTitle(Title title) {
	if (title == null){
		emptyConstructor();
	}
	else{
		this.infoTitle = ( title.getTitle() == null)?"": title.getTitle();
		this.attributes = new ArrayList<ReportAttribute>();
		for (Attribute attribute:title.getAttributes()){
			this.attributes.add(new ReportAttribute(attribute));
		}
	}
}

public void emptyConstructor(){ //avoid duplicate code
	this.attributes = new ArrayList<ReportAttribute>();
	infoTitle = "";
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
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
			+ ((attributes == null) ? 0 : attributes.hashCode());
	result = prime * result + ((infoTitle == null) ? 0 : infoTitle.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof ReportTitle))
		return false;
	ReportTitle other = (ReportTitle) obj;
	if (attributes == null) {
		if (other.attributes != null)
			return false;
	} else if (!attributes.equals(other.attributes))
		return false;
	if (infoTitle == null) {
		if (other.infoTitle != null)
			return false;
	} else if (!infoTitle.equals(other.infoTitle))
		return false;
	return true;
}
@Override
public String toString() {
	return "ReportTitle [attributes=" + attributes + ", infoTitle=" + infoTitle
			+ "]";
}

}
