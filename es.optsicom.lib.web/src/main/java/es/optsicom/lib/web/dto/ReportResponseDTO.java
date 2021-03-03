package es.optsicom.lib.web.dto;

import java.util.List;

public class ReportResponseDTO {

	private ReportConfResponseDTO configuration;
	private List<ReportBlockResponseDTO> blocksTable;

	public ReportResponseDTO() {
	}

	public ReportResponseDTO(ReportConfResponseDTO configuration, List<ReportBlockResponseDTO> blocksTable) {
		this.configuration = configuration;
		this.blocksTable = blocksTable;
	}

	public ReportConfResponseDTO getConfiguration() {
		return configuration;
	}

	public List<ReportBlockResponseDTO> getBlocksTable() {
		return blocksTable;
	}

}
