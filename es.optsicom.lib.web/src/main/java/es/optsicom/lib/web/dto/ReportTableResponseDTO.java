package es.optsicom.lib.web.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportTableResponseDTO {

	private String title;
	private List<List<ReportCellResponseDTO>> cellValues;
	private List<ReportTitleResponseDTO> rowTitles;
	private List<ReportTitleResponseDTO> columnTitles;

	public ReportTableResponseDTO() {
		this.title = "";
		this.cellValues = new ArrayList<>();
		this.rowTitles = new ArrayList<>();
		this.columnTitles = new ArrayList<>();
	}

	public ReportTableResponseDTO(String title, List<List<ReportCellResponseDTO>> cellValues, List<ReportTitleResponseDTO> rowTitles,
			List<ReportTitleResponseDTO> columnTitles) {
		super();
		this.title = title;
		this.cellValues = cellValues;
		this.rowTitles = rowTitles;
		this.columnTitles = columnTitles;
	}

	public String getTitle() {
		return title;
	}

	public List<List<ReportCellResponseDTO>> getCellValues() {
		return cellValues;
	}

	public List<ReportTitleResponseDTO> getRowTitles() {
		return rowTitles;
	}

	public List<ReportTitleResponseDTO> getColumnTitles() {
		return columnTitles;
	}

}
