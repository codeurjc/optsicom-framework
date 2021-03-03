package es.optsicom.lib.web.service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.CellFormat;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.web.dto.ReportAttributeResponseDTO;
import es.optsicom.lib.web.dto.ReportBlockResponseDTO;
import es.optsicom.lib.web.dto.ReportCellResponseDTO;
import es.optsicom.lib.web.dto.ReportConfResponseDTO;
import es.optsicom.lib.web.dto.ReportResponseDTO;
import es.optsicom.lib.web.dto.ReportTableResponseDTO;
import es.optsicom.lib.web.dto.ReportTitleResponseDTO;

@Service
public class ReportService {

	public ReportResponseDTO createReportRest(ReportConfResponseDTO configuration, Report report) {
		return new ReportResponseDTO(configuration, buildTables(report));
	}

	private List<ReportBlockResponseDTO> buildTables(Report report) {

		List<ReportBlockResponseDTO> blocks = new ArrayList<>();

		for (ReportBlock reportBlock : report.getReportBlocks()) {
			String blockName = reportBlock.getName();
			List<ReportTableResponseDTO> tables = new ArrayList<>();

			for (ReportPage reportpage : reportBlock.getReportPages()) {
				String tableTitle = reportpage.getName();

				for (ReportElement reportElement : reportpage.getReportElements()) {
					Table table = (Table) reportElement;

					// Transform cell values
					List<List<ReportCellResponseDTO>> cellValues = new ArrayList<List<ReportCellResponseDTO>>();
					for (int i = 0; i < table.getNumRows(); i++) {
						List<ReportCellResponseDTO> cellRowValues = new ArrayList<ReportCellResponseDTO>();

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

							cellRowValues.add(new ReportCellResponseDTO(object, cellFormat, color));
						}

						cellValues.add(cellRowValues);
					}

					// Transform row titles
					List<ReportTitleResponseDTO> rowTitles = new ArrayList<>();
					for (Title title : table.getRowTitles()) {
						rowTitles.add(buildTitle(title));
					}

					// Transform column titles
					List<ReportTitleResponseDTO> columnTitles = new ArrayList<>();
					for (Title title : table.getColumnTitles()) {
						columnTitles.add(buildTitle(title));
					}

					tables.add(new ReportTableResponseDTO(tableTitle, cellValues, rowTitles, columnTitles));
				}
			}

			blocks.add(new ReportBlockResponseDTO(blockName, tables));
		}

		return blocks;
	}

	private ReportTitleResponseDTO buildTitle(Title title) {
		if (title == null) {
			return new ReportTitleResponseDTO();
		}

		String infoTitle = (title.getTitle() == null) ? "" : title.getTitle();
		List<ReportAttributeResponseDTO> attributes = new ArrayList<ReportAttributeResponseDTO>();

		for (Attribute attribute : title.getAttributes()) {
			attributes.add(buildAttribute(attribute));
		}

		return new ReportTitleResponseDTO(attributes, infoTitle);
	}

	private ReportAttributeResponseDTO buildAttribute(Attribute attribute) {
		if (attribute == null) {
			return new ReportAttributeResponseDTO();
		}

		String name = (attribute.getName() == null) ? "" : attribute.getName();
		String value = (attribute.getTitle() == null) ? "null" : attribute.getTitle();

		return new ReportAttributeResponseDTO(name, value);
	}
}
