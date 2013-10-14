package es.optsicom.lib.expresults.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

public class SingleExperimentManager implements ExperimentManager {

	private Experiment experiment;
	private ExperimentRepositoryManager manager;
	
	public SingleExperimentManager(Experiment experiment, ExperimentRepositoryManager manager) {
		this.experiment = experiment;
		this.manager = manager;
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
		return manager.getExperimentMethodName(experiment,method);
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
	public List<ExecutionManager> getExecutionManagers(
			InstanceDescription instance, MethodDescription method) {
		
		List<Execution> executions = manager.findExecutions(experiment, instance, method);
		List<ExecutionManager> execManagers = new ArrayList<ExecutionManager>();
		for(Execution exec: executions){
			execManagers.add(new ExecutionManager(exec, manager));
		}
		
		return execManagers;
	}
	
	@Override
	public ExperimentManager createFilteredExperimentManager(
			ElementFilter instanceFilter, ElementFilter methodFilter) {
		return new FilteredExperimentManager(manager, this, instanceFilter, methodFilter);
	}
	
	@Override
	public ExperimentManager createFilteredExperimentManager(
			ElementFilter instanceFilter, String... methodsByExpName) {
		return new FilteredExperimentManager(null,this, instanceFilter, methodsByExpName);
	}
	
	public Experiment getExperiment(){
		return experiment;
	}
	
	public ExperimentRepositoryManager getExperimentsManager(){
		return manager;
	}

	@Override
	public long getTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return manager.getTimeLimit(experiment,subsetMethods,subsetInstances);
	}

	@Override
	public long getMaxTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return manager.getMaxTimeLimit(experiment,subsetMethods,subsetInstances);
	}

	@Override
	public List<Execution> getExecutions(InstanceDescription instance,
			MethodDescription method) {
		return manager.findExecutions(experiment, instance, method);
	}

	@Override
	public String getName() {
		return this.experiment.getName();
	}
	
	@Override
	public List<Execution> createExecutions() {
		
		List<Execution> execs = new ArrayList<Execution>();
		
		for(MethodDescription method :  getMethods()){
			for(InstanceDescription instance: getInstances()){
				execs.addAll(getExecutions(instance, method));
			}
		}
		
		return execs;
	}
	
	@Override
	public Map<MethodDescription, String> createExperimentMethodNames() {
		
		Map<MethodDescription, String> expMethodNames =  new HashMap<MethodDescription, String>();
		
		for(MethodDescription method :  getMethods()){
			expMethodNames.put(method, getExperimentMethodName(method));
		}
		
		return expMethodNames;
	}
}
