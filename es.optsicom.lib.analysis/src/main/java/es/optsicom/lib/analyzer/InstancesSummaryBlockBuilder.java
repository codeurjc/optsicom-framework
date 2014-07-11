package es.optsicom.lib.analyzer;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.tablecreator.CommonApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.TempEvolutionApproxAttributeTableCreator;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTableTitleTableCreator;
import es.optsicom.lib.analyzer.tablecreator.group.InstanceGroupMaker;
import es.optsicom.lib.analyzer.tablecreator.pr.LastEventRP;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.FeasDevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.FeasStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.LastRPRelativeValueProvider;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class InstancesSummaryBlockBuilder extends BlockBuilder {

	public void buildPages(ExperimentManager expResults) {
		ReportPage reportPage = new ReportPage("Summary");
		reportPage.addReportElement(createAllInstancesTable(expResults));
		//reportPage.addReportElement(createFusionedTableTempEvolution(expResults));	
		//reportPage.addReportElement(createFusionedNumFeasibleTempEvolution(expResults));
		setBlock(new ReportBlock(reportPage));
	}
	
	private ReportElement createFusionedNumFeasibleTempEvolution(ExperimentManager experimentResults) {

		long timeLimit = experimentResults.getTimeLimit();
		
		if(timeLimit == -1){
			timeLimit = experimentResults.getMaxTimeLimit();
		}
		
		if(timeLimit == -1){
			System.out.println("WTF!");
		}
		
		timeLimit =60000;
		
		TempEvolutionApproxAttributeTableCreator tableCreator = new TempEvolutionApproxAttributeTableCreator(
				timeLimit,
				new FeasStatisticCalc());
		
		configTableCreator(experimentResults, tableCreator);
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker.getOneGroup());
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		return ttCreator.createTitleTable(attTable);

	}

	private Table createAllInstancesTable(ExperimentManager experimentResults) {

		CommonApproxAttributeTableCreator tableCreator = new CommonApproxAttributeTableCreator();
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker.getOneGroup());
		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();		

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		return ttCreator.createTitleTable(attTable);
		
	}

	private Table createFusionedTableTempEvolution(
			ExperimentManager experimentResults) {

		long timeLimit = experimentResults.getTimeLimit();
		
		if(timeLimit == -1){
			timeLimit = experimentResults.getMaxTimeLimit();
		}
		
		if(timeLimit == -1){
			System.out.println("WTF!");
		}
		
		timeLimit =60000;
		
		FeasDevStatisticCalc dev = new FeasDevStatisticCalc();
		dev.setRelativeValueProvider(
				new LastRPRelativeValueProvider(
						new LastEventRP(Event.OBJ_VALUE_EVENT),
						0,
						experimentResults.getProblemBestMode() == BestMode.MAX_IS_BEST ? SummarizeMode.MAX
								: SummarizeMode.MIN)
				);
		TempEvolutionApproxAttributeTableCreator tableCreator = new TempEvolutionApproxAttributeTableCreator(
				timeLimit,
				dev);
		
		configTableCreator(experimentResults, tableCreator);
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker.getOneGroup());
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		return ttCreator.createTitleTable(attTable);
		
	}

}
