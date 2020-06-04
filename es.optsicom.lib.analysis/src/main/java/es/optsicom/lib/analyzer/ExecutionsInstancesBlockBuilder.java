package es.optsicom.lib.analyzer;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.report.table.Table;
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
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class ExecutionsInstancesBlockBuilder extends BlockBuilder {

	public void buildPages(ExperimentManager experimentResults) {

		BestMode bestMode = experimentResults.getProblemBestMode();

		StatisticGroup[] statisticGroups = {
				StatisticGroup
						.createMultipleStatisticGroup(
								new LastEventRP(Event.OBJ_VALUE_EVENT, SummarizeMode.MAX, -1, Source.VALUE),
								new Statistic[] { new Statistic(new DevStatisticCalc(bestMode), "Dev (Max)"),
										new Statistic(new NumBestStatisticCalc(bestMode), "#Best (Max)"),
										new Statistic(new ScoreStatisticCalc(bestMode), "Score (Max)"),
										new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.FIRST,
												NumberType.DECIMAL), "Value (Max)") }),
				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP(Event.OBJ_VALUE_EVENT, SummarizeMode.MIN, -1, Source.VALUE),
						new Statistic[] { new Statistic(new DevStatisticCalc(bestMode), "Dev (Min)"),
								new Statistic(new NumBestStatisticCalc(bestMode), "#Best (Min)"),
								new Statistic(new ScoreStatisticCalc(bestMode), "Score (Min)"),
								new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.FIRST, NumberType.DECIMAL),
										"Value (Min)") }),

				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP(Event.OBJ_VALUE_EVENT, SummarizeMode.AVERAGE, -1, Source.VALUE),
						new Statistic[] { new Statistic(new DevStatisticCalc(bestMode), "Dev (Avg)"),
								new Statistic(new NumBestStatisticCalc(bestMode), "#Best (Avg)"),
								new Statistic(new ScoreStatisticCalc(bestMode), "Score (Avg)"),
								new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.FIRST, NumberType.DECIMAL),
										"Value (Avg)") }),

				StatisticGroup
						.createMultipleStatisticGroup(
								new LastEventRP(Event.FINISH_TIME_EVENT)
										.setSource(
												Source.TIMESTAMP),
								new Statistic[] { new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
										NumberType.DECIMAL, BestMode.MIN_IS_BEST), "Time (Avg)") }),

				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP("constructiveImprovement.iterationsPerformed"),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.DECIMAL),
								"#Const") }) };

		CommonApproxAttributeTableCreator tableCreator = new CommonApproxAttributeTableCreator(statisticGroups);
		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("method", "statistic");
		ttCreator.setRowsAttributes("instance");

		Table tt = ttCreator.createTitleTable(attTable);

		setBlock(new ReportBlock("By Instance", tt));

	}
}
