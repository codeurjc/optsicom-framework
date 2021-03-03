package es.optsicom.lib.web.dto;

import java.util.List;

public class ReportBlockResponseDTO {

	private String name;
	private List<ReportTableResponseDTO> tables;

	public ReportBlockResponseDTO() {
	}

	public ReportBlockResponseDTO(String name, List<ReportTableResponseDTO> tables) {
		this.name = name;
		this.tables = tables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReportTableResponseDTO> getTables() {
		return tables;
	}

	public void setTables(List<ReportTableResponseDTO> tables) {
		this.tables = tables;
	}

}
