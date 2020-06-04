package es.optsicom.lib.expresults.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

/**
 * Creates a new ExperimentManager as fusion of several ExperimentManagers. This
 * class is useful when an experiment is run in different machines and ends up
 * stored as multiple entries in the experiment table. This class considers all
 * the experiments specified as if it was a single experiment. In this sense, it
 * is considered that different experiments can contain the same method and
 * instances are sum up.
 *
 * @author Patxi Gortázar
 *
 */
public class MultipleExperimentManager implements ExperimentManager {

	private ExperimentRepositoryManager manager;

	private List<ExperimentManager> expManagers;

	private List<MethodDescription> mergedMethods;
	private List<InstanceDescription> mergedInstances;

	private Map<InstanceDescription, ExperimentManager> instancesMap = new HashMap<InstanceDescription, ExperimentManager>();

	private long timeLimit = -1;
	private long maxTimeLimit = -1;

	public MultipleExperimentManager(ExperimentRepositoryManager manager, List<ExperimentManager> expManagers) {
		this.manager = manager;
		this.expManagers = expManagers;

		mergeMethods();
		mergeInstances();
		calculateTimeLimits();
	}

	private void calculateTimeLimits() {
		this.timeLimit = getTimeLimit(mergedMethods, mergedInstances);
		this.maxTimeLimit = getMaxTimeLimit(mergedMethods, mergedInstances);
	}

	@Override
	public List<MethodDescription> getMethods() {
		return mergedMethods;
	}

	private void mergeMethods() {
		this.mergedMethods = new ArrayList<MethodDescription>(expManagers.get(0).getMethods());
		for (int i = 1; i < expManagers.size(); i++) {
			List<MethodDescription> methods = new ArrayList<MethodDescription>(this.mergedMethods);
			methods.addAll(expManagers.get(i).getMethods());

			this.mergedMethods.retainAll(expManagers.get(i).getMethods());

			methods.removeAll(mergedMethods);
			methods.removeAll(expManagers.get(i).getMethods());
			System.out.println("The following methods were not included: " + methods);
		}
	}

	@Override
	public List<InstanceDescription> getInstances() {
		return mergedInstances;
	}

	private void mergeInstances() {

		this.mergedInstances = new ArrayList<InstanceDescription>();
		for (ExperimentManager em : expManagers) {
			for (InstanceDescription instance : em.getInstances()) {
				if (!instancesMap.containsKey(instance)) {
					instancesMap.put(instance, em);
					this.mergedInstances.add(instance);
				}
			}
		}

	}

	@Override
	public String getExperimentMethodName(MethodDescription method) {
		return instancesMap.values().iterator().next().getExperimentMethodName(method);
	}

	@Override
	public List<ExecutionManager> getExecutionManagers(InstanceDescription instance, MethodDescription method) {

		if (mergedInstances.contains(instance) && mergedMethods.contains(method)) {
			return instancesMap.get(instance).getExecutionManagers(instance, method);
		} else {
			return null;
		}
	}

	@Override
	public BestMode getProblemBestMode() {
		return expManagers.get(0).getProblemBestMode();
	}

	@Override
	public long getTimeLimit() {
		return timeLimit;
	}

	@Override
	public long getMaxTimeLimit() {
		return maxTimeLimit;
	}

	@Override
	public ExperimentManager createFilteredExperimentManager(ElementFilter instanceFilter, ElementFilter methodFilter) {
		return new FilteredExperimentManager(manager, this, instanceFilter, methodFilter);
	}

	@Override
	public ExperimentManager createFilteredExperimentManager(ElementFilter instanceFilter, String... methodsByExpName) {
		return new FilteredExperimentManager(null, this, instanceFilter, methodsByExpName);
	}

	@Override
	public long getTimeLimit(List<MethodDescription> subsetMethods, List<InstanceDescription> subsetInstances) {

		// TODO The timelimit calculation is broken
		return 1000;
	}

	@Override
	public long getMaxTimeLimit(List<MethodDescription> subsetMethods, List<InstanceDescription> subsetInstances) {

		// TODO The timelimit calculation is broken
		return 1000;
	}

	@Override
	public List<Execution> getExecutions(InstanceDescription instance, MethodDescription method) {
		return instancesMap.get(instance).getExecutions(instance, method);
	}

	@Override
	public String getName() {
		StringBuilder sb = new StringBuilder("MergedExperiment from [");
		for (ExperimentManager exp : this.expManagers) {
			sb.append(exp.getName()).append(" ");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Experiment getExperiment() {

		Experiment firstExperiment = expManagers.get(0).getExperiment();

		Experiment experiment = new Experiment(this.getName(), firstExperiment.getResearcher(), new Date(0),
				firstExperiment.getComputer());
		experiment.setProblemName(firstExperiment.getProblemName());
		experiment.setProblemBestMode(firstExperiment.getProblemBestMode());
		experiment.setMethods(getMethods());
		experiment.setInstances(getInstances());
		experiment.setDescription("Multiple experiments...");
		experiment.setMaxTimeLimit(-1);
		experiment.setTimeLimit(firstExperiment.getTimeLimit());
		experiment.setNumExecs(firstExperiment.getNumExecs());
		experiment.setRecordEvolution(firstExperiment.isRecordEvolution());

		return experiment;
	}

	@Override
	public List<Execution> createExecutions() {

		List<Execution> execs = new ArrayList<Execution>();

		for (MethodDescription method : mergedMethods) {
			for (InstanceDescription instance : mergedInstances) {
				execs.addAll(getExecutions(instance, method));
			}
		}

		return execs;
	}

	@Override
	public Map<MethodDescription, String> createExperimentMethodNames() {

		Map<MethodDescription, String> expMethodNames = new HashMap<MethodDescription, String>();

		for (MethodDescription method : getMethods()) {
			expMethodNames.put(method, getExperimentMethodName(method));
		}

		return expMethodNames;
	}

}
