package es.optsicom.lib.analyzer.tablecreator.atttable;

import java.awt.Color;

public class TableValue implements Comparable<TableValue> {

	private Double value;
	private Color color;

	public TableValue(Double value) {
		super();
		this.value = value;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Double getValue() {
		return value;
	}

	public int compareTo(TableValue o) {
		if (value == null && o.value == null) {
			return 0;
		} else if (value == null) {
			return 1;
		} else if (o.value == null) {
			return -1;
		} else {
			return Double.compare(this.value, o.value);
		}
	}

}
