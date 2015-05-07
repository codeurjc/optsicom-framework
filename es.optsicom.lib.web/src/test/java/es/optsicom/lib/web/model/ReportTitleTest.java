package es.optsicom.lib.web.model;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.web.model.builder.ReportTitleBuilder;
public class ReportTitleTest {
	private Title title = new Title();
	private Title nullTitle;
	private ReportTitle reportTitle;
	private ReportTitleBuilder reportTitleBuilder = new ReportTitleBuilder();
	
	@Test
	public void constructorTitleIsCalledWithoutProblems(){
		reportTitle = reportTitleBuilder.build(nullTitle);
		reportTitle = reportTitleBuilder.build(title);
		reportTitle = new ReportTitle(new ArrayList<ReportAttribute>(),"info");
		assertFalse(reportTitle.getAttributes() == null);
		assertTrue("info".equals(reportTitle.getInfoTitle()));
	}
	

}
