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
package es.optsicom.lib.exact.bb;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.ApproxMethod;
import es.optsicom.lib.util.Id;

public abstract class FixedBinaryTreeBB<S extends Solution<I>, I extends Instance> extends BranchAndBound<S, I> {

	protected I instance;
	protected int n;
	protected int m;

	protected ApproxMethod<S, I> approxMethod = null;
	protected SolutionManager<S, I> solutionManager = null;

	protected double bestSolutionWeight = Double.NEGATIVE_INFINITY;
	protected int[] bestSolutionNodes;
	protected int numVisitedNodes = 0;
	protected Status status = Status.TIME_LIMIT;
	protected double upperBound;
	protected long timeLimit;

	public FixedBinaryTreeBB() {
		super();
	}

	@Override
	public int[] getBestSolutionNodes() {
		return bestSolutionNodes;
	}

	@Override
	public double getBestSolutionWeight() {
		return bestSolutionWeight;
	}

	@Override
	public double getLowerBound() {
		return bestSolutionWeight;
	}

	@Override
	public long getNumTotalNodes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumVisitedNodes() {
		return numVisitedNodes;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	protected void checkTime() {
		if (System.currentTimeMillis() > timeLimit) {
			throw new TimeLimitException();
		}
	}

	@Id
	public ApproxMethod<S, I> getHeuristic() {
		return approxMethod;
	}

	@Override
	public double getUpperBound() {
		return upperBound;
	}

	public FixedBinaryTreeBB<S, I> setApproxMethod(ApproxMethod<S, I> heuristic) {
		this.approxMethod = heuristic;
		return this;
	}

	@Override
	protected S createSolution(int[] bestSolutionNodes, I instance) {
		return solutionManager.createSolution(bestSolutionNodes, instance);
	}

	public void setSolutionManager(SolutionManager<S, I> solutionManager) {
		this.solutionManager = solutionManager;
	}

}