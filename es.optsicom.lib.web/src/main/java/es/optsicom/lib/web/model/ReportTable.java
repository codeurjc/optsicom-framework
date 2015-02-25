package es.optsicom.lib.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;


public class ReportTable {
private List<List<Double>> cellValues;
private List<ReportTitle> rowTitles;
private List<ReportTitle> columnTitles;
private static final Log LOG = LogFactory.getLog(ReportTable.class);


public ReportTable(List<List<Double>> cellValues, List<ReportTitle> rowTitles,
		List<ReportTitle> columnTitles) {
	this.cellValues = cellValues;
	this.rowTitles = rowTitles;
	this.columnTitles = columnTitles;
}

public ReportTable(Table table) {
	constructorFromTable(table);
}

protected void constructorFromTable(Table table) {
	extractCellValues(table);
	this.rowTitles = new ArrayList<ReportTitle>();
	for (Title title:table.getRowTitles()){
		rowTitles.add(new ReportTitle(title));
	}
	this.columnTitles= new ArrayList<ReportTitle>();
	for (Title title:table.getColumnTitles()){
		this.columnTitles.add(new ReportTitle(title));
	}
}

private void extractCellValues(Table table) {
	this.cellValues = new ArrayList<List<Double>>();
	for (int i = 0; i < table.getNumRows();i++){
		List<Double> cellRowValues = new ArrayList<Double>();
		for (int j = 0; j < table.getNumColumns();j++){
			
			Double auxValue;
			try {
				Object cellValue = table.getCell(i, j).getValue();
				auxValue = (Double) cellValue;
			} catch (Exception e) {
				auxValue = 0.0;
				LOG.info("ReportTable -> [" + i + "][" + j + "] is not double" );
			}
			cellRowValues.add(auxValue);
		}
		this.cellValues.add(cellRowValues);
	}
}

public List<List<Double>> getCellValues() {
	return cellValues;
}


public void setCellValues(List<List<Double>> cellValues) {
	this.cellValues = cellValues;
}


public List<ReportTitle> getRowTitles() {
	return rowTitles;
}


public void setRowTitles(List<ReportTitle> rowTitles) {
	this.rowTitles = rowTitles;
}


public List<ReportTitle> getColumnTitles() {
	return columnTitles;
}


public void setColumnTitles(List<ReportTitle> columnTitles) {
	this.columnTitles = columnTitles;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
			+ ((cellValues == null) ? 0 : cellValues.hashCode());
	result = prime * result
			+ ((columnTitles == null) ? 0 : columnTitles.hashCode());
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
	return "ReportTable [cellValues=" + cellValues + ", rowTitles=" + rowTitles
			+ ", columnTitles=" + columnTitles + "]";
}


}
