package es.optsicom.lib.analyzer;

public class ValuesByMethodReportConf extends ReportConf {

	public ValuesByMethodReportConf() {
		addBlockBuilder(new OnlyValueInstancesBlockBuilder());
		addBlockBuilder(new InstancesSummaryBlockBuilder());
	}

}
