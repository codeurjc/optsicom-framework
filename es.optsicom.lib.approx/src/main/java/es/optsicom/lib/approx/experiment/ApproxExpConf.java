package es.optsicom.lib.approx.experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.Method;
import es.optsicom.lib.Problem;
import es.optsicom.lib.instancefile.FileInstancesRepository;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.instancefile.InstancesGroup;
import es.optsicom.lib.instancefile.InstancesRepository;

@SuppressWarnings("rawtypes")
public abstract class ApproxExpConf {

	public static class ExpInstancesGroup {

		private List<InstanceConf> instanceConfs = new ArrayList<InstanceConf>();
		private long timeLimitInMillis = -1;

		public ExpInstancesGroup(InstanceConf instanceConf) {
			this.instanceConfs.add(instanceConf);
		}

		public ExpInstancesGroup() {

		}

		private List<InstanceConf> getInstanceConfs() {
			return instanceConfs;
		}

		public ExpInstancesGroup addInstances(String instanceSetId) {
			InstanceConf conf = new InstanceConf();
			conf.instanceSetId = instanceSetId;
			instanceConfs.add(conf);
			return this;
		}

		public ExpInstancesGroup addInstance(String instanceSetId, int numInstance) {
			InstanceConf conf = new InstanceConf();
			conf.instanceSetId = instanceSetId;
			conf.numInstance = numInstance;
			instanceConfs.add(conf);
			return this;
		}

		public ExpInstancesGroup addInstanceByName(String instanceName) {
			InstanceConf conf = new InstanceConf();
			conf.instanceName = instanceName;
			instanceConfs.add(conf);
			return this;
		}

		public ExpInstancesGroup addInstances(String instanceSetId, int fromInstance, int toInstance) {
			InstanceConf conf = new InstanceConf();
			conf.instanceSetId = instanceSetId;
			conf.fromNumInstance = fromInstance;
			conf.toNumInstance = toInstance;
			instanceConfs.add(conf);
			return this;
		}

		public void setTimeLimitInMillis(long millis) {
			this.timeLimitInMillis = millis;
		}

		public void setTimeLimitInSeconds(double seconds) {
			this.timeLimitInMillis = (long) (seconds * 1000);
		}

		public void setTimeLimitInMinutes(double minutes) {
			this.timeLimitInMillis = (long) (minutes * 60 * 1000);
		}

		public void setTimeLimitInHours(double hours) {
			this.timeLimitInMillis = (long) (hours * 60 * 60 * 1000);
		}

		public long getTimeLimitInMillis() {
			return timeLimitInMillis;
		}
	}

	private static class InstanceConf {
		public String instanceSetId;
		public int numInstance = -1;
		public int fromNumInstance = -1;
		public int toNumInstance = -1;
		public String instanceName;
	}

	private String description;

	private List<Method> methods = new ArrayList<Method>();
	private List<String> methodNames = new ArrayList<String>();

	private List<ExpInstancesGroup> expInstanceGroups = new ArrayList<ExpInstancesGroup>();
	private String useCase = FileInstancesRepository.DEFAULT_USE_CASE;
	private long timeLimitInMillis = -1;
	private boolean recordEvolution = true;
	private Problem problem;
	private int numExecs = 1;
	private String instancesFilesDir = null;

	private List<InstanceFile> instanceFiles;
	private List<Long> instanceTimeLimits;

	private String computerName;

	private String researcherName;

	private String name;

	private List<InstancesGroup> instancesGroups = new ArrayList<InstancesGroup>();

	public void addMethod(String name, Method method) {
		methods.add(method);
		if (methodNames.contains(name)) {
			throw new RuntimeException("Method names have to be unique");
		}
		methodNames.add(name);
	}

	public void addMethod(Method method) {
		methods.add(method);
		methodNames.add(null);
	}

	public void addMethods(Method... methods) {
		this.methods.addAll(Arrays.asList(methods));
		for (int i = 0; i < methods.length; i++) {
			this.methods.add(null);
		}
	}

	public void addMethods(List<Method> methods) {
		this.methods.addAll(methods);
		for (int i = 0; i < methods.size(); i++) {
			this.methods.add(null);
		}
	}

	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	// Instances
	public ExpInstancesGroup createInstancesGroup() {
		ExpInstancesGroup ig = new ExpInstancesGroup();
		expInstanceGroups.add(ig);
		return ig;
	}

	// Instances
	public ExpInstancesGroup addInstances(String instanceSetId) {
		InstanceConf conf = new InstanceConf();
		conf.instanceSetId = instanceSetId;
		ExpInstancesGroup ig = new ExpInstancesGroup(conf);
		expInstanceGroups.add(ig);
		return ig;
	}

	public ExpInstancesGroup addInstance(String instanceSetId, int numInstance) {
		InstanceConf conf = new InstanceConf();
		conf.instanceSetId = instanceSetId;
		conf.numInstance = numInstance;
		ExpInstancesGroup ig = new ExpInstancesGroup(conf);
		expInstanceGroups.add(ig);
		return ig;
	}

	public ExpInstancesGroup addInstanceByName(String instanceName) {
		InstanceConf conf = new InstanceConf();
		conf.instanceName = instanceName;
		ExpInstancesGroup ig = new ExpInstancesGroup(conf);
		expInstanceGroups.add(ig);
		return ig;
	}

	public ExpInstancesGroup addInstances(String instanceSetId, int fromInstance, int toInstance) {
		InstanceConf conf = new InstanceConf();
		conf.instanceSetId = instanceSetId;
		conf.fromNumInstance = fromInstance;
		conf.toNumInstance = toInstance;
		ExpInstancesGroup ig = new ExpInstancesGroup(conf);
		expInstanceGroups.add(ig);
		return ig;
	}

	public void addInstancesGroup(InstancesGroup instancesGroup) {
		instancesGroups.add(instancesGroup);
	}

	public void setTimeLimitInMillis(long millis) {
		this.timeLimitInMillis = millis;
	}

	public void setTimeLimitInSeconds(double seconds) {
		this.timeLimitInMillis = (long) (seconds * 1000);
	}

	public void setTimeLimitInMinutes(double minutes) {
		this.timeLimitInMillis = (long) (minutes * 60 * 1000);
	}

	public void setTimeLimitInHours(double hours) {
		this.timeLimitInMillis = (long) (hours * 60 * 60 * 1000);
	}

	public long getTimeLimitInMillis() {
		return timeLimitInMillis;
	}

	public void setRecordEvolution(boolean recordEvolution) {
		this.recordEvolution = recordEvolution;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setInstancesFilesDir(String instancesFilesDir) {
		this.instancesFilesDir = instancesFilesDir;
	}

	public void setNumExecs(int numExecs) {
		this.numExecs = numExecs;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public void setResearcherName(String researcherName) {
		this.researcherName = researcherName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void calculateInstanceFilesAndTimes(InstancesRepository repository) {

		if (!instancesGroups.isEmpty()) {

			instanceFiles = new ArrayList<InstanceFile>();
			for (InstancesGroup instGroup : instancesGroups) {
				instGroup.setInstancesRepository(repository);
				instanceFiles.addAll(instGroup.getInstanceFiles());
			}

		} else if (!this.getInstanceGroups().isEmpty()) {

			this.calculateInstanceFilesAndTimesWithDifferentTimes(repository);

		} else {
			// if there aren't any instance configuration, all instances are
			// used
			this.instanceFiles = repository.getAllInstanceFiles();
		}
	}

	private void calculateInstanceFilesAndTimesWithDifferentTimes(InstancesRepository repository) {

		boolean timeSet = false;

		List<Long> instanceTimeLimitsAux = new ArrayList<Long>();
		instanceFiles = new ArrayList<InstanceFile>();

		for (ExpInstancesGroup group : expInstanceGroups) {

			for (InstanceConf conf : group.getInstanceConfs()) {

				if (conf.instanceName != null) {

					instanceFiles.add(repository.getInstanceFileByName(conf.instanceName));

				} else {

					List<InstanceFile> setInstanceFiles = repository.getInstanceFiles(conf.instanceSetId);

					if (conf.fromNumInstance != -1) {

						for (InstanceFile instanceFile : setInstanceFiles.subList(conf.fromNumInstance,
								conf.toNumInstance)) {

							instanceFiles.add(instanceFile);
						}

					} else if (conf.numInstance != -1) {
						instanceFiles.add(setInstanceFiles.get(conf.numInstance));
					} else {
						instanceFiles.addAll(setInstanceFiles);
					}

				}

				instanceTimeLimitsAux.add(group.getTimeLimitInMillis());
				timeSet |= group.getTimeLimitInMillis() != -1;

			}
		}

		if (timeSet) {
			this.instanceTimeLimits = instanceTimeLimitsAux;
		}

	}

	public boolean isMultipleExecutions() {
		return this.numExecs > 1;
	}

	public String getResearchName() {
		return researcherName;
	}

	public String getComputerName() {
		return computerName;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public String getInstancesFilesDir() {
		return instancesFilesDir;
	}

	public String getUseCase() {
		return useCase;
	}

	public int getNumExecs() {
		return numExecs;
	}

	public List<ExpInstancesGroup> getInstanceGroups() {
		return expInstanceGroups;
	}

	public List<InstanceFile> getInstanceFiles() {
		return instanceFiles;
	}

	public List<Long> getInstanceTimeLimits() {
		return this.instanceTimeLimits;
	}

	public List<String> getMethodNames() {
		return methodNames;
	}

	public boolean isRecordEvolution() {
		return recordEvolution;
	}

}
