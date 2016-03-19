package es.optsicom.lib.analyzer.tablecreator;

import java.util.Arrays;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.pr.GapRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP.Source;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.SummarizeMode;

public class ExactAttributeTableCreator extends AttributedTableCreator {

	public ExactAttributeTableCreator() {

	}

	@Override
	public AttributedTable buildTable() {

		this.setStatisticGroups(Arrays.asList(
				StatisticGroup.createMultipleStatisticGroup(
						new GapRP(),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.PERCENT), "Gap") },
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.SUM, NumberType.INTEGER), "#Opt") }),
				StatisticGroup.createSimpleStatisticGroup(
						new LastEventRP(Event.FINISH_TIME_EVENT).setSource(Source.TIMESTAMP),
						new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.DECIMAL),
								"CPU Time"))));

		return super.buildTable();

	}

}
