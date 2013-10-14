package es.optsicom.lib.expresults.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.ExplicitElementsFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.OrElementsFilter;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

public class FilteredExperimentManager implements ExperimentManager {

	private ExperimentRepositoryManager manager;
	
	private ExperimentManager expManager;

	private ElementFilter methodFilter;
	private ElementFilter instanceFilter;

	private List<MethodDescription> filteredMethods;
	private List<InstanceDescription> filteredInstances;

	private long timeLimit = -1;
	private long maxTimeLimit = -1;

	private Set<String> methodsByExpName;

	public FilteredExperimentManager(ExperimentRepositoryManager manager, ExperimentManager expManager,
			ElementFilter instanceFilter, ElementFilter methodFilter) {
		this(manager, expManager, instanceFilter, methodFilter, null);
	}
	
	public FilteredExperimentManager(ExperimentRepositoryManager manager, ExperimentManager expManager,
			ElementFilter instanceFilter, ElementFilter methodFilter, String[] methodsByExpName) {
		this.manager = manager;
		this.expManager = expManager;
		this.methodFilter = methodFilter;
		this.instanceFilter = instanceFilter;
		
		if(methodsByExpName != null){
			this.methodsByExpName = new HashSet<String>(Arrays.asList(methodsByExpName));
		} else {
			this.methodsByExpName = Collections.emptySet();
		}
		
		filterMethods();
		filterInstances();
		calculateTimeLimits();
	}

	public FilteredExperimentManager(ExperimentRepositoryManager manager,
			ExperimentManager expManager,
			ElementFilter instanceFilter, String... methodsByExpName) {
		this(manager, expManager, instanceFilter, null, methodsByExpName);
	}

	private void calculateTimeLimits() {
		this.timeLimit = expManager.getTimeLimit(filteredMethods,filteredInstances);
		this.maxTimeLimit = expManager.getMaxTimeLimit(filteredMethods,filteredInstances);
	}

	@Override
	public List<MethodDescription> getMethods() {
		return filteredMethods;
	}

	private void filterMethods() {
		this.filteredMethods = new ArrayList<MethodDescription>();
		
		if(methodFilter == null && !methodsByExpName.isEmpty()){
			
			List<ElementFilter> filters = new ArrayList<ElementFilter>();			
			for(String methodName : methodsByExpName){
				filters.add(new ExplicitElementsFilter("{name="
							+ methodName + "}"));
			}
			
			this.methodFilter = new OrElementsFilter(filters.toArray(new ElementFilter[0]));
		}
		
		for (MethodDescription method : expManager.getMethods()) {
			
			boolean allowed = (methodFilter != null && methodFilter.isAllowed(method.getProperties()));
			
			allowed |= methodsByExpName.contains(expManager.getExperimentMethodName(method));
			
			allowed |= methodFilter == null;
			
			if (allowed){					
				filteredMethods.add(method);
			}
		}
	}

	@Override
	public List<InstanceDescription> getInstances() {
		return filteredInstances;
	}

	private void filterInstances() {
		this.filteredInstances = new ArrayList<InstanceDescription>();
		for (InstanceDescription instance : expManager.getInstances()) {
			if (instanceFilter == null
					|| instanceFilter.isAllowed(instance.getProperties())) {
				filteredInstances.add(instance);
			}
		}
	}

	@Override
	public String getExperimentMethodName(MethodDescription method) {
		return expManager.getExperimentMethodName(method);
	}

	@Override
	public List<ExecutionManager> getExecutionManagers(
			InstanceDescription instance, MethodDescription method) {

		if (filteredInstances.contains(instance)
				&& filteredMethods.contains(method)) {
			return expManager.getExecutionManagers(instance, method);
		} else {
			return null;
		}
	}

	@Override
	public BestMode getProblemBestMode() {
		return expManager.getProblemBestMode();
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
	public ExperimentManager createFilteredExperimentManager(
			ElementFilter instanceFilter, ElementFilter methodFilter) {
		return new FilteredExperimentManager(manager,this, instanceFilter, methodFilter);
	}
	
	@Override
	public ExperimentManager createFilteredExperimentManager(
			ElementFilter instanceFilter, String... methodsByExpName) {
		return new FilteredExperimentManager(manager,this, instanceFilter, methodsByExpName);
	}

	@Override
	public long getTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return expManager.getTimeLimit();
	}

	@Override
	public long getMaxTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {
		return expManager.getMaxTimeLimit();
	}

	@Override
	public List<Execution> getExecutions(InstanceDescription instance,
			MethodDescription method) {
		return expManager.getExecutions(instance, method);
	}

	@Override
	public String getName() {
		return "FilteredExperiment of "+this.expManager.getName();
	}
	
	@Override
	public Experiment getExperiment() {
		return null;
	}
	
	@Override
	public List<Execution> createExecutions() {
		
		List<Execution> execs = new ArrayList<Execution>();
		
		for(MethodDescription method :  filteredMethods){
			for(InstanceDescription instance: filteredInstances){
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
