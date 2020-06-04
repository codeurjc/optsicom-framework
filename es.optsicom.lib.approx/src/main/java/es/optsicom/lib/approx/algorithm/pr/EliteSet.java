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
import java.util.List;

import es.optsicom.lib.DistanceCalc;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public class EliteSet<S extends Solution<I>, I extends Instance> {

	private int b;
	private DistanceCalc<S, I> distCalc;
	private List<S> solutions;
	private int numSolutions = 0;
	private int bestSolutionIndex = -1;
	private int worstSolutionIndex = -1;
	private double thresold;

	public EliteSet(int b, DistanceCalc<S, I> distCalc, double thresold) {
		this.b = b;
		this.distCalc = distCalc;
		this.solutions = new ArrayList<S>(b);
		this.thresold = thresold;
	}

	public int addSolution(S solution) {

		if (numSolutions < b) {
			// System.out.println("<b");
			solutions.add(solution);
			updateBestWeight(solution, numSolutions);
			updateWorstWeight(solution, numSolutions);
			numSolutions++;
			return numSolutions - 1;
		} else {
			// System.out.print(">b ");
			if (solution.isBetterThan(this.solutions.get(bestSolutionIndex))
					|| solution.isBetterThan(this.solutions.get(worstSolutionIndex))
							&& distCalc.calculateDistance(solution, solutions) > thresold) {

				// System.out.println("Enter solution: " +
				// solution.getWeight());

				// We substitute the closer of those solutions that are worse
				// than the given solution
				double lessDistance = Double.MAX_VALUE;
				int changeIndex = -1;
				for (int i = 0; i < solutions.size(); i++) {
					// We never substitute the best solution
					if (solution.isBetterThan(solutions.get(i)) && (i != bestSolutionIndex)) {
						double distance = distCalc.calculateDistance(solution, solutions.get(i));
						if (distance < lessDistance) {
							lessDistance = distance;
							changeIndex = i;
						}
					} else if (solution.equals(solutions.get(i))) {
						return -1;
					}
				}

				//S oldSolution = solutions.get(changeIndex);
				solutions.set(changeIndex, solution);

				updateBestWeight(solution, changeIndex);

				// System.out.println("Replaced: " + oldSolution.getWeight());

				if (changeIndex == worstSolutionIndex) {
					worstSolutionIndex = -1;
					int index = 0;
					for (S element : solutions) {
						updateWorstWeight(element, index++);
					}
				}

				return changeIndex;
			} else {
				return -1;
			}
		}

	}

	public boolean addSolutionWithoutDiversityCheck(S solution) {

		// Replace worst solution if "solution" is better than it

		if (solution.isBetterThan(solutions.get(worstSolutionIndex))) {
			// System.out.println("Replaced
			// solution:"+solutions[worstSolutionIndex].getWeight()+" with
			// "+solution.getWeight());
			solutions.set(worstSolutionIndex, solution);
			updateBestWeight(solution, worstSolutionIndex);
			worstSolutionIndex = -1;
			int index = 0;
			for (S element : solutions) {
				updateWorstWeight(element, index++);
			}
			return true;
		}
		return false;
	}

	private void updateWorstWeight(S solution, int solutionIndex) {
		if (worstSolutionIndex == -1 || this.solutions.get(worstSolutionIndex).isBetterThan(solution)) {
			worstSolutionIndex = solutionIndex;
			// System.out.println("W:" + solution.getWeight());
		}
	}

	private void updateBestWeight(S solution, int solutionIndex) {
		if (bestSolutionIndex == -1 || solution.isBetterThan(this.solutions.get(bestSolutionIndex))) {
			bestSolutionIndex = solutionIndex;
			// System.out.println("B:" + solution.getWeight());
		}
	}

	public S getSolution(int i) {
		return solutions.get(i);
	}

	public List<S> getSolutions() {
		return solutions;
	}

}
