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

import java.util.Arrays;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public abstract class NonIsoIntervalConstructive<S extends Solution<I>, I extends Instance> extends
        IntervalConstructive<S, I> {

	private final float[] accIntervalsProportion;

	public NonIsoIntervalConstructive(float[] intervalsProportion) {
		this.accIntervalsProportion = new float[intervalsProportion.length];
		float previous = 0;
		for (int i = 0; i < accIntervalsProportion.length; i++) {
			accIntervalsProportion[i] = previous + intervalsProportion[i];
			previous = accIntervalsProportion[i];
		}
	}

	@Override
	protected int getNumIntervals() {
		return accIntervalsProportion.length;
	}

	@Override
	protected int getNumInterval(float totalPropotion) {
		int index = Arrays.binarySearch(accIntervalsProportion, totalPropotion);
		if (index < 0) {
			return -index - 1;
		} else {
			return index;
		}
	}

}
