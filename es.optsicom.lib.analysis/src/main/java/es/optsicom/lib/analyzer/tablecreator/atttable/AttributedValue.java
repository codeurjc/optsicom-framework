/**
 * 
 */
package es.optsicom.lib.analyzer.tablecreator.atttable;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import es.optsicom.lib.analyzer.report.table.NumericFormat;

public class AttributedValue {

	private TableValue tableValue;
	private Map<String, Attribute> attributes;
	private NumericFormat numberFormat;

	public AttributedValue(TableValue value, NumericFormat numberFormat,
			Map<String, Attribute> attributes) {
		this.tableValue = value;
		this.numberFormat = numberFormat;
		this.attributes = attributes;
	}

	public AttributedValue(TableValue tableValue, NumericFormat numberFormat,
			Attribute... attributes) {
		this.tableValue = tableValue;
		this.numberFormat = numberFormat;
		this.attributes = new HashMap<String, Attribute>();
		for (Attribute attribute : attributes) {
			this.attributes.put(attribute.getName(), attribute);
		}
	}

	public Double getValue() {
		return tableValue.getValue();
	}
	
	public Color getColor() {
		return tableValue.getColor();
	}

	public Map<String, Attribute> getAttributes() {
		return attributes;
	}

	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}

	public NumericFormat getNumberFormat() {
		return numberFormat;
	}

	@Override
	public String toString() {
		return "AttributedValue [value=" + tableValue + ", attributes="
				+ attributes + ", numberFormat=" + numberFormat + "]";
	}
}