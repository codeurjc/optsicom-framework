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
import es.optsicom.lib.util.Log;

public class StaticConstPathRelinking<S extends Solution<I>, I extends Instance> extends AbstractPathRelinking<S, I> {

	protected int numIter;

	public StaticConstPathRelinking(int numIter, Constructive<S, I> constructive, ImprovementMethod<S, I> improvement,
	        PathRelinking<S, I> pathRelinking, DistanceCalc<S, I> distCalc, int thresold) {
		super(constructive, pathRelinking, improvement, distCalc, thresold);
		this.numIter = numIter;
	}

	public void setNumIter(int numIter) {
		this.numIter = numIter;
	}

	@Id
	public int getNumIter() {
		return numIter;
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		//		1. Set GlobalIter equal to the number of global iterations.
		//		2. Apply the GRASP method (construction plus improvement)
		//		    for b=|ES| iterations to populate ES={ x1, x2, …, xb }.
		//		3. iter=b+1.
		//		While( iter < GlobalIter )
		//			4. Apply the construction phase of GRASP  x.
		//			5. Apply the local search phase of GRASP to x  x'.
		//			If ( zMM(x') > zMM(x1)  or  (zMM(x') > zMM(xb) and d(x',ES)  dth ) )
		//			6. Let xk be the closest solution to x' in ES with zMM(x')>zMM(xk).
		//			7. Add x' to ES and remove xk.
		//			8. Update the order in ES (from the best x1 to the worst xb).
		//		9. Let xbest= x1.
		//			
		//		For(i=1 to b-1 and j=i+1 to b)
		//			10. Apply PR(xi,xj) and PR(xj,xi), let x be the best solution found
		//			11. Apply the local search phase of GRASP to x  x'.
		//			If(zMM(x') > zMM(xbest))
		//				12. xbest= x'.
		//
		//		13. Return xbest.

		if (duration != -1) {
			System.out.println("WARN: This algorithm is not prepared to be executed by time. Ignoring time limit");
		}

		this.constructive.setInstance(instance);

		this.es = new EliteSet<S, I>(eliteSetSize, distCalc, thresold);

		populateElisteSet(numIter);

		for (int i = 0; i < eliteSetSize; i++) {
			for (int j = i + 1; j < eliteSetSize; j++) {
				S solXi = es.getSolution(i);
				S solXj = es.getSolution(j);

				if (!solXi.equals(solXj)) {

					S prSol = this.pathRelinking.pathRelinking(solXi, solXj);

					if (prSol != null) {
						improvement.improveSolution(prSol);
						setIfBestSolution(prSol);
					}
				} else {
					Log.debugln("Soluciones iguales en el EliteSet: " + solXi);
				}
			}
		}

	}

}
