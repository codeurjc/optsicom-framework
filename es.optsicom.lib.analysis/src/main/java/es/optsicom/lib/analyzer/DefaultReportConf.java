package es.optsicom.lib.analyzer;

public class DefaultReportConf extends ReportConf {

	public DefaultReportConf() {
		addBlockBuilder(new InstancesBlockBuilder());
		addBlockBuilder(new InstancesSummaryBlockBuilder());
		addBlockBuilder(new InstancesPropertiesBlockBuilder());
	}
}
