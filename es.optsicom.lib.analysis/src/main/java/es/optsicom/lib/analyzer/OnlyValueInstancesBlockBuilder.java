package es.optsicom.lib.analyzer;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.tablecreator.CommonApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.Statistic;
import es.optsicom.lib.analyzer.tablecreator.StatisticGroup;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTableTitleTableCreator;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NonRelativizerStatisticCalc;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.SummarizeMode;

public class OnlyValueInstancesBlockBuilder extends BlockBuilder {

	public void buildPages(ExperimentManager experimentResults) {

		StatisticGroup[] statisticGroups = {StatisticGroup
				.createMultipleStatisticGroup(new LastEventRP(Event.OBJ_VALUE_EVENT),
						new Statistic[] {
								new Statistic(new NonRelativizerStatisticCalc(SummarizeMode.FIRST, NumberType.DECIMAL), "Value")})};
				
		CommonApproxAttributeTableCreator tableCreator = new CommonApproxAttributeTableCreator(statisticGroups);
		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("method", "statistic");
		ttCreator.setRowsAttributes("instance");

		Table tt = ttCreator.createTitleTable(attTable);

		setBlock(new ReportBlock("Values Instance Method", tt));

	}

}
