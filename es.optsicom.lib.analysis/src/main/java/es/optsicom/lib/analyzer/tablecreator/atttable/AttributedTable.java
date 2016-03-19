package es.optsicom.lib.analyzer.tablecreator.atttable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.optsicom.lib.analyzer.report.table.NumericFormat;

public class AttributedTable {

	private List<AttributedValue> values = new ArrayList<AttributedValue>();
	private Map<String, Set<Attribute>> attributes = new HashMap<String, Set<Attribute>>();
	private Map<String, List<Attribute>> sortedAttributeValues = new HashMap<String, List<Attribute>>();

	public void addValue(AttributedValue value) {

		this.values.add(value);

		for (Map.Entry<String, Attribute> attribute : value.getAttributes().entrySet()) {
			Set<Attribute> values = attributes.get(attribute.getKey());
			if (values == null) {
				values = new HashSet<Attribute>();
				attributes.put(attribute.getKey(), values);
			}
			values.add(attribute.getValue());
		}
	}

	public void addValue(TableValue value, NumericFormat numberFormat, Attribute... attributes) {
		this.addValue(new AttributedValue(value, numberFormat, attributes));
	}

	public void addValue(TableValue value, NumericFormat numberFormat, Map<String, Attribute> attributes) {
		this.addValue(new AttributedValue(value, numberFormat, attributes));
	}

	public List<AttributedValue> getValues() {
		return values;
	}

	public Map<String, Set<Attribute>> getAttributes() {
		return attributes;
	}

	public void addSortedAttributes(String attName, List<Attribute> attributes) {
		sortedAttributeValues.put(attName, attributes);
	}

	public Map<String, List<Attribute>> getSortedAttributes() {

		// Ordenamos los que no estén ordenados.
		Set<String> attributeNames = new HashSet<String>(attributes.keySet());
		attributeNames.removeAll(sortedAttributeValues.keySet());
		for (String attName : attributeNames) {
			Set<Attribute> atts = attributes.get(attName);
			List<Attribute> attValues = new ArrayList<Attribute>(atts);
			Collections.sort(attValues);
			sortedAttributeValues.put(attName, attValues);
		}
		// System.out.println(sortedAttributeValues);
		return sortedAttributeValues;
	}

}
