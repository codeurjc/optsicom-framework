package es.optsicom.lib.expresults.manager;

import java.util.List;
import java.util.Map;


import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

public interface ExperimentManager {
		
	public abstract List<MethodDescription> getMethods();
	
	public abstract List<InstanceDescription> getInstances();
	
	public abstract String getExperimentMethodName(MethodDescription method);
	
	public abstract List<ExecutionManager> getExecutionManagers(InstanceDescription instance, MethodDescription method);

	public abstract BestMode getProblemBestMode();

	public abstract long getTimeLimit();

	/**
	 * Returns the maximum time limit configured in the instances of this experiment. 
	 * @return
	 */
	public abstract long getMaxTimeLimit();

	public abstract ExperimentManager createFilteredExperimentManager(ElementFilter instanceFilter, ElementFilter methodFilter);
	
	public abstract ExperimentManager createFilteredExperimentManager(ElementFilter instanceFilter, String... methodsByExpName);

	public abstract long getTimeLimit(List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances);

	public abstract long getMaxTimeLimit(
			List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances);

	public abstract List<Execution> getExecutions(InstanceDescription instance,
			MethodDescription method);

	public abstract String getName();
	
	public abstract Experiment getExperiment();

	public abstract List<Execution> createExecutions();

	public abstract Map<MethodDescription, String> createExperimentMethodNames();

}
