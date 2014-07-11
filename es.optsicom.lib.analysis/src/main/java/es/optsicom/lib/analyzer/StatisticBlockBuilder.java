package es.optsicom.lib.analyzer;

import java.util.ArrayList;
import java.util.List;

import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.multiple.friedmanTest.FriedmanTest;
import javanpst.tests.oneSample.wilcoxonTest.WilcoxonTest;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.Cell;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class StatisticBlockBuilder extends BlockBuilder {

	@Override
	public void buildPages(ExperimentManager experimentResults) {
		ReportPage page = new ReportPage("Statistical tests");

		if (experimentResults.getInstances().size() < 10) {
			setBlock(new ReportBlock(page));
			return;
		}

		List<MethodDescription> methods = experimentResults.getMethods();
		List<InstanceDescription> instances = experimentResults.getInstances();

		double values[][] = new double[methods.size()][instances.size()];

		int method = 0;
		for (MethodDescription methodDescription : methods) {
			int instance = 0;
			for (InstanceDescription instanceDescription : instances) {
				List<Execution> executions = experimentResults.getExecutions(instanceDescription, methodDescription);
				for (Execution execution : executions) {
					Event event = execution.getLastEvent(Event.OBJ_VALUE_EVENT);
					values[method][instance++] = (double) event.getValue();
				}
			}
			method++;
		}

		List<Title> colsTitles = new ArrayList<Title>();
		List<Title> rowsTitles = new ArrayList<Title>();
		colsTitles.add(new Title("Wilcoxon Test"));
		colsTitles.add(new Title("Friedman Test"));

		List<Double> pvalues = new ArrayList<Double>();
		// Wilcoxon test
		for (int i = 0; i < methods.size() - 1; i++) {
			for (int j = i + 1; j < methods.size(); j++) {
				DataTable data = new DataTable(createDataTable(values[i], values[j]));
				WilcoxonTest test = new WilcoxonTest(data);
				test.doTest();
				rowsTitles.add(new Title(experimentResults.getExperimentMethodName(methods.get(i)) + "\n"
						+ experimentResults.getExperimentMethodName(methods.get(j))));
				pvalues.add(test.getExactDoublePValue());
			}
		}

		Table tt = new Table(rowsTitles, colsTitles);
		int numRow = 0;
		for (Double pvalue : pvalues) {
			tt.setCell(numRow++, 0, new Cell(pvalue));
		}

		if (values.length > 2) {
			// Friedman test
			FriedmanTest friedmanTest = new FriedmanTest(new DataTable(createDataTable(values)));
			friedmanTest.doTest();
			tt.setCell(0, 1, new Cell(friedmanTest.getPValue()));
		}

		page.addReportElement(tt);

		setBlock(new ReportBlock(page));

	}

	private double[][] createDataTable(double[]... ds) {
		int length = ds[0].length;
		double result[][] = new double[length][];

		for (int i = 0; i < length; i++) {
			result[i] = new double[ds.length];
			for (int j = 0; j < ds.length; j++) {
				result[i][j] = ds[j][i];
			}
		}

		return result;
	}

}
