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

public class MemoryExperimentManager implements ExperimentManager {

	private Experiment experiment;
	private Map<InstanceDescription, Map<MethodDescription, List<Execution>>> data = new HashMap<InstanceDescription, Map<MethodDescription, List<Execution>>>();
	private Map<MethodDescription, String> experimentMethodNames;
	
	public MemoryExperimentManager(
			Experiment experiment,
			Map<InstanceDescription, Map<MethodDescription, List<Execution>>> data, Map<MethodDescription, String> experimentMethodNames) {
		this.experiment = experiment;
		this.data = data;
		this.experimentMethodNames = experimentMethodNames;
	}

	public MemoryExperimentManager(Experiment experiment,
			List<Execution> executions,
			Map<MethodDescription, String> experimentMethodNames) {
		
		this.data = createDataFromExecutions(executions);
		this.experiment = experiment;
		this.experimentMethodNames = experimentMethodNames;
	}

	private Map<InstanceDescription, Map<MethodDescription, List<Execution>>> createDataFromExecutions(List<Execution> executions) {
		
		Map<InstanceDescription, Map<MethodDescription, List<Execution>>> data = new HashMap<InstanceDescription, Map<MethodDescription,List<Execution>>>();
		
		for(Execution execution : executions){
			Map<MethodDescription, List<Execution>> instData = data.get(execution.getInstance());
			if(instData == null){
				instData = new HashMap<MethodDescription, List<Execution>>();
				data.put(execution.getInstance(), instData);
			}
			
			List<Execution> execs = instData.get(execution.getMethod());
			if(execs == null){
				execs = new ArrayList<Execution>();
				instData.put(execution.getMethod(), execs);
			}
			
			execs.add(execution);
		}
		
		return data;		
	}

	@Override
	public List<MethodDescription> getMethods() {
		return experiment.getMethods();
	}

	@Override
	public List<InstanceDescription> getInstances() {
		// return experiment.getInstances();
		// This hack allows the creation of AttributedTables if the experiment
		// is inconsistent with data. This is usually due to errors on
		// one or more instances.
		return new ArrayList<InstanceDescription>(data.keySet());
	}

	@Override
	public String getExperimentMethodName(MethodDescription method) {
		return experimentMethodNames.get(method);
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

//	@Override
//	public List<ExecutionManager> getExecutionManagers(
//			InstanceDescription instance, MethodDescription method) {
//
//		List<Execution> executions = manager.findExecutions(experiment,
//				instance, method);
//		List<ExecutionManager> execManagers = new ArrayList<ExecutionManager>();
//		for (Execution exec : executions) {
//			execManagers.add(new ExecutionManager(exec, manager));
//		}
//
//		return execManagers;
//	}

	@Override
	public ExperimentManager createFilteredExperimentManager(
			ElementFilter instanceFilter, ElementFilter methodFilter) {
		// TODO: Unexpected behavior: passing a null value as ExperimentRepositoryManager
		return new FilteredExperimentManager(null, this, instanceFilter,
				methodFilter);
	}
	
	@Override
	public ExperimentManager createFilteredExperimentManager(
			ElementFilter instanceFilter, String... methodsByExpName) {
		// TODO: Unexpected behavior: passing a null value as ExperimentRepositoryManager
		return new FilteredExperimentManager(null,this, instanceFilter, methodsByExpName);
	}

	@Override
	public long getTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return -1;
	}

	@Override
	public long getMaxTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return -1;
	}

	@Override
	public List<Execution> getExecutions(InstanceDescription instance,
			MethodDescription method) {
		return data.get(instance).get(method);
	}

	public String getName() {
		return this.experiment.getName();
	}

	public Experiment getExperiment() {
		return experiment;
	}

	@Override
	public List<ExecutionManager> getExecutionManagers(
			InstanceDescription instance, MethodDescription method) {
		
		List<Execution> executions = data.get(instance).get(method);
		List<ExecutionManager> execManagers = new ArrayList<ExecutionManager>();
		for(Execution ex : executions) {
			execManagers.add(new FakeExecutionManager(ex));			
		}
		
		return execManagers;
	}

	static class FakeExecutionManager extends ExecutionManager {

		private Execution execution;

		public FakeExecutionManager(Execution execution) {
			super(execution, null);
			this.execution = execution;
		}
		
		@Override
		public double countEvents(String eventName) {
			List<Event> events = execution.getEvents();
			int count = 0;
			for(Event ev : events) {
				if(ev.getName().equals(eventName)) {
					count++;
				}
			}
			return count;
		}

		@Override
		public List<Event> getEvents() {
			return execution.getEvents();
		}
		
		@Override
		public List<Event> getEvents(String eventName) {
			List<Event> events = new ArrayList<Event>();
			for(Event ev : execution.getEvents()) {
				if(ev.getName().equals(eventName)) {
					events.add(ev);
				}
			}
			return events;
		}
		
		@Override
		public Execution getExecution() {
			return this.execution;
		}
		
		@Override
		public Event getLastEvent(String eventName) {
			long timestamp = -1;
			Event lastEvent = null;
			for(Event ev : execution.getEvents()) {
				if(ev.getName().equals(eventName) && ev.getTimestamp() > timestamp) {
					timestamp = ev.getTimestamp();
					lastEvent = ev;
				}
			}
			return lastEvent;
		}
		
		@Override
		public Event getLastEvent(String eventName, long timelimit) {
			long timestamp = -1;
			Event lastEvent = null;
			for(Event ev : execution.getEvents()) {
				if(ev.getName().equals(eventName) && ev.getTimestamp() > timestamp && ev.getTimestamp() <= timelimit) {
					timestamp = ev.getTimestamp();
					lastEvent = ev;
				}
			}
			return lastEvent;
		}
	}
	
	@Override
	public List<Execution> createExecutions() {
		List<Execution> execs = new ArrayList<Execution>();
		for(Map.Entry<InstanceDescription,Map<MethodDescription, List<Execution>>> e1 : data.entrySet()){
			for(Map.Entry<MethodDescription, List<Execution>> e2 : e1.getValue().entrySet()){
				for(Execution exec : e2.getValue()){
					execs.add(exec);
				}
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
