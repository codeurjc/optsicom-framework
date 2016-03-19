package es.optsicom.lib.analyzer.tablecreator.atttable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import es.optsicom.lib.analyzer.report.table.Cell;
import es.optsicom.lib.analyzer.report.table.Leyend;
import es.optsicom.lib.analyzer.report.table.LeyendElement;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;

public class AttributedTableTitleTableCreator {

	private List<String> colsAttributes;
	private List<String> rowsAttributes;
	private Map<String, List<Attribute>> attributes;

	public Table createTitleTable(AttributedTable attTable) {

		Set<String> sameValueAtts = calculateSameValueAtts(attTable);

		attributes = attTable.getSortedAttributes();
		for (String sameValueAtt : sameValueAtts) {
			attributes.remove(sameValueAtt);
		}

		Map<Set<Attribute>, AttributedValue> valuesIndexedByAtts = new HashMap<Set<Attribute>, AttributedValue>();
		for (AttributedValue attValue : attTable.getValues()) {

			Set<Attribute> attrs = new HashSet<Attribute>();
			for (Attribute att : attValue.getAttributes().values()) {
				if (!sameValueAtts.contains(att.getName())) {
					attrs.add(att);
				}
			}
			valuesIndexedByAtts.put(attrs, attValue);
		}

		rowsAttributes.removeAll(sameValueAtts);
		colsAttributes.removeAll(sameValueAtts);

		List<Title> rowTitles = createTitlesFromAttributes(rowsAttributes);
		List<Title> columnTitles = createTitlesFromAttributes(colsAttributes);

		Table table = new Table(rowTitles, columnTitles);

		int numRow = 0;
		for (Title rowTitle : rowTitles) {

			int numColumn = 0;
			for (Title columnTitle : columnTitles) {

				Set<Attribute> atts = new HashSet<Attribute>();
				atts.addAll(rowTitle.getAttributes());
				atts.addAll(columnTitle.getAttributes());

				AttributedValue attributedValue = valuesIndexedByAtts.get(atts);

				Cell cellValue = new Cell(attributedValue.getValue(), attributedValue.getNumberFormat());

				cellValue.setColor(attributedValue.getColor());

				table.setCell(numRow, numColumn, cellValue);

				numColumn++;
			}

			numRow++;
		}

		table.setLeyend(createLeyend(attTable));

		return table;
	}

	private Leyend createLeyend(AttributedTable attTable) {

		Leyend leyend = new Leyend();

		for (Entry<String, List<Attribute>> entry : attTable.getSortedAttributes().entrySet()) {
			leyend.addLeyentElement(new LeyendElement(entry.getKey(), entry.getValue()));
		}

		return leyend;
	}

	private Set<String> calculateSameValueAtts(AttributedTable table) {

		Map<String, Object> valueOfAtts = new HashMap<String, Object>();
		Set<String> nonEqualAtts = new HashSet<String>();

		for (AttributedValue attValue : table.getValues()) {

			for (Attribute att : attValue.getAttributes().values()) {

				String attName = att.getName();
				Object value = att.getValue();

				if (valueOfAtts.containsKey(attName)) {

					Object previousValue = valueOfAtts.get(attName);
					if (previousValue == null) {
						if (value != null) {
							nonEqualAtts.add(attName);
						}
					} else if (!previousValue.equals(value)) {
						nonEqualAtts.add(attName);
					}

				} else {
					valueOfAtts.put(attName, value);
				}
			}

		}

		Set<String> equalAtts = new HashSet<String>(valueOfAtts.keySet());
		equalAtts.removeAll(nonEqualAtts);
		return equalAtts;
	}

	private List<Title> createTitlesFromAttributes(List<String> attNames) {

		List<Title> titles = new ArrayList<Title>();
		List<Attribute> values = new ArrayList<Attribute>();
		createTitlesFromAttributes(attNames, 0, values, titles);
		return titles;
	}

	private void createTitlesFromAttributes(List<String> attNames, int attIndex, List<Attribute> attributes,
			List<Title> titles) {

		if (attIndex == attNames.size()) {

			List<Attribute> attributesCopy = new ArrayList<Attribute>(attributes);
			titles.add(new Title(attributesCopy));

		} else {

			String attName = attNames.get(attIndex);

			for (Attribute att : this.attributes.get(attName)) {

				attributes.add(att);
				createTitlesFromAttributes(attNames, attIndex + 1, attributes, titles);
				attributes.remove(attributes.size() - 1);
			}

		}
	}

	public void setColsAttributes(String... attributes) {
		this.colsAttributes = new ArrayList<String>(Arrays.asList(attributes));
	}

	public void setRowsAttributes(String... attributes) {
		this.rowsAttributes = new ArrayList<String>(Arrays.asList(attributes));
	}

}
