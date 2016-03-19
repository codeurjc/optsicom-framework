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
import es.optsicom.lib.util.description.Descriptive;

public interface ImprovementMethod<S extends Solution<I>, I extends Instance> extends Descriptive {

	public abstract int getLastImprovVisitedSolutions();

	public abstract boolean improveSolution(S solution);

	public abstract boolean improveSolution(S solution, long millis);

	public void setImprovementMethodListener(ImprovementMethodListener<S, I> listener);

}