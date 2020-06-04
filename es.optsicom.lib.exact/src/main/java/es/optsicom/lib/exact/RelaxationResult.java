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

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.exact.RelaxationResult.Mode;

public class RelaxationResult<S extends Solution<? extends Instance>> {

	public enum Mode { OPTIMAL, ABORTED_BY_TIME, SOLUTION_NOT_FOUND, UNKNOWN }
	
	private long execTime;

	private double upperBound;
	private double solution;

	private boolean abortedByTime = false;

	private Mode mode;

	private List<String> varNames;

	private double[] values;

	public RelaxationResult(Mode mode, long execTime, boolean abortedByTime, double upperBound, double solution,
			List<String> varNames, double[] values) {
		
		this.mode = mode;
		this.upperBound = upperBound;
		this.solution = solution;
		this.varNames = varNames;
		this.values = values;
		this.execTime = execTime;
		this.abortedByTime = abortedByTime;
		
	}

	public long getExecTime() {
		return execTime;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public double getSolution() {
		return solution;
	}

	public boolean isAbortedByTime() {
		return abortedByTime;
	}

	public Mode getMode() {
		return mode;
	}

	public List<String> getVarNames() {
		return varNames;
	}

	public double[] getValues() {
		return values;
	}

	@Override
	public String toString() {
		return "RelaxationResult [execTime=" + execTime + ", upperBound="
				+ upperBound + ", solution=" + solution + ", abortedByTime="
				+ abortedByTime + ", mode=" + mode + ", varNames=" + varNames
				+ ", values=" + Arrays.toString(values) + "]";
	}
}

