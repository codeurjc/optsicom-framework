package es.optsicom.lib.analyzer;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.tablecreator.TempEvolutionApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTableTitleTableCreator;
import es.optsicom.lib.analyzer.tablecreator.group.InstanceGroupMaker;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.util.RelativizeMode;

public class TempEvolutionSummaryBlockBuilder extends BlockBuilder {

	private long timeLimit;
	private int numSteps;

	public TempEvolutionSummaryBlockBuilder(long timeLimit, int numSteps) {
		this.timeLimit = timeLimit;
		this.numSteps = numSteps;
	}

	@Override
	public void buildPages(ExperimentManager experimentResults) {
		ReportPage reportPage = new ReportPage("Temp Evolution");
		reportPage.addReportElement(createTempEvolution(experimentResults));
		setBlock(new ReportBlock(reportPage));
	}

	private ReportElement createTempEvolution(ExperimentManager experimentResults) {

		TempEvolutionApproxAttributeTableCreator tableCreator = new TempEvolutionApproxAttributeTableCreator(timeLimit);
		tableCreator.setNumSteps(numSteps);
		tableCreator.setExperimentResults(experimentResults);
		tableCreator.setBestMode(experimentResults.getProblemBestMode());
		tableCreator.setRelativezeMode(RelativizeMode.OPTIMUM);
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker.getOneGroup());

		AttributedTable at = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		return ttCreator.createTitleTable(at);
	}

}
