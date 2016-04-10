package es.optsicom.lib.approx.experiment;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import es.optsicom.lib.Method;
import es.optsicom.lib.analyzer.ReportConf;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator.ExperimentMethodConf;
import es.optsicom.lib.approx.ApproxMethod;
import es.optsicom.lib.expresults.DBExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.ExperimentRepositoryFactory;
import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.db.DBManagerProvider;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.expresults.saver.ExperimentSaver;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.instancefile.InstancesRepository;

public class FastExperimentExecutor {

	private static final String OS_NAME = "os.name";
	private static final String OS_ARCH = "os.arch";
	private static final String JAVA_HOME = "java.home";
	private static final String JAVA_VM_NAME = "java.vm.name";
	private static final String JAVA_VERSION = "java.version";
	private static final String JAVA_VM_VENDOR = "java.vm.vendor";

	private final ApproxExpConf approxExpConf;
	private final List<ExperimentMethodConf> expMethodConfs = new ArrayList<ExperimentMethodConf>();
	private ReportConf reportConf;

	private long experimentId;
	private boolean localExecution = true;
	private DBManager dbManager;

	private String name = "";
	private InstancesRepository repository;

	public FastExperimentExecutor(ApproxExpConf approxExpConf) {
		this.approxExpConf = approxExpConf;
	}

	public FastExperimentExecutor setLocalExperiment(boolean localExecution) {
		this.localExecution = localExecution;
		return this;
	}

	public FastExperimentExecutor addExperimentMethod(String experimentName, String methodName) {
		expMethodConfs.add(new ExperimentMethodConf(experimentName, methodName));
		return this;
	}

	public FastExperimentExecutor addOptimumExperimentMethod() {
		addExperimentMethod("predefined", "optimum");
		return this;
	}

	public FastExperimentExecutor setReportConf(ReportConf reportConf) {
		this.reportConf = reportConf;
		return this;
	}

	public FastExperimentExecutor setDBManager(DBManager dbManager) {
		this.dbManager = dbManager;
		return this;
	}

	public FastExperimentExecutor setName(String name) {
		this.name = name;
		return this;
	}

	public long execExperiment() {

		if (dbManager == null) {
			try {
				dbManager = DBManagerProvider.getDBManager();
			} catch (SQLException e) {
				throw new RuntimeException("Error when opening database", e);
			}
		}

		ExperimentRepositoryFactory expRepoFactory = null;
		expRepoFactory = new DBExperimentRepositoryManagerFactory(dbManager);

		if (this.approxExpConf.getName() == null) {
			this.approxExpConf.setName(this.approxExpConf.getClass().getSimpleName());
		}

		experimentId = execExperiment(approxExpConf, expRepoFactory);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String docname = formatter.format(new Date()) + " " + name + " - id " + experimentId;
		FusionerReportCreator reportCreator = new FusionerReportCreator(approxExpConf.getProblem().getName(), docname,
				dbManager);

		reportCreator.addExperimentMethod(experimentId);
		reportCreator.addExperimentMethods(expMethodConfs);
		if (this.reportConf != null) {
			reportCreator.setReportConf(reportConf);
		}

		// reportCreator.createReportAndShow();

		return experimentId;
	}

	public long execExperiment(ApproxExpConf approxExpConf, ExperimentRepositoryFactory expRepoFactory) {

		if (approxExpConf.getResearchName() == null) {
			throw new RuntimeException("Researcher name must be filled");
		}

		if (approxExpConf.getComputerName() == null) {
			throw new RuntimeException("ComputerDescription name must be filled");
		}
		
		createInstancesRepository(approxExpConf);

		ApproxMethodExperiment<?, ?> exp = createApproxMethodExperiment(approxExpConf);

		ExperimentSaver expSaver = persistExperiment(approxExpConf, expRepoFactory);

		try {

			exp.executeExperiment(expSaver, localExecution);

		} catch (Exception e) {
			
			throw new RuntimeException("Exception executing experiment", e);
		
		} finally {
			
			repository.close();
		}		

		return expSaver.getExperimentId();
	}
	
	

	private ExperimentSaver persistExperiment(ApproxExpConf approxExpConf, ExperimentRepositoryFactory expRepoFactory) {

		ExperimentSaver expSaver = expRepoFactory.createExperimentRepositorySaver().createExperimentSaver();

		Experiment expdb = persistExperimentInfo(approxExpConf, expRepoFactory);

		expSaver.setExperiment(expdb);

		for (Method<?, ?> method : approxExpConf.getMethods()) {
			MethodDescription methodDesc;
			try {
				methodDesc = expSaver.findMethodDescription(method.getProperties().toString());
			} catch (NoResultException e) {
				methodDesc = method.createMethodDescription();
				expSaver.persist(methodDesc);
			}
			expSaver.getExperiment().getMethods().add(methodDesc);
		}

		List<InstanceFile> ifs = approxExpConf.getInstanceFiles();
		for (InstanceFile instance : ifs) {
			InstanceDescription instanceDesc;
			try {
				instanceDesc = expSaver.findInstanceDescription(instance.getProperties().toString());
			} catch (NoResultException e) {
				instanceDesc = instance.createInstanceDescription();
				expSaver.persist(instanceDesc);
			}
			expSaver.getExperiment().getInstances().add(instanceDesc);
		}

		createExperimentInfo(approxExpConf, expSaver.getExperiment());

		expSaver.persistExperiment();
		expSaver.commitTx();
		return expSaver;
	}

	private Experiment persistExperimentInfo(ApproxExpConf approxExpConf, ExperimentRepositoryFactory expRepoFactory) {

		ExperimentRepositoryManager expRepoManager = expRepoFactory.createExperimentRepositoryManager();

		expRepoManager.beginTx();

		java.util.Properties properties = System.getProperties();

		Map<String, String> computerProps = new HashMap<String, String>();
		computerProps.put(JAVA_VM_VENDOR, (String) properties.get(JAVA_VM_VENDOR));
		computerProps.put(JAVA_VERSION, (String) properties.get(JAVA_VERSION));
		computerProps.put(JAVA_VM_NAME, (String) properties.get(JAVA_VM_NAME));
		computerProps.put(JAVA_HOME, (String) properties.get(JAVA_HOME));
		computerProps.put(OS_ARCH, (String) properties.get(OS_ARCH));
		computerProps.put(OS_NAME, (String) properties.get(OS_NAME));
		computerProps.put("name", approxExpConf.getComputerName());
		DBProperties props = new DBProperties(computerProps);

		ComputerDescription computer = null;
		List<ComputerDescription> computerDescriptions = expRepoManager
				.findComputerDescriptionByName(approxExpConf.getComputerName());
		for (ComputerDescription cd : computerDescriptions) {
			if (props.equals(cd.getProperties())) {
				computer = cd;
			}
		}
		if (computer == null) {
			computer = new ComputerDescription(props);
			expRepoManager.persist(computer);
		}

		Researcher researcher;
		try {
			researcher = expRepoManager.findResearcherByName(approxExpConf.getResearchName());
		} catch (NoResultException e) {
			researcher = new Researcher(approxExpConf.getResearchName());
			expRepoManager.persist(researcher);
		}
		expRepoManager.commitTx();

		Experiment expdb = new Experiment(approxExpConf.getName(), researcher, new Date(), computer);
		return expdb;
	}
	
	private void createInstancesRepository(ApproxExpConf approxExpConf) {
		
		String instancesFilesDir = approxExpConf.getInstancesFilesDir();
		String useCase = approxExpConf.getUseCase();
		
		if (instancesFilesDir == null) {
			repository = approxExpConf.getProblem().getInstancesRepository(useCase);
		} else {
			repository = approxExpConf.getProblem().getInstancesRepository(new File(instancesFilesDir), useCase);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ApproxMethodExperiment createApproxMethodExperiment(ApproxExpConf approxExpConf) {

		List<ApproxMethod> approxMethods = new ArrayList<ApproxMethod>();

		for (Method m : approxExpConf.getMethods()) {
			approxMethods.add((ApproxMethod) m);
		}

		ApproxMethodExperiment exp = new ApproxMethodExperiment(approxExpConf, approxMethods,
				approxExpConf.getNumExecs());

		approxExpConf.calculateInstanceFilesAndTimes(repository);

		exp.setInstanceTimeLimits(approxExpConf.getInstanceTimeLimits());
		exp.setInstanceFiles(approxExpConf.getInstanceFiles());
		exp.setUseCase(approxExpConf.getUseCase());
		exp.setDescription(approxExpConf.getDescription());
		exp.setProblem(approxExpConf.getProblem());
		exp.setMethodNames(approxExpConf.getMethodNames());

		exp.setRecordEvolution(approxExpConf.isRecordEvolution());
		long timeLimitInMillis = approxExpConf.getTimeLimitInMillis();
		if (timeLimitInMillis != -1) {
			exp.setTimeLimit(timeLimitInMillis);
		}
		return exp;
	}

	private void createExperimentInfo(ApproxExpConf approxExpConf, Experiment experiment) {

		experiment.setDescription(approxExpConf.getDescription());
		experiment.setNumExecs(approxExpConf.getNumExecs());
		experiment.setProblemBestMode(approxExpConf.getProblem().getMode());
		experiment.setProblemName(approxExpConf.getProblem().getName());
		experiment.setUseCase(approxExpConf.getUseCase());
		experiment.setRecordEvolution(approxExpConf.isRecordEvolution());
		experiment.setTimeLimit(approxExpConf.getTimeLimitInMillis());

	}

	public long getExperimentId() {
		return experimentId;
	}

	public FastExperimentExecutor addExperimentMethod(long id) {
		expMethodConfs.add(new ExperimentMethodConf(id));
		return this;
	}
}
