package es.optsicom.lib.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.export.ExcelReportManager;
import es.optsicom.lib.analyzer.report.export.FileType;
import es.optsicom.lib.analyzer.report.export.OpenCalcReportManager;
import es.optsicom.lib.expresults.manager.ExperimentManager;

public abstract class ReportConf {

	private List<BlockBuilder> blockBuilders = new ArrayList<BlockBuilder>();
	private Report report;
	
	protected void addBlockBuilder(BlockBuilder block) {
		blockBuilders.add(block);
	}
	
	public Report buildReport(ExperimentManager experimentResults) {
		
		if(experimentResults.getInstances().isEmpty()){
			throw new RuntimeException("The experimentManager has no instances");
		}
		
		report = new Report();
		for(BlockBuilder blockBuilder : blockBuilders) {
			blockBuilder.buildPages(experimentResults);
			ReportBlock block = blockBuilder.getBlock();
			report.addReportBlock(block);
		}
		
		return report;
	}
	
	public void exportToExcelFile(File excelFile) throws IOException {

		System.out.println("Tables created");

		ExcelReportManager erm = new ExcelReportManager();
		erm.generateExcelFile(report, excelFile);
		
	}
	
	public Report getReport() {
		return report;
	}
	
}
