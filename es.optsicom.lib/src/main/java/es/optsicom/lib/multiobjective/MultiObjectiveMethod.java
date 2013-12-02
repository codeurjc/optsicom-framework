package es.optsicom.lib.multiobjective;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Method;
import es.optsicom.lib.multiobjective.experiment.MOExecutionResult;


public interface MultiObjectiveMethod<S extends MultiObjectiveSolution<I>, I extends Instance> extends Method<S, I> {

	public boolean isMultiObjective();
	
	@Override
	public MOExecutionResult execute();
	
	@Override
	public MOExecutionResult execute(long timeLimit);
	
}
