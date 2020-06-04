package es.optsicom.lib.expresults.manager;

import java.util.ArrayList;
import java.util.Collections;
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
 * Creates a new ExperimentManager as fusion of several ExperimentManagers. If
 * two or more ExperimentManagers contain the same Method, only the method of
 * the first ExperimentManager will be used. The instances of the merged
 * ExperimentManager will be the common instances between all ExperimentManagers
 * being merged.
 *
 * @author mica
 *
 */
public class MergedExperimentManager implements ExperimentManager {

	private ExperimentRepositoryManager manager;

	private List<ExperimentManager> expManagers;

	private List<MethodDescription> mergedMethods;
	private List<InstanceDescription> mergedInstances;

	private Map<MethodDescription, ExperimentManager> methodsMap = new HashMap<MethodDescription, ExperimentManager>();

	private long timeLimit = -1;
	private long maxTimeLimit = -1;

	public MergedExperimentManager(ExperimentRepositoryManager manager, List<ExperimentManager> expManagers) {
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

		System.out.println("Merging methods in experiments:");

		this.mergedMethods = new ArrayList<MethodDescription>();
		int numExperimentManager = 0;
		for (ExperimentManager manager : expManagers) {

			System.out.println("  Experiment manager: " + numExperimentManager);

			for (MethodDescription method : manager.getMethods()) {

				System.out.println("     Method: " + method.getName());

				if (!methodsMap.containsKey(method)) {
					methodsMap.put(method, manager);
					mergedMethods.add(method);
				} else {
					System.out.println("Colision detected. The same method is present in several experiments.\n"
							+ "Use filters to avoid colisions");
				}
			}

			numExperimentManager++;
		}
	}

	@Override
	public List<InstanceDescription> getInstances() {
		return Collections.unmodifiableList(mergedInstances);
	}

	private void mergeInstances() {

		this.mergedInstances = new ArrayList<InstanceDescription>(expManagers.get(0).getInstances());

		for (int i = 1; i < expManagers.size(); i++) {
			this.mergedInstances.retainAll(expManagers.get(i).getInstances());
		}

	}

	@Override
	public String getExperimentMethodName(MethodDescription method) {
		return methodsMap.get(method).getExperimentMethodName(method);
	}

	@Override
	public List<ExecutionManager> getExecutionManagers(InstanceDescription instance, MethodDescription method) {

		if (mergedInstances.contains(instance) && mergedMethods.contains(method)) {
			return methodsMap.get(method).getExecutionManagers(instance, method);
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
		// TODO: Unexpected behavior: passing a null value as
		// ExperimentRepositoryManager
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
		return methodsMap.get(method).getExecutions(instance, method);
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
		return null;
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
