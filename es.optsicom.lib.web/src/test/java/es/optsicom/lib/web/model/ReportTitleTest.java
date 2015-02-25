package es.optsicom.lib.web.model;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import es.optsicom.lib.analyzer.report.table.Title;
public class ReportTitleTest {
	private Title title = new Title();
	private Title nullTitle;
	private ReportTitle reportTitle;
	
	@Test
	public void constructorTitleIsCalledWithoutProblems(){
		reportTitle = new ReportTitle(nullTitle);
		reportTitle = new ReportTitle(title);
		reportTitle.setInfoTitle("info");
		reportTitle.setAttributes(new ArrayList<ReportAttribute>());
		assertFalse(reportTitle.getAttributes() == null);
		assertTrue("info".equals(reportTitle.getInfoTitle()));
	}
	

}
