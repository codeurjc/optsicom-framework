package es.optsicom.lib.analyzer.report;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.table.Table;

public class ReportBlock {

	private String name;
	List<ReportPage> pages = new ArrayList<ReportPage>();
	
	public ReportBlock(String name, Table tt) {
		this(name);
		ReportPage page = new ReportPage(name, tt);
		pages.add(page);
	}
	
	public ReportBlock(String name, List<ReportPage> reportPages) {
		this(name);
		pages = reportPages;
	}

	public ReportBlock(String sheetName) {
		this.name = sheetName;
	}

	public ReportBlock(ReportPage reportPage) {
		this.name = reportPage.getName();
		pages.add(reportPage);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;		
	}
	
	public List<ReportPage> getReportPages() {
		return this.pages;
	}
}
