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

public abstract class IsoIntervalConstructive<S extends Solution<I>, I extends Instance> extends
        IntervalConstructive<S, I> {

	private final int numIntervals;

	public IsoIntervalConstructive(int numIntervals) {
		this.numIntervals = numIntervals;
	}

	@Override
	protected int getNumIntervals() {
		return numIntervals;
	}

	@Override
	protected int getNumInterval(float totalPropotion) {
		//I have to do this check to avoid problems when a method takes more time than considered
		return Math.min((int) (totalPropotion * numIntervals), numIntervals-1);
	}

}
