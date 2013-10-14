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

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface ApproxMethodListener<S extends Solution<I>, I extends Instance> {

	void solutionImproved(ApproxMethod<S, I> approxMethod, S solution);
	
	void solutionImproved(ApproxMethod<S, I> approxMethod, double weight);

	void calculationStarted(ApproxMethod<S, I> approxMethod);

	void calculationFinished(ApproxMethod<S, I> approxMethod);

}
