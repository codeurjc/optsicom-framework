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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((reportConfiguration == null) ? 0 : reportConfiguration
						.hashCode());
		result = prime * result
				+ ((reportTables == null) ? 0 : reportTables.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReportRest))
			return false;
		ReportRest other = (ReportRest) obj;
		if (reportConfiguration == null) {
			if (other.reportConfiguration != null)
				return false;
		} else if (!reportConfiguration.equals(other.reportConfiguration))
			return false;
		if (reportTables == null) {
			if (other.reportTables != null)
				return false;
		} else if (!reportTables.equals(other.reportTables))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ReportRest [reportConfiguration=" + reportConfiguration
				+ ", reportTables=" + reportTables + "]";
	}
	
}
