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
package es.optsicom.lib.approx.constructive;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public abstract class NonIntervalConstructive<S extends Solution<I>, I extends Instance> extends AbstractConstructive<S, I> {

	public NonIntervalConstructive() {
	}

	public void initSolutionCreation() {
	}

	public void initSolutionCreationByNum(int numSolutions) {
	}

	public void initSolutionCreationByTime(long millis) {
	}

}
