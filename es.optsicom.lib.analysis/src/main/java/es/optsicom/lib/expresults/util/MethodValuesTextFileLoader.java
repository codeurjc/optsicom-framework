package es.optsicom.lib.expresults.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.persistence.NoResultException;

import es.optsicom.lib.analyzer.tablecreator.AnalysisException;
import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.DoubleEvent;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.expresults.model.StringEvent;
import es.optsicom.lib.util.BestMode;

public class MethodValuesTextFileLoader {

	private static final String PREDEFINED = "predefined";
	private DBManager dbManager;
	private String researcherName;
	private String experimentName;

	private String problemName;
	private BestMode bestMode;
	private List<InstanceDescription> instanceDescriptions;

	private Map<String, Long> times;

	private String fileName;
	private String methodName;

	public void setDbManager(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	public void setResearcherName(String researcherName) {
		this.researcherName = researcherName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setBestMode(BestMode bestMode) {
		this.bestMode = bestMode;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public void setInstanceDescriptions(List<InstanceDescription> instanceDescriptions) {
		this.instanceDescriptions = instanceDescriptions;
	}

	// public ExperimentManager loadExperimentManager() {
	//
	// System.out.println("Starting to read " + fileName);
	//
	// Researcher r = new Researcher(this.researcherName);
	// ComputerDescription c = new ComputerDescription("Synthetic");
	//
	// Map<String, Double> values = loadValuesFromFile();
	//
	// this.experimentName = PREDEFINED;
	//
	// Experiment experiment = new Experiment(this.experimentName, r,
	// new Date(), c);
	// experiment.setTimeLimit(-1);
	// experiment.setProblemBestMode(this.bestMode);
	// experiment.setProblemName(this.problemName);
	// experiment.setMaxTimeLimit(-1);
	//
	// MethodDescription method = new MethodDescription(new DBProperties(
	// this.methodName));
	//
	// Map<String, InstanceDescription> idByName = new HashMap<String,
	// InstanceDescription>();
	//
	// for (InstanceDescription instance : instanceDescriptions) {
	// idByName.put(instance.getName(), instance);
	// }
	//
	// List<Execution> executions = new ArrayList<Execution>();
	//
	// List<InstanceDescription> instances = new
	// ArrayList<InstanceDescription>();
	// Map<MethodDescription, String> experimentMethodNames = new
	// HashMap<MethodDescription, String>();
	// experimentMethodNames.put(method, method.getProperties().getName());
	//
	// for (String instanceName : values.keySet()) {
	//
	// InstanceDescription dbInstance = idByName.get(instanceName);
	//
	// instances.add(dbInstance);
	//
	// Execution exec = new Execution(experiment, dbInstance, method, -1,
	// 0);
	//
	// StringEvent nameEvent = new StringEvent(exec, -1,
	// Event.EXPERIMENT_METHOD_NAME, method.getProperties()
	// .getName());
	//
	// long timestamp = times.containsKey(instanceName) ? times
	// .get(instanceName) : -1;
	//
	// DoubleEvent dbEvent = new DoubleEvent(exec, timestamp,
	// Event.OBJ_VALUE_EVENT, values.get(instanceName));
	//
	// List<Event> events = new ArrayList<Event>();
	// events.add(nameEvent);
	// events.add(dbEvent);
	//
	// exec.setEvents(events);
	//
	// executions.add(exec);
	// }
	//
	// experiment.setInstances(instances);
	// experiment.setMethods(Arrays.asList(method));
	//
	// return new MemoryExperimentManager(experiment, executions,
	// experimentMethodNames);
	// }

	public void importToDB() {

		System.out.println("Starting to read " + fileName);

		try {

			ExperimentRepositoryManagerFactory daoFactory = new ExperimentRepositoryManagerFactory(dbManager);

			ExperimentRepositoryManager dao = daoFactory.createExperimentsManager();

			dao.beginTx();
			Researcher r = loadResearcher(dao);
			ComputerDescription c = loadComputer(dao);
			dao.commitTx();
			dao.close();

			Map<String, Double> values = loadValuesFromFile();

			insertValues(daoFactory, values, r, new Date(), c);

			dbManager.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Researcher loadResearcher(ExperimentRepositoryManager dao) {
		Researcher r = null;
		try {
			r = dao.findResearcherByName(researcherName);
		} catch (NoResultException e) {
			r = new Researcher(this.researcherName);
			dao.persist(r);
		}
		return r;
	}

	private ComputerDescription loadComputer(ExperimentRepositoryManager dao) {
		List<ComputerDescription> cList = dao.findComputerDescriptionByName("Synthetic");
		ComputerDescription c;
		if (cList.isEmpty()) {
			c = new ComputerDescription("Synthetic");
			dao.persist(c);
		} else {
			c = cList.get(0);
		}
		return c;
	}

	private Map<String, Double> loadValuesFromFile() {
		Map<String, Double> values = new HashMap<String, Double>();
		times = new HashMap<String, Long>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName)))) {
			String line;
			while ((line = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line);

				try {
					String instanceName = st.nextToken();
					String bestKnownValue = st.nextToken();

					System.out.println("Instance: " + instanceName + " value: " + bestKnownValue);

					values.put(instanceName, Double.parseDouble(bestKnownValue));

					if (st.hasMoreTokens()) {
						// we read timestamp info
						String timestamp = st.nextToken();
						double seconds = Double.parseDouble(timestamp);
						times.put(instanceName, (long) (seconds * 1000));
					}

				} catch (NoSuchElementException e) {
					System.err.println("Exception while processing input file lines");
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			throw new AnalysisException("File not found: " + this.fileName, e);
		} catch (IOException e) {
			throw new AnalysisException(e);
		}

		return values;
	}

	private void insertValues(ExperimentRepositoryManagerFactory daoFactory, Map<String, Double> values,
			Researcher researcher, Date date, ComputerDescription computer)
			throws FileNotFoundException, IOException, ClassNotFoundException {

		ExperimentRepositoryManager repoManager = daoFactory.createExperimentsManager();
		repoManager.beginTx();

		if (this.experimentName == null) {
			this.experimentName = PREDEFINED;
		}

		Experiment experiment = loadOrCreateExperiment(researcher, date, computer, repoManager);

		System.out.println("Experiment ID = " + experiment.getId());

		es.optsicom.lib.expresults.model.MethodDescription dbMethod = loadOrCreateMethod(repoManager);

		repoManager.commitTx();

		Map<String, InstanceDescription> instancesByName = new HashMap<String, InstanceDescription>();

		loadOrCreateInstances(repoManager, instancesByName);

		List<es.optsicom.lib.expresults.model.InstanceDescription> instances = new ArrayList<es.optsicom.lib.expresults.model.InstanceDescription>();
		for (String instanceName : values.keySet()) {

			repoManager.beginTx();

			es.optsicom.lib.expresults.model.InstanceDescription dbInstance = instancesByName.get(instanceName);

			if (dbInstance == null) {
				repoManager.commitTx();
				System.out.println("Instance in file " + instanceName + " not found as Instance in problem");
				continue;
			}

			instances.add(dbInstance);

			es.optsicom.lib.expresults.model.Execution dbExecution = new es.optsicom.lib.expresults.model.Execution(
					experiment, dbInstance, dbMethod, -1, 0);
			repoManager.persist(dbExecution);

			List<es.optsicom.lib.expresults.model.Event> dbEvents = new ArrayList<es.optsicom.lib.expresults.model.Event>();

			StringEvent nameEvent = new StringEvent(dbExecution, -1, Event.EXPERIMENT_METHOD_NAME,
					dbMethod.getProperties().getName());
			long timestamp = times.containsKey(instanceName) ? times.get(instanceName) : -1;
			DoubleEvent dbEvent = new DoubleEvent(dbExecution, timestamp, Event.OBJ_VALUE_EVENT,
					values.get(instanceName));
			dbEvents.add(nameEvent);
			dbEvents.add(dbEvent);

			repoManager.persist(dbEvent);

			dbExecution.setEvents(dbEvents);

			repoManager.commitTx();

			System.out.println("Saved events for instance " + dbInstance.getName());
		}

		experiment.getInstances().addAll(instances);
		experiment.setMethods(Arrays.asList(dbMethod));

		repoManager.beginTx();
		repoManager.persist(experiment);
		repoManager.commitTx();

		repoManager.close();

	}

	private void loadOrCreateInstances(ExperimentRepositoryManager repoManager,
			Map<String, InstanceDescription> idByName) {
		repoManager.beginTx();
		for (InstanceDescription id : instanceDescriptions) {

			es.optsicom.lib.expresults.model.InstanceDescription dbInstance = null;

			try {
				dbInstance = repoManager.findInstanceDescription(id.getProperties().getPropsAString());
			} catch (NoResultException e) {
				dbInstance = id;
				repoManager.persist(id);
			}

			idByName.put(dbInstance.getName(), dbInstance);

			System.out.println("Load instance: " + dbInstance.getName());
		}
		repoManager.commitTx();
	}

	private es.optsicom.lib.expresults.model.MethodDescription loadOrCreateMethod(
			ExperimentRepositoryManager repoManager) {

		es.optsicom.lib.expresults.model.MethodDescription dbMethod = null;

		System.out.println("Loading method " + this.methodName);

		try {

			dbMethod = repoManager.findMethodDescription(new DBProperties(this.methodName).getPropsAString());

			System.out.println("Method found in database.");

		} catch (NoResultException e) {

			System.out.println("No method found in database. Creating a fake one.");

			dbMethod = new es.optsicom.lib.expresults.model.MethodDescription(new DBProperties(this.methodName));
			repoManager.persist(dbMethod);

		}
		return dbMethod;
	}

	private Experiment loadOrCreateExperiment(Researcher researcher, Date date, ComputerDescription computer,
			ExperimentRepositoryManager repoManager) {

		Experiment experiment = null;
		try {
			experiment = repoManager.findExperimentByName(this.experimentName, this.problemName);

			System.out.println("Experiment found in database.");

		} catch (NoResultException e) {

			System.out.println("Experiment not found in database. Creating a fake one.");

			experiment = new Experiment(this.experimentName, researcher, date, computer);
			experiment.setTimeLimit(-1);
			experiment.setProblemBestMode(this.bestMode);
			experiment.setProblemName(this.problemName);
			experiment.setMaxTimeLimit(-1);

			repoManager.persist(experiment);

		}
		return experiment;
	}

}
