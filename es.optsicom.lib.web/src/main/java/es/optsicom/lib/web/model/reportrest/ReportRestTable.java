package es.optsicom.lib.web.model.reportrest;

import java.util.ArrayList;
import java.util.List;

public class ReportRestTable {

	private String title;
	private List<List<ReportRestCell>> cellValues;
	private List<ReportRestTitle> rowTitles;
	private List<ReportRestTitle> columnTitles;

	public ReportRestTable() {
		this.title = "";
		this.cellValues = new ArrayList<>();
		this.rowTitles = new ArrayList<>();
		this.columnTitles = new ArrayList<>();
	}

	public ReportRestTable(String title, List<List<ReportRestCell>> cellValues, List<ReportRestTitle> rowTitles,
			List<ReportRestTitle> columnTitles) {
		super();
		this.title = title;
		this.cellValues = cellValues;
		this.rowTitles = rowTitles;
		this.columnTitles = columnTitles;
	}

	public String getTitle() {
		return title;
	}

	public List<List<ReportRestCell>> getCellValues() {
		return cellValues;
	}

	public List<ReportRestTitle> getRowTitles() {
		return rowTitles;
	}

	public List<ReportRestTitle> getColumnTitles() {
		return columnTitles;
	}

}
