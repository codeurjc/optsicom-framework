package es.optsicom.lib.analyzer.tablecreator;

import java.util.Arrays;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.pr.GapRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.SummarizeMode;

public class BoundsAttributeTableCreator extends AttributedTableCreator {

	@Override
	public AttributedTable buildTable() {

		this.addStatisticGroup(StatisticGroup
				.createMultipleStatisticGroup(new GapRP(),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(
										SummarizeMode.AVERAGE,
										NumberType.PERCENT), "Gap") },
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(
										SummarizeMode.SUM, NumberType.INTEGER),
								"#Opt") }));
				
				
		this.addStatisticGroup(StatisticGroup
				.createMultipleStatisticGroup(new LastEventRP("lowerBound"),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(
										SummarizeMode.AVERAGE,
										NumberType.DECIMAL), "Lower Bound") }));
		
		this.addStatisticGroup(StatisticGroup.createMultipleStatisticGroup(new LastEventRP(
						"upperBound"), new Statistic[] { new Statistic(
						new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
								NumberType.DECIMAL), "Upper Bound") }));
		
		this.addStatisticGroup(StatisticGroup.createMultipleStatisticGroup(new LastEventRP(
						Event.OBJ_VALUE_EVENT), new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
										NumberType.DECIMAL), "SolutionValue") }));

		return super.buildTable();

	}

}
