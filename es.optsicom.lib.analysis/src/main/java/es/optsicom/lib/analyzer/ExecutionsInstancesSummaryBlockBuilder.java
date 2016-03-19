package es.optsicom.lib.analyzer;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.tablecreator.CommonApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.Statistic;
import es.optsicom.lib.analyzer.tablecreator.StatisticGroup;
import es.optsicom.lib.analyzer.tablecreator.TempEvolutionApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTableTitleTableCreator;
import es.optsicom.lib.analyzer.tablecreator.group.InstanceGroupMaker;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP.Source;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.DevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NumBestStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.ScoreStatisticCalc;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class ExecutionsInstancesSummaryBlockBuilder extends BlockBuilder {

	public void buildPages(ExperimentManager expResults) {
		ReportPage reportPage = new ReportPage("All Instances");
		reportPage.addReportElement(createAllInstancesTable(expResults));
		reportPage.addReportElement(createFusionedTableTempEvolution(expResults));
		setBlock(new ReportBlock(reportPage));
	}

	private Table createAllInstancesTable(ExperimentManager experimentResults) {
		StatisticGroup sg1 = StatisticGroup.createMultipleStatisticGroup(
				new LastEventRP(Event.OBJ_VALUE_EVENT, SummarizeMode.MAX, -1, Source.VALUE),
				new Statistic[] {
						new Statistic(new DevStatisticCalc(experimentResults.getProblemBestMode()), "Dev (Max)"),
						new Statistic(new NumBestStatisticCalc(experimentResults.getProblemBestMode()), "#Best (Max)"),
						new Statistic(new ScoreStatisticCalc(experimentResults.getProblemBestMode()),
								"Score (Max)"), });
		StatisticGroup sg2 = StatisticGroup.createMultipleStatisticGroup(
				new LastEventRP(Event.OBJ_VALUE_EVENT, SummarizeMode.MIN, -1, Source.VALUE),
				new Statistic[] {
						new Statistic(new DevStatisticCalc(experimentResults.getProblemBestMode()), "Dev (Min)"),
						new Statistic(new NumBestStatisticCalc(experimentResults.getProblemBestMode()), "#Best (Min)"),
						new Statistic(new ScoreStatisticCalc(experimentResults.getProblemBestMode()),
								"Score (Min)"), });

		StatisticGroup sg3 = StatisticGroup.createMultipleStatisticGroup(
				new LastEventRP(Event.OBJ_VALUE_EVENT, SummarizeMode.AVERAGE, -1, Source.VALUE),
				new Statistic[] {
						new Statistic(new DevStatisticCalc(experimentResults.getProblemBestMode()), "Dev (Avg)"),
						new Statistic(new NumBestStatisticCalc(experimentResults.getProblemBestMode()), "#Best (Avg)"),
						new Statistic(new ScoreStatisticCalc(experimentResults.getProblemBestMode()),
								"Score (Avg)"), });

		StatisticGroup sg4 = StatisticGroup
				.createMultipleStatisticGroup(
						new LastEventRP(Event.FINISH_TIME_EVENT)
								.setSource(
										Source.TIMESTAMP),
						new Statistic[] { new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
								NumberType.DECIMAL, BestMode.MIN_IS_BEST), "Time (Avg)") });

		CommonApproxAttributeTableCreator tableCreator = new CommonApproxAttributeTableCreator(sg1, sg2, sg3, sg4);
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker.getOneGroup());
		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		return ttCreator.createTitleTable(attTable);

	}

	private Table createFusionedTableTempEvolution(ExperimentManager experimentResults) {

		long timeLimit = experimentResults.getTimeLimit();

		if (timeLimit == -1) {
			timeLimit = experimentResults.getMaxTimeLimit();
		}

		// if(timeLimit == -1){
		// System.out.println("WTF!");
		// }

		// timeLimit = 10000;

		TempEvolutionApproxAttributeTableCreator tableCreator = new TempEvolutionApproxAttributeTableCreator(timeLimit);

		configTableCreator(experimentResults, tableCreator);
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker.getOneGroup());
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		return ttCreator.createTitleTable(attTable);

	}

}
