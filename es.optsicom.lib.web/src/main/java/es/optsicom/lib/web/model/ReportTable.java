package es.optsicom.lib.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportTable {
	private final List<List<Double>> cellValues;
	private final List<ReportTitle> rowTitles;
	private final List<ReportTitle> columnTitles;
	private static final Log LOG = LogFactory.getLog(ReportTable.class);

	public ReportTable() {
		this.rowTitles = new ArrayList<ReportTitle>();
		this.columnTitles = new ArrayList<ReportTitle>();
		this.cellValues = new ArrayList<List<Double>>();
	}

	public ReportTable(List<List<Double>> cellValues, List<ReportTitle> rowTitles, List<ReportTitle> columnTitles) {
		this.cellValues = cellValues;
		this.rowTitles = rowTitles;
		this.columnTitles = columnTitles;
	}

	public List<List<Double>> getCellValues() {
		return cellValues;
	}

	public List<ReportTitle> getRowTitles() {
		return rowTitles;
	}

	public List<ReportTitle> getColumnTitles() {
		return columnTitles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellValues == null) ? 0 : cellValues.hashCode());
		result = prime * result + ((columnTitles == null) ? 0 : columnTitles.hashCode());
		result = prime * result + ((rowTitles == null) ? 0 : rowTitles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ReportTable)) {
			return false;
		}
		ReportTable other = (ReportTable) obj;
		if (cellValues == null) {
			if (other.cellValues != null) {
				return false;
			}
		} else if (!cellValues.equals(other.cellValues)) {
			return false;
		}
		if (columnTitles == null) {
			if (other.columnTitles != null) {
				return false;
			}
		} else if (!columnTitles.equals(other.columnTitles)) {
			return false;
		}
		if (rowTitles == null) {
			if (other.rowTitles != null) {
				return false;
			}
		} else if (!rowTitles.equals(other.rowTitles)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ReportTable [cellValues=" + cellValues + ", rowTitles=" + rowTitles + ", columnTitles=" + columnTitles
				+ "]";
	}

}
