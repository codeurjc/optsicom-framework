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
package es.optsicom.lib.exact;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.exact.ExactResult.ExactResultMode;

public class ExactResult<S extends Solution<? extends Instance>> {

	public enum ExactResultMode { OPTIMAL, ABORTED_BY_TIME_WITH_UPPER_AND_LOWER_BOUND, 
		ABORTED_BY_TIME_WITH_HEURISTIC_SOLUTION_WITHOUT_CPLEX_SOLUTION, ABORTED_BY_TIME_WITHOUT_BOUNDS
	}
	
	private long execTime;

	// The number of nodes processed.
	private long numNodos;

	private S solution;

	private double upperBound;
	private double lowerBound;

	private boolean abortedByTime = false;

	private ExactResultMode mode;

	public ExactResult(long execMillis, S solution, long numNodos) {
		this.mode = ExactResultMode.OPTIMAL;
		this.execTime = execMillis;
		this.solution = solution;
		this.numNodos = numNodos;
	}

	public ExactResult(long execTime, S solution, double upperBound, double lowerBound, long numNodos) {
		this(execTime, solution, numNodos);
		this.mode = ExactResultMode.ABORTED_BY_TIME_WITH_UPPER_AND_LOWER_BOUND;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.abortedByTime = true;
	}

	public ExactResult(long execMillis) {
		this(execMillis, null, -1);
		this.mode = ExactResultMode.ABORTED_BY_TIME_WITHOUT_BOUNDS;
	}
	
	public ExactResult(ExactResultMode mode, long execTime, S solution, double upperBound, double lowerBound, long numNodos) {
		this(execTime, solution, numNodos);
		this.mode = mode;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.abortedByTime = true;
	}

	public long getNumVisitedNodes() {
		return numNodos;
	}

	public void setNumNodos(int numNodos) {
		this.numNodos = numNodos;
	}

	public long getExecTime() {
		return execTime;
	}

	public void setExecTime(long execTime) {
		this.execTime = execTime;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public double getGap() {
		return upperBound - lowerBound;
	}

	public S getSolution() {
		return solution;
	}

	public boolean isAbortedByTime() {
		return abortedByTime;
	}

	@Override
	public String toString() {
		String result = "ExecTime: " + execTime + " Solution: " + solution.toString();

		if (isAbortedByTime()) {
			result = mode+" AbortedByTime: " + result + " UpperBound: " + upperBound + " LowerBound:"
			        + lowerBound;
		} else {
			result = mode+" Optimal: " + result;
		}

		return result;
	}

}
