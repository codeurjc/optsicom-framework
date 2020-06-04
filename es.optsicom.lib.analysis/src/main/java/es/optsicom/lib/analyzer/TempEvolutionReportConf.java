package es.optsicom.lib.analyzer;

public class TempEvolutionReportConf extends ReportConf {

	public TempEvolutionReportConf(long timeLimit, int numSteps) {
		addBlockBuilder(new TempEvolutionSummaryBlockBuilder(timeLimit, numSteps));
	}

}
