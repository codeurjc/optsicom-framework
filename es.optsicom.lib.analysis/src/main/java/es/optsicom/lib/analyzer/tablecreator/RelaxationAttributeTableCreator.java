package es.optsicom.lib.analyzer.tablecreator;

import java.util.Arrays;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP.Source;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.SummarizeMode;

public class RelaxationAttributeTableCreator extends AttributedTableCreator {

	@Override
	public AttributedTable buildTable() {

		this.setStatisticGroups(Arrays.asList(
				StatisticGroup.createMultipleStatisticGroup(new LastEventRP("relaxSolution"),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.DECIMAL),
								"Solution Value") }),
				StatisticGroup.createMultipleStatisticGroup(
						new LastEventRP(Event.FINISH_TIME_EVENT).setSource(Source.TIMESTAMP),
						new Statistic[] { new Statistic(
								new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.DECIMAL),
								"CPU Time") })));

		return super.buildTable();

	}

}
