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

private void constructorFromTable(Table table) {
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


}
