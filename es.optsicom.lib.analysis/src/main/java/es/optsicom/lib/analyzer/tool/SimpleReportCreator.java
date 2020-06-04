package es.optsicom.lib.analyzer.tool;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import es.optsicom.lib.analyzer.DefaultReportConf;
import es.optsicom.lib.analyzer.ReportConf;
import es.optsicom.lib.expresults.DBExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.ExperimentRepositoryFactory;
import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;

public class SimpleReportCreator {

	private ReportConf reportConf = new DefaultReportConf();

	private DBManager dbManager;

	private String reportName;

	private int experimentId;

	public SimpleReportCreator(String problemName, String reportName, DBManager dbManager, int experimentId) {
		this.dbManager = dbManager;
		this.reportName = reportName;
		this.experimentId = experimentId;
	}

	public SimpleReportCreator setReportConf(ReportConf reportConf) {
		this.reportConf = reportConf;
		return this;
	}

	public void createReport() {

		ExperimentRepositoryFactory expRepoFactory = null;
		expRepoFactory = new DBExperimentRepositoryManagerFactory(dbManager);

		ExperimentRepositoryManager expRepoManager = expRepoFactory.createExperimentRepositoryManager();

		ExperimentManager fullExpManager = expRepoManager.findExperimentManagerById(experimentId);

		// ExperimentManager filteredExpManager =
		// fullExpManager.createFilteredExperimentManager(null, new
		// ExplicitElementsFilter("{name="+expMethodConf.methodName+"}"));

		this.reportConf = new DefaultReportConf();
		reportConf.buildReport(fullExpManager);

		File resultsDir = new File("reports");
		resultsDir.mkdirs();

		File excelFile = new File(resultsDir, reportName + ".xlsx");

		try {
			reportConf.exportToExcelFile(excelFile);
			Desktop.getDesktop().open(excelFile);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
