package es.optsicom.lib.web.model.reportrest;

import java.util.List;

public class ReportRestBlock {

	private String name;
	private List<ReportRestTable> tables;

	public ReportRestBlock() {
	}

	public ReportRestBlock(String name, List<ReportRestTable> tables) {
		this.name = name;
		this.tables = tables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReportRestTable> getTables() {
		return tables;
	}

	public void setTables(List<ReportRestTable> tables) {
		this.tables = tables;
	}

}
