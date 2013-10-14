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
package es.optsicom.lib.approx.constructive.test;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.constructive.IsoIntervalConstructive;

public class DummyIsoIntervalConstructive<S extends Solution<I>, I extends Instance> extends IsoIntervalConstructive<S, I> {

	public DummyIsoIntervalConstructive(int numIntervals) {
		super(numIntervals);
	}

	@Override
	public void initInterval(int interval) {
		System.out.println("Init Interval: " + interval);
	}

	@Override
	public S createSolution(int numInterval) {
		System.out.println("Created Solution in numInterval" + numInterval);
		return null;
	}

	public boolean isDeterminist() {
		return false;
	}

	@Override
	public List<S> createSolutions(int numSolutions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<S> createSolutionsInTime(long millis) {
		// TODO Auto-generated method stub
		return null;
	}

}
