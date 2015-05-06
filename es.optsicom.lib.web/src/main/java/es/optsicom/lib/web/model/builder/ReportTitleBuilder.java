package es.optsicom.lib.web.model.builder;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.web.model.ReportAttribute;
import es.optsicom.lib.web.model.ReportTitle;

public class ReportTitleBuilder {
	private final ReportAttributeBuilder reportAttributeBuilder = new ReportAttributeBuilder();

	public ReportTitle build(Title title) {
		if (title == null) {
			return new ReportTitle();
		}
		List<ReportAttribute> attributes;
		String infoTitle;
		infoTitle = (title.getTitle() == null) ? "" : title.getTitle();
		attributes = new ArrayList<ReportAttribute>();
		for (Attribute attribute : title.getAttributes()) {
			attributes.add(reportAttributeBuilder.build(attribute));
		}
		return new ReportTitle(attributes, infoTitle);
	}
}
