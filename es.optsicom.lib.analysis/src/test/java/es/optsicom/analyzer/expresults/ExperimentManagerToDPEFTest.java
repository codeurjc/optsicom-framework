package es.optsicom.analyzer.expresults;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Collections;
import java.util.List;

import es.optsicom.lib.expresults.db.DerbyDBManager;
import es.optsicom.lib.expresults.dpef.DPEFToExperimentManager;
import es.optsicom.lib.expresults.dpef.ExperimentManagerToDPEF;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class ExperimentManagerToDPEFTest {

	public static void main(String[] args) throws SQLException, IOException {

		ExperimentRepositoryManager expRepoManager = createExperimentRepositoryManager();

		ExperimentManager expManager = expRepoManager.findExperimentManagerById(11351);

		File resultFile = new File("export.txt");

		ExperimentManagerToDPEF em2d = new ExperimentManagerToDPEF();
		em2d.createDPEF(expManager, resultFile);

		System.out.println("EXPORTED!!!!!");

		DPEFToExperimentManager d2em = new DPEFToExperimentManager();
		ExperimentManager em = d2em.createExperimentManager(resultFile);

		showExperimentWithExecs(em);

	}

	private static ExperimentRepositoryManager createExperimentRepositoryManager() throws SQLException {

		// DerbyDBManager dbManager = new DerbyDBManager(new
		// File("derby_exp_repo"));
		DerbyDBManager dbManager = new DerbyDBManager(new File(
				"/home/mica/Data/Investigacion/Papers/Paper Solver Enteros/ws/maven.1330086817925/es.optsicom.problem.ip/derby_exp_repo"));

		ExperimentRepositoryManagerFactory daoFactory = new ExperimentRepositoryManagerFactory(dbManager);

		ExperimentRepositoryManager expRepoManager = daoFactory.createExperimentsManager();

		return expRepoManager;
	}

	public static void showExperimentWithExecs(ExperimentManager expManager) throws IOException {

		showExperiment(expManager);

		for (InstanceDescription instance : expManager.getInstances()) {
			// System.out.println("Instance: " + instance.getName());
			for (MethodDescription method : expManager.getMethods()) {

				// System.out.println(" Method:"
				// + expManager.getExperimentMethodName(method));

				// System.out.println("Executions:");
				for (Execution execution : expManager.getExecutions(instance, method)) {
					showExecution(execution);
				}
			}
		}
	}

	private static void showExperiment(ExperimentManager expManager) throws IOException {

		Experiment exp = expManager.getExperiment();

		System.out.println("------------------------------");

		System.out.println("Experiment");
		System.out.println("   Name:" + exp.getName());
		System.out.println("   NumExecs:" + exp.getNumExecs());
		System.out.println("   Date:" + DateFormat.getDateInstance().format(exp.getDate()));
		System.out.println("   TimeLimit:" + exp.getTimeLimit());
		System.out.println("   ProblemName:" + exp.getProblemName());
		System.out.println("Instances: ");
		for (InstanceDescription instance : expManager.getInstances()) {
			System.out.println("  " + instance.getName());
		}
		System.out.println("Methods");
		for (MethodDescription method : expManager.getMethods()) {
			System.out.println("  " + expManager.getExperimentMethodName(method) + " (" + method.getName() + "): "
					+ method.getProperties());
		}

	}

	private static void showExecution(Execution execution) {

		System.out.println("     Execution:");
		List<Event> events = execution.getEvents();
		Collections.sort(events);
		for (Event event : events) {
			System.out.println(
					"       " + event.getTimestamp() + " > " + ": " + event.getName() + ":" + event.getValue());
		}

	}

}
