package es.optsicom.lib.web.model.reportrest;

import java.util.List;

public class ReportRest {

	private ReportRestConfiguration configuration;
	private List<ReportRestBlock> blocksTable;

	public ReportRest() {
	}

	public ReportRest(ReportRestConfiguration configuration, List<ReportRestBlock> blocksTable) {
		this.configuration = configuration;
		this.blocksTable = blocksTable;
	}

	public ReportRestConfiguration getConfiguration() {
		return configuration;
	}

	public List<ReportRestBlock> getBlocksTable() {
		return blocksTable;
	}

}
