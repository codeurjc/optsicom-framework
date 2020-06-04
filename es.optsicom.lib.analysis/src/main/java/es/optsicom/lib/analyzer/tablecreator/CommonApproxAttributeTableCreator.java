package es.optsicom.lib.analyzer.tablecreator;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.pr.FirtsOfSameValueAsLastEventRP;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.DevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NumBestStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.ScoreStatisticCalc;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class CommonApproxAttributeTableCreator extends AttributedTableCreator {

	private StatisticGroup[] groups;

	public CommonApproxAttributeTableCreator(StatisticGroup... groups) {
		this.groups = groups;
	}

	@Override
	public AttributedTable buildTable() {

		if (groups.length == 0) {
			addStatisticGroup(StatisticGroup.createMultipleStatisticGroup(new LastEventRP(Event.OBJ_VALUE_EVENT),
					new Statistic[] {

							// TODO Buscar una mejor forma de configurar los
							// informes al ejecutar los experimentos
							new Statistic(new DevStatisticCalc(bestMode), "Dev"),
							new Statistic(new NumBestStatisticCalc(bestMode), "#Best"),
							new Statistic(new ScoreStatisticCalc(bestMode), "Score"),

					// new Statistic(new FeasStatisticCalc(
					// bestMode), "Feas"),
					// new Statistic(new FeasDevStatisticCalc(
					// bestMode), "DevFeas"),
					// new Statistic(new FeasNumBestStatisticCalc(
					// bestMode), "#BestFeas"),
					// new Statistic(new ScoreStatisticCalc(
					// bestMode), "Score"),
					// new Statistic(new ScoreFeasStatisticCalc(
					// bestMode), "ScoreFeas"),
					}));

			addStatisticGroup(
					StatisticGroup
							.createMultipleStatisticGroup(
									new LastEventRP(Event.FINISH_TIME_EVENT)
											.setSource(
													LastEventRP.Source.TIMESTAMP),
									new Statistic[] {
											new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
													NumberType.DECIMAL, BestMode.MIN_IS_BEST), "Time") }));

			addStatisticGroup(
					StatisticGroup
							.createMultipleStatisticGroup(
									new FirtsOfSameValueAsLastEventRP(Event.OBJ_VALUE_EVENT)
											.setSource(FirtsOfSameValueAsLastEventRP.Source.TIMESTAMP),
									new Statistic[] {
											new Statistic(
													new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE,
															NumberType.DECIMAL, BestMode.MIN_IS_BEST),
													"Time to best") }));

			addStatisticGroup(StatisticGroup
					.createMultipleStatisticGroup(new LastEventRP("constructiveImprovement.iterationsPerformed"),
							new Statistic[] { new Statistic(
									new NonRelativizerStatisticCalc(SummarizeMode.AVERAGE, NumberType.INTEGER),
									"#Const") }));

		} else {
			for (StatisticGroup group : groups) {
				this.addStatisticGroup(group);
			}
		}

		return super.buildTable();
	}
}
