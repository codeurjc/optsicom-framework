package es.optsicom.lib.analyzer.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.analyzer.DefaultReportConf;
import es.optsicom.lib.analyzer.ReportConf;
import es.optsicom.lib.analyzer.TempEvolutionReportConf;
import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator.ExperimentMethodConf;
import es.optsicom.lib.expresults.db.DerbyDBManager;
import es.optsicom.lib.expresults.manager.ExecutionManager;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class ExpAnalyzerHelper {

	private static final String DEFAULT_DERBY_DIR = "derby_exp_repo";

	public static void showDefaultReport(
			String experimentName, String problemName, FiltersAndAliases fa) {
		showDefaultReport(DEFAULT_DERBY_DIR, experimentName, problemName, fa);
	}

	public static void showDefaultReport(String derbyDir, long id) {
		showDefaultReport(derbyDir, id, null);
	}

	public static void showTempEvolutionReport(String derbyDir, String experimentName, String problemName,  FiltersAndAliases fa, long timeLimit, int numSteps) {
		showDefaultReport(derbyDir, experimentName, problemName, new TempEvolutionReportConf(timeLimit, numSteps), fa);
	}

	public static void showDefaultReportWithBestValues(String derbyDir,String problemName,
			long id, FiltersAndAliases fa) {

		try {
			DerbyDBManager dbManager = new DerbyDBManager(new File(derbyDir));

			FusionerReportCreator reportCreator = new FusionerReportCreator(
					problemName, "Report", dbManager);

			reportCreator.addExperimentMethod(id);
			reportCreator.addExperimentMethods(Arrays
					.asList(new ExperimentMethodConf("predefined",
							"best_values")));
			reportCreator.setMethodFilter(fa);
			reportCreator.createReportAndShow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void showDefaultReport(String derbyDir, long id,
			FiltersAndAliases fa) {
		try {
			ExperimentRepositoryManager expRepoManager = createExperimentRepositoryManager(derbyDir);

			ExperimentManager expManager = expRepoManager
					.findExperimentManagerById(id);

			for (MethodDescription method : expManager.getMethods()) {
				System.out.println(method);
			}

			ElementFilter methodFilter = fa != null ? fa.getMethodFilter()
					: null;
			expManager = expManager.createFilteredExperimentManager(null,
					methodFilter);

			createReportAndOpenExcel(expManager.getName(),
					new DefaultReportConf(), expManager);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showDefaultReport(String derbyDir,
			String experimentName, String problemName) {
		showDefaultReport(derbyDir, experimentName, problemName,
				new DefaultReportConf(), null);
	}

	public static void showDefaultReport(String derbyDir,
			String experimentName, String problemName, FiltersAndAliases fa) {
		showDefaultReport(derbyDir, experimentName, problemName,
				new DefaultReportConf(), fa);
	}

	public static void showDefaultReport(String derbyDir,
			String experimentName, String problemName, ReportConf reportConf,
			FiltersAndAliases fa) {

		try {
			ExperimentRepositoryManager expRepoManager = createExperimentRepositoryManager(derbyDir);

			ExperimentManager expManager = expRepoManager
					.findExperimentManagerByName(experimentName, problemName);

			ElementFilter methodFilter = fa != null ? fa.getMethodFilter()
					: null;
			expManager = expManager.createFilteredExperimentManager(null,
					methodFilter);

			// showExperimentContents(expManager);

			createReportAndOpenExcel(experimentName, reportConf, expManager);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createReportAndOpenExcel(String experimentName,
			ReportConf reportConf, ExperimentManager expManager)
			throws IOException {

		System.out.println("ExperimentManager created");

		reportConf.buildReport(expManager);

		System.out.println("Report built");

		File resultsDir = new File("reports");
		resultsDir.mkdirs();

		File excelFile = new File(resultsDir, experimentName + ".xlsx");

		System.out.println("Start exporting to excel");

		reportConf.exportToExcelFile(excelFile);

		System.out.println("Finish exporting to excel");

		Desktop.getDesktop().open(excelFile);
	}

	private static ExperimentRepositoryManager createExperimentRepositoryManager(
			String derbyDir) throws SQLException {

		DerbyDBManager dbManager = new DerbyDBManager(new File(derbyDir));

		ExperimentRepositoryManagerFactory daoFactory = new ExperimentRepositoryManagerFactory(
				dbManager);

		ExperimentRepositoryManager expRepoManager = daoFactory
				.createExperimentsManager();

		showExperiments(expRepoManager);

		return expRepoManager;
	}

	private static void showExperiments(
			ExperimentRepositoryManager expRepoManager) {

		List<Experiment> experiments = expRepoManager.findExperiments();
		for (Experiment exp : experiments) {
			System.out.println("Experiment");
			System.out.println("   Id:" + exp.getId());
			System.out.println("   Name:" + exp.getName());
			System.out.println("   NumExecs:" + exp.getNumExecs());
			System.out.println("   Date:"
					+ DateFormat.getDateInstance().format(exp.getDate()));
			System.out.println("   TimeLimit:" + exp.getTimeLimit());
			System.out.println("   ProblemName:" + exp.getProblemName());
			// System.out.println("Instances: ");
			// for(InstanceDescription instance : exp.getInstances()){
			// System.out.println("  "+instance.getName());
			// }
			// System.out.println("Methods");
			// for(MethodDescription method : exp.getMethods()){
			// System.out.println("   "+method.getName()+": "+method.getProperties());
			// }
		}
	}

	private static void showExperimentContents(ExperimentManager expManager) {

		System.out.println("Experiment Name: " + expManager.getName());
		System.out.println("Instances: ");
		for (InstanceDescription instance : expManager.getInstances()) {
			System.out.println("  " + instance.getName());
		}
		System.out.println("Methods");
		for (MethodDescription method : expManager.getMethods()) {
			System.out.println("  "
					+ expManager.getExperimentMethodName(method) + " ("
					+ method.getName() + "): " + method.getProperties());
		}
		System.out.println("Executions:");

		for (InstanceDescription instance : expManager.getInstances()) {
			for (MethodDescription method : expManager.getMethods()) {
				System.out.println("  Method:"
						+ expManager.getExperimentMethodName(method)
						+ " Instance:" + instance.getName());
				List<ExecutionManager> executions = expManager
						.getExecutionManagers(instance, method);
				for (ExecutionManager execution : executions) {
					System.out.println("     Execution:");
					for (Event event : execution.getEvents()) {
						System.out.println("       " + event.getTimestamp()
								+ " > " + /* event.getNumber()+ */": "
								+ event.getName() + ":" + event.getValue());
					}
				}
			}
		}

	}

}
