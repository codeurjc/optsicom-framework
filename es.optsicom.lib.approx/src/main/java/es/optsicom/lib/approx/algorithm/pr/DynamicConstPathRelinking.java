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
package es.optsicom.lib.approx.algorithm.pr;

import es.optsicom.lib.DistanceCalc;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.Id;

public class DynamicConstPathRelinking<S extends Solution<I>, I extends Instance> extends AbstractPathRelinking<S, I> {

	protected int numIter;

	public DynamicConstPathRelinking(int numIter, Constructive<S, I> constructive, ImprovementMethod<S, I> improvement,
			PathRelinking<S, I> pathRelinking, DistanceCalc<S, I> distCalc, int thresold) {

		super(constructive, pathRelinking, improvement, distCalc, thresold);
		this.numIter = numIter;
	}

	@Id
	public int getNumIter() {
		return numIter;
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		// 1. Set GlobalIter equal to the number of global iterations.
		// 2. Apply the GRASP method (construction plus improvement)
		// for b=|ES| iterations to populate ES. ES={ x1, x2, …, xb }.
		// 3. iter=b+1.
		// While( iter < GlobalIter )
		// 4. Apply the construction phase of GRASP  x.
		// 5. Apply the local search phase of GRASP to x  x'.
		// 6. Randomly select xj in ES (Map values into probabilities).
		// 7. Apply PR(x', xj) and PR(xj,x'), let y be the best solution found.
		// 8. Apply the local search phase of GRASP to y  y'.
		// If ( zMM(y') > zMM(x1) or (zMM(y') > zMM(xb) and d(y',ES)  dth))
		// 9. Let xk be the closest solution to y' in ES with zMM(y')>zMM(xk).
		// 10. Add y' to ES and remove xk.
		// 11. Update the order in ES (from the best x1 to the worst xb).
		//
		// 12. Return xbest.

		long finishTime;
		if (duration != -1) {
			finishTime = System.currentTimeMillis() + duration;
		} else {
			finishTime = Long.MAX_VALUE;
		}

		this.constructive.setInstance(instance);

		this.es = new EliteSet<S, I>(eliteSetSize, distCalc, thresold);

		this.populateElisteSet(eliteSetSize);

		if (duration == -1) {

			for (int i = eliteSetSize; i < numIter; i++) {
				doDynEliteSetIteration(finishTime);
			}

		} else {

			while (System.currentTimeMillis() < finishTime) {
				doDynEliteSetIteration(finishTime);
			}

		}

	}

}
