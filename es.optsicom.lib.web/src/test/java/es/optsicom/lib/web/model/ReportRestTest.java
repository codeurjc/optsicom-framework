package es.optsicom.lib.web.model;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
public class ReportRestTest {
	
	private List<ReportTable> reportTables;
	private ReportConfiguration reportConfiguration;
	private ReportRest reportRest;
	
	@Before
	public void init(){
		reportTables = new ArrayList<ReportTable>();
		reportConfiguration = new ReportConfiguration();
	}
	
	@Test
	public void givenReportRestWhenGetAttributesThenICanAccessThem(){
		reportRest = new ReportRest(reportConfiguration,reportTables);
		assertFalse(reportRest.getReportConfiguration() == null);
		assertFalse(reportRest.getReportTables() == null);
		
	}
}
