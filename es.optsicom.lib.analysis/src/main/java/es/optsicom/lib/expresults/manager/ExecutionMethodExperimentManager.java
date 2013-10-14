package es.optsicom.lib.expresults.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

public class ExecutionMethodExperimentManager implements ExperimentManager {

	private Experiment experiment;
	private ExperimentRepositoryManager manager;
	private int exec;
	
	List<MethodDescription> methods = new ArrayList<MethodDescription>();
	Map<MethodDescription, MethodDescription> originalMethodIndexes = new HashMap<MethodDescription, MethodDescription>();
	Map<MethodDescription, MethodDescription> reverseMethodIndexes = new HashMap<MethodDescription, MethodDescription>();
	
	public ExecutionMethodExperimentManager(Experiment experiment, ExperimentRepositoryManager manager, int exec) {
		this.experiment = experiment;
		this.manager = manager;
		this.exec = exec;
		
		extractMethods();
	}

	private void extractMethods() {
		
		for(MethodDescription md : this.experiment.getMethods()) {
			MethodDescription execMethodDescription = new MethodDescription(md.getName() + " " + exec);
			execMethodDescription.getProperties().getMap().putAll(md.getProperties().getMap());
			execMethodDescription.getProperties().setName(md.getName());
			this.methods.add(execMethodDescription);
			this.originalMethodIndexes.put(md, execMethodDescription);
			this.reverseMethodIndexes.put(execMethodDescription, md);
		}
		
	}

	@Override
	public List<MethodDescription> getMethods() {
		return this.methods;
	}

	@Override
	public List<InstanceDescription> getInstances() {
		return experiment.getInstances();
	}

	@Override
	public String getExperimentMethodName(MethodDescription method) {
		return method.getProperties().getName();
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
		
		List<Execution> executions = manager.findExecutions(getExperiment(), instance, method);
		List<ExecutionManager> execManagers = new ArrayList<ExecutionManager>();
		execManagers.add(new ExecutionManager(executions.get(this.exec), manager));
		
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
		return manager.getTimeLimit(getExperiment(),subsetMethods,subsetInstances);
	}

	@Override
	public long getMaxTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return manager.getMaxTimeLimit(getExperiment(),subsetMethods,subsetInstances);
	}

	@Override
	public List<Execution> getExecutions(InstanceDescription instance,
			MethodDescription method) {
		return Arrays.asList(manager.findExecutions(getExperiment(), instance, this.reverseMethodIndexes.get(method)).get(exec));
	}

	@Override
	public String getName() {
		return getExperiment().getName();
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
