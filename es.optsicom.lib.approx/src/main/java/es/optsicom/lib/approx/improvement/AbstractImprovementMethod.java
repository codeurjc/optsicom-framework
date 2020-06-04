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
package es.optsicom.lib.approx.improvement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class AbstractImprovementMethod<S extends Solution<I>, I extends Instance>
		implements ImprovementMethod<S, I> {

	private ImprovementMethodListener<S, I> listener;
	// TODO: improvedApplied pasa de ser private a protected por que uno de los
	// MDP así lo requería
	protected boolean improveApplied = false;
	protected int lastImprovVisitedSolutions = 0;
	protected long duration = -1;
	protected long timeLimit = -1;

	public int getLastImprovVisitedSolutions() {
		return lastImprovVisitedSolutions;
	}

	public void setImprovementMethodListener(ImprovementMethodListener<S, I> listener) {
		this.listener = listener;
	}

	public boolean improveSolution(S solution) {
		return improveSolution(solution, -1);
	}

	public final boolean improveSolution(S solution, long duration) {

		lastImprovVisitedSolutions = 0;

		this.improveApplied = false;

		if (duration != -1) {
			this.duration = duration;
			this.timeLimit = System.currentTimeMillis() + duration;
		}

		try {
			this.improveApplied = internalImproveSolution(solution, duration);
		} catch (TimeLimitException e) {
		}

		return this.improveApplied;
	}

	/**
	 * This method could be called by subclases when they found a new
	 * bestSolution in the improvement process. The solution doesn't need to be
	 * copied or cloned. All the relevant information will be captured in this
	 * method.
	 * 
	 * @param solution
	 */
	protected void newBestSolutionFound(S solution) {
		if (listener != null) {
			listener.newBestSolutionFound(this, solution);
		}
	}

	/**
	 * This method could be called by subclases when they found a new
	 * bestSolution in the improvement process. This method is provided to be
	 * used by improvement methods that no use Solution class in the improvement
	 * process. If the method uses Solution class, it should be call to
	 * {@link #newBestSolutionFound(Solution)}.
	 * 
	 * @param solution
	 */
	protected void newBestSolutionFound(double weight) {
		if (listener != null) {
			listener.newBestSolutionFound(this, weight);
		}
	}

	protected void checkFinishByTime() {
		if (duration != -1 && System.currentTimeMillis() > timeLimit) {
			throw new TimeLimitException();
		}
	}

	protected long getRemainigDuration() {
		if (this.duration == -1) {
			return -1;
		} else {
			return this.timeLimit - System.currentTimeMillis();
		}
	}

	public abstract boolean internalImproveSolution(S solution, long millis);

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
