package es.optsicom.lib.web.model;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class ReportTitle {
private final List<ReportAttribute> attributes;
private final String infoTitle;

public ReportTitle() {
	this.attributes = new ArrayList<ReportAttribute>();
	infoTitle = "";
}

public ReportTitle(List<ReportAttribute> attributes, String infoTitle) {
	this.infoTitle = infoTitle;
	this.attributes = attributes;
}

public String getInfoTitle() {
	return infoTitle;
}

public List<ReportAttribute> getAttributes() {
	return attributes;
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
