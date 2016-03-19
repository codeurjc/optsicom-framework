package es.optsicom.lib.analyzer;

public class DefaultReportConf extends ReportConf {

	public DefaultReportConf() {
		addBlockBuilder(new InstancesSummaryBlockBuilder());
		addBlockBuilder(new StatisticBlockBuilder());
		addBlockBuilder(new InstancesBlockBuilder());
		addBlockBuilder(new InstancesPropertiesBlockBuilder());
	}
}
