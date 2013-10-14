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
package es.optsicom.lib.approx.experiment;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.ApproxMethod;
import es.optsicom.lib.approx.ApproxMethodListener;
import es.optsicom.lib.expresults.saver.DBExecutionSaver;

public class ExecutionApproxMethodListener<S extends Solution<I>, I extends Instance> implements ApproxMethodListener<S, I> {

	private DBExecutionSaver execSaver;

	public ExecutionApproxMethodListener(DBExecutionSaver execSaver) {
		this.execSaver = execSaver;
	}

	public void calculationFinished(ApproxMethod<S, I> calculator) {
		
	}

	public void calculationStarted(ApproxMethod<S, I> calculator) {
		
	}

	public void solutionImproved(ApproxMethod<S, I> approxMethod, S solution) {
		execSaver.addSolutionEvents(solution.getWeight(), solution.getInfoToSave());
	}
	
	public void solutionImproved(ApproxMethod<S, I> approxMethod, double weight) {
		execSaver.addSolutionValueEvent(weight);
	}

}
