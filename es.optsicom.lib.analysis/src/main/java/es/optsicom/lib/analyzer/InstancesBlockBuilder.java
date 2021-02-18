package es.optsicom.lib.analyzer;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.CommonApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.Statistic;
import es.optsicom.lib.analyzer.tablecreator.StatisticGroup;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTableTitleTableCreator;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP.Source;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.DevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NumBestStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.ScoreStatisticCalc;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class InstancesBlockBuilder extends BlockBuilder {

	public void buildPages(ExperimentManager experimentResults) {

		BestMode bestMode = experimentResults.getProblemBestMode();

		StatisticGroup[] statisticGroups = {
				StatisticGroup.createMultipleStatisticGroup(new LastEventRP(Event.OBJ_VALUE_EVENT), new Statistic[] {

						new Statistic(new DevStatisticCalc(bestMode), "Dev"),
						new Statistic(new NumBestStatisticCalc(bestMode), "#Best"),
						new Statistic(new ScoreStatisticCalc(bestMode), "Score"),

						// new Statistic(new FeasStatisticCalc(
						// bestMode), "Feas"),
						// new Statistic(new FeasDevStatisticCalc(
						// bestMode), "DevFeas"),
						// new Statistic(new FeasAllDevStatisticCalc(
						// bestMode), "DevAllFeas"),
						// new Statistic(new FeasNumBestStatisticCalc(
						// bestMode), "#BestFeas"),
						// new Statistic(new ScoreStatisticCalc(
						// bestMode), "Score"),
						// new Statistic(new ScoreFeasStatisticCalc(
						// bestMode), "ScoreFeas"),
						new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.FIRST, NumberType.DECIMAL),
								"Value") }),

				StatisticGroup
						.createMultipleStatisticGroup(
								new LastEventRP(Event.FINISH_TIME_EVENT)
										.setSource(
												Source.TIMESTAMP),
								new Statistic[] { new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
										NumberType.DECIMAL, BestMode.MIN_IS_BEST), "Time") }),

				StatisticGroup
						.createMultipleStatisticGroup(
								new LastEventRP(Event.OBJ_VALUE_EVENT)
										.setSource(
												Source.TIMESTAMP),
								new Statistic[] { new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.FIRST,
										NumberType.DECIMAL, BestMode.MIN_IS_BEST), "Time to best") }),

				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP("constructiveImprovement.iterationsPerformed"),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.INTEGER),
								"#Const") }),
				
				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP("constructiveImprovement.feasibleSolutions"),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.INTEGER),
								"#Feasible") }),
				
				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP("constructiveImprovement.infeasibleSolutions"),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.INTEGER),
								"#Infeasible") })};

		CommonApproxAttributeTableCreator tableCreator = new CommonApproxAttributeTableCreator(statisticGroups);
		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("method", "statistic");
		ttCreator.setRowsAttributes("instance");

		Table tt = ttCreator.createTitleTable(attTable);

		// List<MethodDescription> methods = experimentResults.getMethods();
		// List<Title> rowTitles = createMethodTitles(methods);
		// List<Title> colTitles = new ArrayList<Title>();
		// colTitles.add(new Title("Properties"));
		// Table desc = new Table(rowTitles, colTitles);
		//
		// for(int row = 0; row < rowTitles.size(); row++) {
		// for(int col = 0; col < colTitles.size(); col++) {
		// Cell cell = new
		// Cell(methods.get(row).getProperties().getPropsAString());
		// desc.setCell(row, col, cell);
		// }
		// }

		ReportPage page = new ReportPage("Detailed by instance");
		page.addReportElement(tt);
		// page.addReportElement(desc);

		setBlock(new ReportBlock(page));

	}

	private List<Title> createMethodTitles(List<MethodDescription> methods) {
		List<Title> titles = new ArrayList<Title>();
		for (MethodDescription md : methods) {
			titles.add(new Title(md.getName()));
		}
		return titles;
	}
}
