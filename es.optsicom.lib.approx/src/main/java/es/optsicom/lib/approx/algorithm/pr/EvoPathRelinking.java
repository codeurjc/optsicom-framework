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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.DistanceCalc;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.Id;

public class EvoPathRelinking<S extends Solution<I>, I extends Instance> extends AbstractPathRelinking<S, I> {

	private int globalIter = 5;
	private int localIter = 20;

	public EvoPathRelinking(int globalIter, int localIter, Constructive<S, I> constructive,
			ImprovementMethod<S, I> improvement, PathRelinking<S, I> pathRelinking, DistanceCalc<S, I> distCalc,
			int thresold) {
		super(constructive, pathRelinking, improvement, distCalc, thresold);
		this.globalIter = globalIter;
		this.localIter = localIter;
	}

	@Id
	public int getGlobalIter() {
		return globalIter;
	}

	public EvoPathRelinking<S, I> setGlobalIter(int iter) {
		globalIter = iter;
		return this;
	}

	@Id
	public int getLocalIter() {
		return localIter;
	}

	public EvoPathRelinking<S, I> setLocalIter(int iter) {
		localIter = iter;
		return this;
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		// 1. Set GlobalIter equal to the number of global iterations.
		// 2. Apply the GRASP method (construction plus improvement) for b=|ES|
		// iterations to populate ES. ES={ x1, x2, …, xb }.
		//
		// For(iter= 1 to GIter)
		// For(i=1 to LocalIter)
		// 3. Apply the construction phase of GRASP  x.
		// 4. Apply the local search phase of GRASP to x  x'.
		// 5. Randomly select xj in ES (Map values into probabilities).
		// 6. Apply PR(x', xj) and PR(xj,x'), let y be the best solution found.
		// 7. Apply the local search phase of GRASP to y  y'.
		// If ( zMM(y') > zMM(x1) or (zMM(y') > zMM(xb) and d(y',ES)  dth))
		// 8. Let xk be the closest solution to y' in ES with zMM(y')>zMM(xk).
		// 9. Add y' to ES and remove xk.
		// 10. Update the order in ES (from the best x1 to the worst xb).
		//
		// 11. NewSol=1
		// While(NewSol)
		// 12. NewSol=0
		// 13. Apply PR(x,x’) and PR(x’,x) for every pair (x,x’) in ES not
		// combined before. Let y be the best solution found.
		// 14. Apply the local search phase of GRASP to y  y'.
		// If ( zMM(y') > zMM(x1) or (zMM(y') > zMM(xb) and d(y',ES)  dth))
		// 15. Let xk be the closest solution to y' in ES with zMM(y')>zMM(xk).
		// 16. Add y' to ES and remove xk.
		// 17. Update the order in ES (from the best x1 to the worst xb).
		// 18. NewSol=1
		//
		// 19. Return x1.

		long limitTime = Long.MAX_VALUE;
		if (duration != -1) {
			limitTime = System.currentTimeMillis() + duration;
		}

		this.constructive.setInstance(instance);

		this.es = new EliteSet<S, I>(eliteSetSize, distCalc, thresold);

		this.populateElisteSet(eliteSetSize);

		if (duration == -1) {

			for (int i = 0; i < globalIter; i++) {

				for (int j = 0; j < localIter; j++) {
					doDynEliteSetIteration(Long.MAX_VALUE);
				}

				eliteSetEvolution(limitTime);
			}

		} else {

			while (System.currentTimeMillis() < limitTime) {

				for (int j = 0; j < localIter; j++) {

					if (System.currentTimeMillis() > limitTime) {
						return;
					}

					doDynEliteSetIteration(limitTime);
				}

				eliteSetEvolution(limitTime);
			}

		}

	}

	protected void eliteSetEvolution(long limitTime) {

		// System.out.println("Evolution ES");

		int newEsSolutionsCounter;

		final boolean NEW = true;
		final boolean OLD = false;

		boolean[] esSolutions = new boolean[eliteSetSize];
		Arrays.fill(esSolutions, NEW);

		do {

			newEsSolutionsCounter = 0;

			List<S> solutions = new ArrayList<S>();

			for (int i = 0; i < eliteSetSize; i++) {
				for (int j = i + 1; j < eliteSetSize; j++) {

					if (System.currentTimeMillis() > limitTime) {
						return;
					}

					if (esSolutions[i] == NEW || esSolutions[j] == NEW) {

						S solI = es.getSolution(i);
						S solJ = es.getSolution(j);
						if (!solI.equals(solJ)) {

							S prSol = this.pathRelinking.pathRelinking(solI, solJ);

							if (prSol != null) {
								improvement.improveSolution(prSol);
								setIfBestSolution(prSol);

								// System.out.println("Sol: " +
								// prSol.getWeight());

								solutions.add(prSol);
							}
						}
					}
				}
			}

			for (int i = 0; i < eliteSetSize; i++) {
				esSolutions[i] = OLD;
			}

			for (S solution : solutions) {
				int index = es.addSolution(solution);
				if (index != -1) {
					esSolutions[index] = NEW;
					newEsSolutionsCounter++;
				}
			}

		} while (newEsSolutionsCounter > 0);

	}
}
