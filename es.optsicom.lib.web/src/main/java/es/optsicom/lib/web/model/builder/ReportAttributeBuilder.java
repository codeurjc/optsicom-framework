package es.optsicom.lib.web.model.builder;

import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.web.model.ReportAttribute;

public class ReportAttributeBuilder {

	public ReportAttribute build(Attribute attribute) {
		if (attribute == null) {
			return new ReportAttribute();
		}
		String name = (attribute.getName() == null) ? "" : attribute.getName();
		String value = (attribute.getTitle() == null) ? "null" : attribute.getTitle();
		return new ReportAttribute(name, value);
	}
}
