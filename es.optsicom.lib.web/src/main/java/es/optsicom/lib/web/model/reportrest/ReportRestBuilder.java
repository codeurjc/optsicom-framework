package es.optsicom.lib.web.model.reportrest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.CellFormat;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;

public class ReportRestBuilder {

	public ReportRest buildReportRest(ReportRestConfiguration configuration, Report report) {
		return new ReportRest(configuration, buildTables(report));
	}

	public List<ReportRestBlock> buildTables(Report report) {

		List<ReportRestBlock> blocks = new ArrayList<>();

		for (ReportBlock reportBlock : report.getReportBlocks()) {
			String blockName = reportBlock.getName();
			List<ReportRestTable> tables = new ArrayList<>();

			for (ReportPage reportpage : reportBlock.getReportPages()) {
				String tableTitle = reportpage.getName();

				for (ReportElement reportElement : reportpage.getReportElements()) {
					Table table = (Table) reportElement;

					// Transform cell values
					List<List<ReportRestCell>> cellValues = new ArrayList<List<ReportRestCell>>();
					for (int i = 0; i < table.getNumRows(); i++) {
						List<ReportRestCell> cellRowValues = new ArrayList<ReportRestCell>();

						for (int j = 0; j < table.getNumColumns(); j++) {
							Object object;
							CellFormat cellFormat;
							Color color;

							try {
								object = table.getCell(i, j).getValue();
							} catch (Exception e) {
								object = null;
							}

							try {
								cellFormat = table.getCell(i, j).getFormat();
							} catch (Exception e) {
								cellFormat = null;
							}

							try {
								color = table.getCell(i, j).getColor();
							} catch (Exception e) {
								color = null;
							}

							cellRowValues.add(new ReportRestCell(object, cellFormat, color));
						}

						cellValues.add(cellRowValues);
					}

					// Transform row titles
					List<ReportRestTitle> rowTitles = new ArrayList<>();
					for (Title title : table.getRowTitles()) {
						rowTitles.add(buildTitle(title));
					}

					// Transform column titles
					List<ReportRestTitle> columnTitles = new ArrayList<>();
					for (Title title : table.getColumnTitles()) {
						columnTitles.add(buildTitle(title));
					}

					tables.add(new ReportRestTable(tableTitle, cellValues, rowTitles, columnTitles));
				}
			}

			blocks.add(new ReportRestBlock(blockName, tables));
		}

		return blocks;
	}

	private static ReportRestTitle buildTitle(Title title) {
		if (title == null) {
			return new ReportRestTitle();
		}

		String infoTitle = (title.getTitle() == null) ? "" : title.getTitle();
		List<ReportRestAttribute> attributes = new ArrayList<ReportRestAttribute>();

		for (Attribute attribute : title.getAttributes()) {
			attributes.add(buildAttribute(attribute));
		}

		return new ReportRestTitle(attributes, infoTitle);
	}

	private static ReportRestAttribute buildAttribute(Attribute attribute) {
		if (attribute == null) {
			return new ReportRestAttribute();
		}

		String name = (attribute.getName() == null) ? "" : attribute.getName();
		String value = (attribute.getTitle() == null) ? "null" : attribute.getTitle();

		return new ReportRestAttribute(name, value);
	}
}
