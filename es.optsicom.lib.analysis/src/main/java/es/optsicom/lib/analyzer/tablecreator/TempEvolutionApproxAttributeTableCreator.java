package es.optsicom.lib.analyzer.tablecreator;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.DevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.LastRPRelativeValueProvider;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.StatisticCalc;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class TempEvolutionApproxAttributeTableCreator extends AttributedTableCreator {

	private BestMode bestMode = BestMode.MAX_IS_BEST;
	private long executionTime = -1;
	private int numSteps = 10;
	private StatisticCalc statistic;

	/**
	 * Creates a new object for building time tables with the specified time
	 * limit.
	 * 
	 * @param executionTime
	 *            Execution time limit in milliseconds to consider for table.
	 */
	public TempEvolutionApproxAttributeTableCreator(long executionTime) {
		this(executionTime, null);
	}

	public TempEvolutionApproxAttributeTableCreator(long executionTime, StatisticCalc statistic) {
		this.executionTime = executionTime;
		this.statistic = statistic;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void setBestMode(BestMode bestMode) {
		this.bestMode = bestMode;
	}

	@Override
	public AttributedTable buildTable() {

		List<StatisticGroup> statisticGroups = new ArrayList<StatisticGroup>();

		long stepDuration = executionTime / numSteps;

		if (statistic == null) {
			this.statistic = new DevStatisticCalc();

			((DevStatisticCalc) statistic)
					.setRelativeValueProvider(new LastRPRelativeValueProvider(new LastEventRP(Event.OBJ_VALUE_EVENT), 0,
							bestMode == BestMode.MAX_IS_BEST ? SummarizeMode.MAX : SummarizeMode.MIN));
		}

		this.statistic.setResultsBestMode(bestMode);

		for (int i = 0; i < numSteps; i++) {
			long timelimit = i * stepDuration;

			statisticGroups
					.add(StatisticGroup.createMultipleStatisticGroup(new LastEventRP(Event.OBJ_VALUE_EVENT, timelimit),
							new Statistic[] { new Statistic(statistic, Long.toString(timelimit)) }));
		}

		statisticGroups.add(StatisticGroup.createMultipleStatisticGroup(new LastEventRP(Event.OBJ_VALUE_EVENT),
				new Statistic[] { new Statistic(statistic, Long.toString(executionTime)) }));

		this.setStatisticGroups(statisticGroups);

		return super.buildTable();

	}

}
