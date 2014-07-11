package es.optsicom.lib.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.optsicom.lib.analyzer.report.ReportBlock;
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
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.CombinationGenerator;
import es.optsicom.lib.util.SummarizeMode;

public class InstancesPropertiesBlockBuilder extends BlockBuilder {

	private List<String> propsConsidered = new ArrayList<String>();
	private List<String> propsToFilterOut = new ArrayList<String>();
	
	public InstancesPropertiesBlockBuilder() {
		propsToFilterOut.addAll(Arrays.asList("usecase", "name", "class",
				"filename", "num"));
	}
	
	public InstancesPropertiesBlockBuilder(String... propsToConsider) {
		propsConsidered.addAll(propsConsidered);
	}
	
	public void buildPages(ExperimentManager experimentResults) {

		// We suppose that all instances has the same properties (very common
		// case)
		InstanceDescription instance = experimentResults.getInstances().get(0);

		List<String> propNames = new ArrayList<String>(instance.getProperties()
				.keySet());

		// TODO We need to make explicit "num" property when loading
		// instancefiles
		if(propsConsidered.isEmpty()) {
			propNames.removeAll(propsToFilterOut);
		} else {
			propNames.retainAll(propsConsidered);
		}

		filterProperties(experimentResults.getInstances(), propNames);

		List<List<String>> allPropertyCombinations = CombinationGenerator
				.createCombinations(propNames);

		List<ReportPage> pages = new ArrayList<ReportPage>();
		
		Map<String,Integer> sheetNames = new HashMap<String,Integer>();
		
		for (List<String> propCombination : allPropertyCombinations) {
			
			String sheetName = "By "+propCombination.toString();
			
			sheetName = sheetName.replaceAll("\\[","");
			sheetName = sheetName.replaceAll("\\]","");
			
			//TODO Move this to the Excel exporter
			if(sheetName.length() > 28){
				sheetName = sheetName.substring(0,28);
			}
			
			Integer number = sheetNames.get(sheetName);
			if(number == null){
				sheetNames.put(sheetName,1);
			} else {
				number++;
				sheetNames.put(sheetName,number);
				sheetName += number;
			}
			
			ReportPage reportPage = new ReportPage(sheetName);
			
			Table table = createPropertiesTable(experimentResults,
					propCombination);
			reportPage.addReportElement(table);
			
//			List<Table> tempEvolutionTables = createTempEvolutionTables(experimentResults, propCombination);
//			reportPage.addReportElements(tempEvolutionTables);
			
			pages.add(reportPage);
			
			System.out.println("Properties: "+sheetName+" Created");
			
		}
		
		setBlock(new ReportBlock("Grouped by properties", pages));

	}

	private List<Table> createTempEvolutionTables(ExperimentManager experimentResults, List<String> properties) {
		
		long timeLimit = experimentResults.getTimeLimit();
		
		if(timeLimit == -1){
			timeLimit = experimentResults.getMaxTimeLimit();
		}
		
		if(timeLimit == -1){
			System.out.println("WTF!");
		}
		
		timeLimit = 60000;
		
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
		
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker
				.getGroupBy(properties.toArray(new String[0])));

		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		Table devTempEvolutionTable = ttCreator.createTitleTable(attTable);
		
		FeasStatisticCalc feasStatisticCalc = new FeasStatisticCalc(); 
		TempEvolutionApproxAttributeTableCreator feasTableCreator = new TempEvolutionApproxAttributeTableCreator(
				timeLimit, 
				feasStatisticCalc);
		
		feasTableCreator.setInstanceGroupMaker(InstanceGroupMaker
				.getGroupBy(properties.toArray(new String[0])));

		configTableCreator(experimentResults, feasTableCreator);
		AttributedTable feasAttTable = feasTableCreator.buildTable();

		AttributedTableTitleTableCreator feasttCreator = new AttributedTableTitleTableCreator();
		feasttCreator.setColsAttributes("statistic");
		feasttCreator.setRowsAttributes("instancegroup", "method");
		
		Table feasTempEvolutionTable = feasttCreator.createTitleTable(feasAttTable);
		
		return Arrays.asList(devTempEvolutionTable, feasTempEvolutionTable);
		
	}

	private Table createPropertiesTable(
			ExperimentManager experimentResults, List<String> properties) {

		CommonApproxAttributeTableCreator tableCreator = new CommonApproxAttributeTableCreator();
		
		tableCreator.setInstanceGroupMaker(InstanceGroupMaker
				.getGroupBy(properties.toArray(new String[0])));
		
		configTableCreator(experimentResults, tableCreator);
		AttributedTable attTable = tableCreator.buildTable();

		AttributedTableTitleTableCreator ttCreator = new AttributedTableTitleTableCreator();
		ttCreator.setColsAttributes("statistic");
		ttCreator.setRowsAttributes("instancegroup", "method");

		Table tt = ttCreator.createTitleTable(attTable);
		return tt;
	}

	private void filterProperties(List<InstanceDescription> instances,
			List<String> propNames) {

		// To avoid the combinatorial explosion, we need to filter some of this
		// combinations.
		// - In some cases, a property has the same value for all instances, and
		// hence, has no
		// no sense to include it in combinations.
		// - In other cases, there a properties completly correlated between
		// them. In this case,
		// one property is enough to represent all other properties.

		for (Iterator<String> it = propNames.iterator(); it.hasNext();) {

			String propName = it.next();

			Object value = instances.get(0).getProperties().get(propName);
			boolean sameValue = true;
			for (int i = 1; i < instances.size(); i++) {
				if (instances.get(i).getProperties().get(propName) == null ||
						!instances.get(i).getProperties().get(propName)
						.equals(value)) {
					sameValue = false;
					break;
				}
			}

			if (sameValue) {
				it.remove();
			}

		}

	}


}
