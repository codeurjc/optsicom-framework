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

import java.util.List;

import es.optsicom.lib.DistanceCalc;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.Log;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.lib.util.RandomizedSelector;

public abstract class AbstractPathRelinking<S extends Solution<I>, I extends Instance>
		extends AbstractApproxMethod<S, I> {

	private final int MAX_RETRIES;

	protected int eliteSetSize = 10;
	protected Constructive<S, I> constructive;
	protected PathRelinking<S, I> pathRelinking;
	protected ImprovementMethod<S, I> improvement;
	protected DistanceCalc<S, I> distCalc;
	protected int thresold;

	protected EliteSet<S, I> es;

	protected long finishTime = Long.MAX_VALUE;

	public AbstractPathRelinking(Constructive<S, I> constructive, PathRelinking<S, I> pathRelinking,
			ImprovementMethod<S, I> improvement, DistanceCalc<S, I> distCalc, int thresold) {
		this.constructive = constructive;
		this.pathRelinking = pathRelinking;
		this.improvement = improvement;
		this.distCalc = distCalc;
		this.thresold = thresold;

		this.MAX_RETRIES = eliteSetSize;

	}

	@Id
	public int getThresold() {
		return thresold;
	}

	@Id
	public Constructive<S, I> getConstructive() {
		return constructive;
	}

	@Id
	public PathRelinking<S, I> getPathRelinking() {
		return pathRelinking;
	}

	@Id
	public ImprovementMethod<S, I> getImprovement() {
		return improvement;
	}

	@Id
	public DistanceCalc<S, I> getDistCalc() {
		return distCalc;
	}

	protected void populateElisteSet(int numConstructions) {

		for (int i = 0; i < numConstructions; i++) {
			S solution = constructive.createSolution();
			setIfBestSolution(solution);

			if (System.currentTimeMillis() > finishTime) {
				return;
			}

			improvement.improveSolution(solution, finishTime - System.currentTimeMillis());
			setIfBestSolution(solution);
			es.addSolution(solution);

			if (System.currentTimeMillis() > finishTime) {
				return;
			}
		}
	}

	protected void doDynEliteSetIteration(long finishTime) {

		S solution = constructive.createSolution();
		setIfBestSolution(solution);
		improvement.improveSolution(solution, finishTime - System.currentTimeMillis());
		setIfBestSolution(solution);

		if (System.currentTimeMillis() > finishTime) {
			return;
		}

		S esSol;
		int retries = 0;
		do {
			esSol = RandomizedSelector.selectRandomlyObject(es.getSolutions());
			retries++;
			if (retries > MAX_RETRIES) {
				Log.debugln("Too much retries selecting a solution from Elite Set");
				return;
			}
		} while (esSol.equals(solution));

		S prSol = this.pathRelinking.pathRelinking(solution, esSol);
		if (prSol != null) {
			improvement.improveSolution(prSol, finishTime - System.currentTimeMillis());
			setIfBestSolution(prSol);
			es.addSolution(prSol);
		}

	}

	// Es una versión simplificada del método doDynEliteSetIteration. Es
	// simplificada porque
	// la selección del la solución del ES se realiza de forma totalmente
	// aleatoria, en vez vez
	// de realizarse de forma proporcional a la calidad. Además, también es una
	// versión simplificada
	// porque no tiene control de diversidad al insertar una solución al ES.
	// Simplemente añade una
	// solución si es mejor que la peor del ES.
	protected void doSimpleDynEliteSetIteration(long finishTime) {

		// System.out.println("SimpleDynEliteSetIteration");

		S solution = constructive.createSolution();
		setIfBestSolution(solution);
		improvement.improveSolution(solution, finishTime - System.currentTimeMillis());
		setIfBestSolution(solution);

		if (System.currentTimeMillis() > finishTime) {
			return;
		}

		S esSol;
		int retries = 0;
		do {
			List<S> esSolutions = es.getSolutions();
			int numSol = RandomManager.nextInt(esSolutions.size());
			esSol = esSolutions.get(numSol);
			retries++;
			if (retries > MAX_RETRIES) {
				Log.debugln("Too much retries selecting a solution from Elite Set");
				return;
			}
		} while (esSol.equals(solution));

		S prSol = this.pathRelinking.pathRelinking(solution, esSol);
		// System.out.println("PathRelinking: "+prSol.getWeight());
		if (prSol != null) {

			if (System.currentTimeMillis() > finishTime) {
				return;
			}

			improvement.improveSolution(prSol, finishTime - System.currentTimeMillis());
			// System.out.println("ImprovedSolution: "+prSol.getWeight());
			setIfBestSolution(prSol);
			es.addSolutionWithoutDiversityCheck(prSol);
		}

	}

	@Id
	public int getEliteSetSize() {
		return eliteSetSize;
	}

	public void setEliteSetSize(int eliteSetSize) {
		this.eliteSetSize = eliteSetSize;
	}

}