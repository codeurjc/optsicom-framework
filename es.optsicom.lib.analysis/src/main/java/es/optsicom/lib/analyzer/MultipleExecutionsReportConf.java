package es.optsicom.lib.analyzer;

public class MultipleExecutionsReportConf extends ReportConf {

	public MultipleExecutionsReportConf() {
		addBlockBuilder(new ExecutionsInstancesBlockBuilder());
		// addBlockBuilder(new ExecutionsInstancesSummaryBlockBuilder());
		// addBlockBuilder(new InstancesPropertiesBlockBuilder());
	}
}
