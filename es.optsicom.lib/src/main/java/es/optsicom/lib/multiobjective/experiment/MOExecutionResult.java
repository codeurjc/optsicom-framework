package es.optsicom.lib.multiobjective.experiment;

import java.util.List;

import es.optsicom.lib.Solution;
import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.experiment.ExecutionLogger.Event;
import es.optsicom.lib.multiobjective.MultiObjectiveSolution;

public abstract class MOExecutionResult extends ExecutionResult {

	@Override
	public abstract MultiObjectiveSolution getBestSolution();

}
