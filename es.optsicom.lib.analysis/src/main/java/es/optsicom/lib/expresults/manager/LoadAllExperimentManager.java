package es.optsicom.lib.expresults.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

public class LoadAllExperimentManager implements ExperimentManager {

	private Experiment experiment;
	private ExperimentRepositoryManager manager;
	private Map<InstanceDescription, Map<MethodDescription, List<Execution>>> data = new HashMap<InstanceDescription, Map<MethodDescription, List<Execution>>>();

	public LoadAllExperimentManager(Experiment experiment, ExperimentRepositoryManager manager) {
		this.experiment = experiment;
		this.manager = manager;
		loadData();
	}

	private void loadData() {

		System.out.println("Loading experiment: " + experiment.getName() + " Problem: " + experiment.getProblemName());

		long startTime = System.currentTimeMillis();

		for (InstanceDescription instance : experiment.getInstances()) {

			Map<MethodDescription, List<Execution>> byMethod = new HashMap<MethodDescription, List<Execution>>();
			data.put(instance, byMethod);

			for (MethodDescription method : experiment.getMethods()) {

				List<Execution> execs = manager.findExecutions(experiment, instance, method);
				byMethod.put(method, execs);

				for (Execution exec : execs) {

					List<Event> events = exec.getEvents();

				}
			}

		}

		long loadTime = System.currentTimeMillis() - startTime;

		System.out.println("Finish. Load time " + loadTime + " millis");

	}

	@Override
	public List<MethodDescription> getMethods() {
		return experiment.getMethods();
	}

	@Override
	public List<InstanceDescription> getInstances() {
		return experiment.getInstances();
	}

	@Override
	public String getExperimentMethodName(MethodDescription method) {
		return manager.getExperimentMethodName(experiment, method);
	}

	@Override
	public BestMode getProblemBestMode() {
		return experiment.getProblemBestMode();
	}

	@Override
	public long getTimeLimit() {
		return experiment.getTimeLimit();
	}

	@Override
	public long getMaxTimeLimit() {
		return experiment.getMaxTimeLimit();
	}

	@Override
	public List<ExecutionManager> getExecutionManagers(InstanceDescription instance, MethodDescription method) {

		List<Execution> executions = manager.findExecutions(experiment, instance, method);
		List<ExecutionManager> execManagers = new ArrayList<ExecutionManager>();
		for (Execution exec : executions) {
			execManagers.add(new ExecutionManager(exec, manager));
		}

		return execManagers;
	}

	@Override
	public ExperimentManager createFilteredExperimentManager(ElementFilter instanceFilter, ElementFilter methodFilter) {
		return new FilteredExperimentManager(manager, this, instanceFilter, methodFilter);
	}

	@Override
	public ExperimentManager createFilteredExperimentManager(ElementFilter instanceFilter, String... methodsByExpName) {
		return new FilteredExperimentManager(manager, this, instanceFilter, methodsByExpName);
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public ExperimentRepositoryManager getExperimentsManager() {
		return manager;
	}

	@Override
	public long getTimeLimit(List<MethodDescription> subsetMethods, List<InstanceDescription> subsetInstances) {
		return manager.getTimeLimit(experiment, subsetMethods, subsetInstances);
	}

	@Override
	public long getMaxTimeLimit(List<MethodDescription> subsetMethods, List<InstanceDescription> subsetInstances) {
		return manager.getMaxTimeLimit(experiment, subsetMethods, subsetInstances);
	}

	@Override
	public List<Execution> getExecutions(InstanceDescription instance, MethodDescription method) {
		return data.get(instance).get(method);
	}

	@Override
	public String getName() {
		return this.experiment.getName();
	}

	@Override
	public List<Execution> createExecutions() {
		List<Execution> execs = new ArrayList<Execution>();
		for (Map.Entry<InstanceDescription, Map<MethodDescription, List<Execution>>> e1 : data.entrySet()) {
			for (Map.Entry<MethodDescription, List<Execution>> e2 : e1.getValue().entrySet()) {
				for (Execution exec : e2.getValue()) {
					execs.add(exec);
				}
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
