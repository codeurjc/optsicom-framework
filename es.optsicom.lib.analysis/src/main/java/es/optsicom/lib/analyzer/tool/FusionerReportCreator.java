package es.optsicom.lib.analyzer.tool;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.analyzer.DefaultReportConf;
import es.optsicom.lib.analyzer.ReportConf;
import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.expresults.DBExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.ExperimentRepositoryFactory;
import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.manager.MergedExperimentManager;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class FusionerReportCreator {

	public static class ExperimentMethodConf {

		private String experimentName;
		private long experimentId;
		private final ArrayList<String> methodNames = new ArrayList<String>();

		public ExperimentMethodConf(String experimentName) {
			super();
			this.experimentName = experimentName;
		}

		public ExperimentMethodConf(long experimentId) {
			super();
			this.experimentId = experimentId;
		}

		public ExperimentMethodConf(String experimentName, String methodName) {
			super();
			this.experimentName = experimentName;
			this.methodNames.add(methodName);
		}

		public ExperimentMethodConf(long experimentId, String methodName) {
			super();
			this.experimentId = experimentId;
			this.methodNames.add(methodName);
		}

		public ExperimentMethodConf(long experimentId, String... methodNames) {
			this.experimentId = experimentId;
			this.methodNames.addAll(Arrays.asList(methodNames));
		}

		public String getExpNameOrId() {
			return experimentName != null ? experimentName : Long.toString(experimentId);
		}
	}

	private final List<ExperimentMethodConf> expMethodConfs = new ArrayList<FusionerReportCreator.ExperimentMethodConf>();
	private ReportConf reportConf;

	private final DBManager dbManager;

	private final String reportName;
	private final String problemName;
	private ElementFilter instancesFilter;

	public FusionerReportCreator(String problemName, String reportName, DBManager dbManager) {
		this.dbManager = dbManager;
		this.problemName = problemName;
		this.reportName = reportName;
	}

	public FusionerReportCreator addExperimentMethod(ExperimentMethodConf emc) {
		expMethodConfs.add(emc);
		return this;
	}

	public FusionerReportCreator addExperimentMethod(String experimentName, String methodName) {
		expMethodConfs.add(new ExperimentMethodConf(experimentName, methodName));
		return this;
	}

	public FusionerReportCreator addExperimentMethod(long experimentId, String methodName) {
		expMethodConfs.add(new ExperimentMethodConf(experimentId, methodName));
		return this;
	}

	public FusionerReportCreator addExperimentMethod(String experimentName) {
		expMethodConfs.add(new ExperimentMethodConf(experimentName));
		return this;
	}

	public FusionerReportCreator addExperimentMethod(long experimentId) {
		expMethodConfs.add(new ExperimentMethodConf(experimentId));
		return this;
	}

	public void addExperimentMethods(List<ExperimentMethodConf> expMethodConfs) {
		this.expMethodConfs.addAll(expMethodConfs);
	}

	public FusionerReportCreator addExperimentMethods(long experimentId, String... methodNames) {
		expMethodConfs.add(new ExperimentMethodConf(experimentId, methodNames));
		return this;
	}

	public FusionerReportCreator addOptimumExperimentMethod() {
		addExperimentMethod("predefined", "optimum");
		return this;
	}

	public FusionerReportCreator setReportConf(ReportConf reportConf) {
		this.reportConf = reportConf;
		return this;
	}

	public void createReportAndShow() {

		File excelFile = createReport();

		try {
			Desktop.getDesktop().open(excelFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File createReport() {
		ExperimentRepositoryFactory expRepoFactory = null;
		expRepoFactory = new DBExperimentRepositoryManagerFactory(dbManager);

		// long experimentId = this.approxExpConf.execExperiment(expRepoFactory);

		ExperimentRepositoryManager expRepoManager = expRepoFactory.createExperimentRepositoryManager();

		List<ExperimentManager> expManagers = new ArrayList<ExperimentManager>();
		// expManagers.add(expRepoManager.findExperimentManagerById(experimentId));
		for (ExperimentMethodConf expMethodConf : this.expMethodConfs) {

			ExperimentManager expManager;
			if (expMethodConf.experimentName != null) {
				expManager = expRepoManager.findExperimentManagerByName(expMethodConf.experimentName, problemName);
			} else {
				expManager = expRepoManager.findExperimentManagerById(expMethodConf.experimentId);
			}

			// showInfo(expManager);

			if (!expMethodConf.methodNames.isEmpty() || instancesFilter != null) {

				ExperimentManager filteredExpManager = expManager.createFilteredExperimentManager(instancesFilter,
						expMethodConf.methodNames.toArray(new String[0]));

				List<MethodDescription> methods = filteredExpManager.getMethods();
				if (methods.size() == 0) {
					System.out.println("WARNING: Filtering mehtods in experiment \"" + expMethodConf.getExpNameOrId()
							+ "\" gives no methods");
					System.out.println("  Methods before filtering: " + expManager.getMethods());
					System.out.println("  Methods after filtering: " + filteredExpManager.getMethods());
					System.out.println("  Method names looked for: " + expMethodConf.methodNames);
				}
			}

			expManagers.add(expManager);
		}

		MergedExperimentManager mergedExpManager = new MergedExperimentManager(expRepoManager, expManagers);
		if (this.reportConf == null) {
			this.reportConf = new DefaultReportConf();
		}
		reportConf.buildReport(mergedExpManager);

		File resultsDir = new File("reports");
		resultsDir.mkdirs();

		File excelFile = new File(resultsDir, reportName + ".xlsx");

		try {
			reportConf.exportToExcelFile(excelFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return excelFile;
	}

	private void showInfo(ExperimentManager expManager) {

		System.out.println("Methods");
		for (MethodDescription method : expManager.getMethods()) {
			System.out.println(method);
		}
		System.out.println("Instances");
		for (InstanceDescription instance : expManager.getInstances()) {
			System.out.println(instance);
		}

	}

	public void setInstacesFilter(ElementFilter instancesFilter) {
		this.instancesFilter = instancesFilter;
	}

}
