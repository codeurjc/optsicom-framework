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

public abstract class IntervalConstructive<S extends Solution<I>, I extends Instance>
		extends AbstractConstructive<S, I> {

	enum CreationMode {
		INDEPENDENT_CREATION, CREATION_BY_TIME, CREATION_BY_NUM
	}

	protected long startTime;
	protected long millis;
	protected int numSolutions;
	protected int createdSolutions;
	protected CreationMode creationMode = CreationMode.INDEPENDENT_CREATION;
	protected boolean[] intervalInitialized;
	private int currentInterval;

	// public IntervalConstructive(I instance) {
	// super(instance);
	// }

	public IntervalConstructive() {
	}

	private S createSolutionForInterval(int currentInterval) {

		this.currentInterval = currentInterval;

		int auxCurrentInterval = currentInterval;
		while (!intervalInitialized[auxCurrentInterval]) {
			auxCurrentInterval--;
		}

		auxCurrentInterval++;
		while (auxCurrentInterval <= currentInterval) {
			initInterval(auxCurrentInterval);
			intervalInitialized[auxCurrentInterval] = true;
			auxCurrentInterval++;
		}

		createdSolutions++;
		return createSolution(currentInterval);
	}

	public void initSolutionCreation() {
		this.creationMode = CreationMode.INDEPENDENT_CREATION;
	}

	public void initSolutionCreationByNum(int numSolutions) {
		this.intervalInitialized = new boolean[getNumIntervals()];
		this.intervalInitialized[0] = true;
		initInterval(0);
		this.creationMode = CreationMode.CREATION_BY_NUM;
		this.numSolutions = numSolutions;
		this.createdSolutions = 0;
	}

	public void initSolutionCreationByTime(long millis) {
		this.intervalInitialized = new boolean[getNumIntervals()];
		this.intervalInitialized[0] = true;
		initInterval(0);
		this.creationMode = CreationMode.CREATION_BY_TIME;
		this.startTime = System.currentTimeMillis();
		this.millis = millis;
	}

	public S createSolution() {

		S solution = null;

		switch (creationMode) {
		case CREATION_BY_NUM: {
			float proportion = ((float) createdSolutions) / numSolutions;
			solution = createSolutionForInterval(getNumInterval(proportion));
			break;
		}
		case CREATION_BY_TIME: {
			float proportion = ((float) (System.currentTimeMillis() - startTime)) / millis;
			solution = createSolutionForInterval(getNumInterval(proportion));
			break;
		}
		case INDEPENDENT_CREATION: {
			solution = createSolution(-1);
			break;
		}
		}

		return solution;
	}

	protected int getCurrentInterval() {
		return currentInterval;
	}

	protected abstract int getNumInterval(float totalPropotion);

	protected abstract void initInterval(int interval);

	protected abstract S createSolution(int numInterval);

	protected abstract int getNumIntervals();

}
