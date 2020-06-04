package es.optsicom.lib.analyzer.report.table;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class LeyendElement {

	private String title;
	private List<Attribute> attributes;

	public LeyendElement(String title, List<Attribute> attributes) {
		super();
		this.title = title;
		this.attributes = attributes;
	}

	public String getTitle() {
		return title;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

}
