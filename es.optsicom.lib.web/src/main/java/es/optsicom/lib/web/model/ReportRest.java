package es.optsicom.lib.web.model;

import java.util.List;

public class ReportRest {
	private final ReportConfiguration reportConfiguration;
	private final List<ReportTable> reportTables;
	
	public ReportRest(ReportConfiguration reportConfiguration, List<ReportTable> reportTables) {
		this.reportTables = reportTables;
		this.reportConfiguration = reportConfiguration;
	}
	public List<ReportTable> getReportTables() {
		return reportTables;
	}
	public ReportConfiguration getReportConfiguration() {
		return reportConfiguration;
	}
}
