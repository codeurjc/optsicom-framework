package es.optsicom.lib.web.model.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.web.model.ReportTable;
import es.optsicom.lib.web.model.ReportTitle;

public class ReportTableBuilder {
	private static final Log LOG = LogFactory.getLog(ReportTableBuilder.class);

	private ReportTitleBuilder reportTitleBuilder = new ReportTitleBuilder();

	public ReportTable build(Table table) {
		if (table == null) {
			new ReportTable();
		}
		List<List<Double>> cellValues = extractCellValues(table);
		List<ReportTitle> rowTitles = new ArrayList<ReportTitle>();
		for (Title title : table.getRowTitles()) {
			rowTitles.add(reportTitleBuilder.build(title));
		}
		List<ReportTitle> columnTitles = new ArrayList<ReportTitle>();
		for (Title title : table.getColumnTitles()) {
			columnTitles.add(reportTitleBuilder.build(title));
		}
		return new ReportTable(cellValues, rowTitles, columnTitles);
	}

	private List<List<Double>> extractCellValues(Table table) {
		List<List<Double>> cellValues = new ArrayList<List<Double>>();
		for (int i = 0; i < table.getNumRows(); i++) {
			List<Double> cellRowValues = new ArrayList<Double>();
			for (int j = 0; j < table.getNumColumns(); j++) {
				Double auxValue;
				try {
					Object cellValue = table.getCell(i, j).getValue();
					auxValue = (Double) cellValue;
				} catch (Exception e) {
					auxValue = 0.0;
					LOG.debug("ReportTable -> [" + i + "][" + j + "] is not double");
				}
				cellRowValues.add(auxValue);
			}
			cellValues.add(cellRowValues);
		}
		return cellValues;
	}

}
