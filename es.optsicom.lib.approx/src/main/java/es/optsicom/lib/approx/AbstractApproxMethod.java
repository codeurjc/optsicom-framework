/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.approx;

import es.optsicom.lib.AbstractMethod;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.experiment.ApproxExecResult;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.approx.improvement.ImprovementMethodListener;
import es.optsicom.lib.experiment.ExecLogger;
import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.experiment.StopMethodException;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.BestMode;

/**
 * 
 * This an abstract class that contains utility methods for implementing
 * SolutionCalculators. It can be used for maximization and minimization
 * problems.
 * 
 * Subclasses must implement {@link #internalCalculateSolution(long)}
 * 
 * @param <S>
 *            class that represents problem's solutions
 * @param <I>
 *            class that represents problem's instances
 */
public abstract class AbstractApproxMethod<S extends Solution<I>, I extends Instance>
		extends AbstractMethod<S, I> implements ApproxMethod<S, I>,
		ImprovementMethodListener<S, I> {

	protected S bestSolution;
	protected double bestSolutionWeight = 0;

	protected ApproxMethodListener<S, I> approxMethodListener;
	protected I instance;
	private BestMode bestMode;

	public final ExecutionResult execute() {
		return execute(-1);
	}

	public final ExecutionResult execute(long timeout) {
		// startTime = System.currentTimeMillis();
		if (approxMethodListener != null) {
			approxMethodListener.calculationStarted(this);
		}
		
		try {
			internalCalculateSolution(timeout);
		} catch(StopMethodException e) {
			// Do nothing.
		}
		
		if (approxMethodListener != null) {
			approxMethodListener.calculationFinished(this);
		}
		
		ApproxExecResult execResult = new ApproxExecResult(bestSolution);
        bestSolution = null;
        return execResult;
        
}

	/**
	 * 
	 * @param timeout
	 *            the timeout or -1 if no time limit is set
	 */
	protected abstract void internalCalculateSolution(long timeout);

//	public List<S> calculateSolutions(int numberSolutions) {
//		List<S> solutions = new ArrayList<S>();
//		for (int i = 0; i < numberSolutions; i++) {
//			solutions.add(execute());
//		}
//		return solutions;
//	}
//
//	public List<S> calculateSolutions(int numberSolutions, long milliseconds) {
//		List<S> solutions = new ArrayList<S>();
//		for (int i = 0; i < numberSolutions; i++) {
//			solutions.add(execute(milliseconds));
//		}
//		return solutions;
//	}

	public S getBestSolution() {
		if (bestMode.isBetterThan(bestSolutionWeight, bestSolution.getWeight())) {
			throw new RuntimeException(
					"bestSolutionWeight is better than bestSolution.getWeight(). " +
					"This is produced because an improvement method is executed but " +
					"not setIfBestSolution is called after that.");
		}
		return this.bestSolution;
	}

	public void setSolutionCalculatorListener(
			ApproxMethodListener<S, I> listener) {
		this.approxMethodListener = listener;
	}

	public void removeSolutionCalculatorListener(
			ApproxMethodListener<S, I> listener) {
		this.approxMethodListener = null;
	}

	protected void initBestSolution() {
		bestSolution = null;
		bestSolutionWeight = 0;
	}

	@SuppressWarnings("unchecked")
	protected boolean setIfBestSolution(S solution) {

		if (bestSolution == null || solution.isBetterThan(bestSolution)) {

			bestSolution = (S) solution.createCopy();
			bestSolutionWeight = bestSolution.getWeight();
			
			ExecLogger.newSolutionFound(bestSolution);

			if (approxMethodListener != null) {
				approxMethodListener.solutionImproved(this, bestSolution);
			}
			
			System.out.println(" > "+bestSolutionWeight);
			
			return true;
		} else {
			return false;
		}
	}

	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod,
			S newBestSolution) {
		// This methods are called only by the improvement method. We have
		// decided to not
		// copy the solution in this cases by the overhead that this copy. Is
		// responsibility of
		// implementor of the approxMethod, to call to setIfBestSolution when
		// the improvement method
		// finish its execution.
		if (approxMethodListener != null) {
			if (bestMode.isBetterThan(newBestSolution.getWeight(),
					bestSolutionWeight)) {
				bestSolutionWeight = newBestSolution.getWeight();
				approxMethodListener.solutionImproved(this, newBestSolution);
				System.out.println(" > "+bestSolutionWeight);
			}
		}
	}

	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod,
			double weight) {
		// This methods are called only by the improvement method. We have
		// decided to not
		// copy the solution in this cases by the overhead that this copy. Is
		// responsibility of
		// implementor of the approxMethod, to call to setIfBestSolution when
		// the improvement method
		// finish its execution.
		if (approxMethodListener != null) {
			if (bestMode.isBetterThan(weight, bestSolutionWeight)) {
				bestSolutionWeight = weight;
				approxMethodListener.solutionImproved(this, weight);
			}
		}
	}

	public I getInstance() {
		return instance;
	}

	public void setInstance(I instance) {
		initBestSolution();
		this.instance = instance;
		this.bestMode = instance.getProblem().getMode();
	}

	public void removeInstance() {
		this.instance = null;
	}
	
	public MethodDescription createMethodDescription(){
		return new MethodDescription(new DBProperties(this.getProperties().getMap()));
	}
}
