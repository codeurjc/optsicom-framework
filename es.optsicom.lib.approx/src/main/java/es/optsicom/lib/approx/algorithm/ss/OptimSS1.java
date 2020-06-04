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
package es.optsicom.lib.approx.algorithm.ss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.combinator.Combinator;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.diversificator.Diversificator;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.BiggestInvertSortLimitedList;

//RemoveUsed = false;
public class OptimSS1<S extends Solution<I>, I extends Instance> extends AbstractApproxMethod<S, I> {

	private static final int NUM_INITIAL_SOLUTIONS = 100;

	private long finishTime;
	private int globalIterations = 10;

	private Constructive<S, I> constructive;

	private ImprovementMethod<S, I> improvingMethod;

	private Diversificator<S> diversificator;
	private Combinator<S, I> combinator;

	private static final int NUM_BEST_SOLUTIONS = 4;
	private static final int NUM_DIVERSE_SOLUTIONS = 8;

	private List<S> initialSolutions;

	private BiggestInvertSortLimitedList<S> refSet;

	private List<S> newRefSetSolutions;

	private SSCombinationsGen combinations;

	private Map<S, S> improvedSolutions;

	public OptimSS1(Constructive<S, I> constructive, Combinator<S, I> comb, ImprovementMethod<S, I> impMethod,
			Diversificator<S> div) {
		this.constructive = constructive;
		this.combinator = comb;
		this.improvingMethod = impMethod;
		this.diversificator = div;
	}

	@Override
	public void internalCalculateSolution(long milliseconds) {

		this.improvedSolutions = new HashMap<S, S>();
		this.combinations = new SSCombinationsGen(2, NUM_BEST_SOLUTIONS + NUM_DIVERSE_SOLUTIONS);

		if (milliseconds == -1) {
			this.finishTime = Long.MAX_VALUE;
		} else {
			this.finishTime = System.currentTimeMillis() + milliseconds;
		}

		this.initialSolutions = new ArrayList<S>();
		this.constructive.initSolutionCreationByNum(NUM_INITIAL_SOLUTIONS);

		refSet = new BiggestInvertSortLimitedList<S>(NUM_BEST_SOLUTIONS);

		for (int i = 0; i < NUM_INITIAL_SOLUTIONS; i++) {
			S solution = this.constructive.createSolution();
			this.initialSolutions.add(solution);
			setIfBestSolution(solution);

			if (System.currentTimeMillis() > this.finishTime) {
				return;
			}

			improveAndRefreshRefSet(solution);

			if (System.currentTimeMillis() > this.finishTime) {
				return;
			}
		}
		// Debug.debugln("Created and improved initial solutions");

		refSet.setMaxSize(NUM_BEST_SOLUTIONS + NUM_DIVERSE_SOLUTIONS);

		refSet.addAll(diversificator.getDiversity(NUM_DIVERSE_SOLUTIONS, initialSolutions, refSet.getList()));

		int iter = 0;

		global: while (true) {

			SSRefreshMemCombinator<S, I> comb = getIfSSRefreshMemCombinator();

			Collection<List<S>> groups = combinations.getGroups(refSet.getList());

			do {

				List<S> oldRefSet = new ArrayList<S>(refSet.getList());

				for (List<S> group : groups) {
					S solution = combinator.combineGroup(group);
					setIfBestSolution(solution);
					if (comb != null) {
						comb.refreshMemory(solution);
					}
					improveAndRefreshRefSet(solution);

					if (System.currentTimeMillis() > this.finishTime) {
						break global;
					}
				}
				// Debug.debugln("Combined and improved solutions");

				newRefSetSolutions = new ArrayList<S>(refSet.getList());
				newRefSetSolutions.removeAll(oldRefSet);
				oldRefSet.retainAll(refSet.getList());

				if (newRefSetSolutions.size() == 0) {
					break;
				}

				groups = createGroupsToCombine();

			} while (true);

			if (System.currentTimeMillis() > this.finishTime) {
				break;
			}

			if (refSet.size() > NUM_BEST_SOLUTIONS) {
				refSet.clear(NUM_BEST_SOLUTIONS, refSet.size());
			}

			List<S> solutions = null;

			List<S> newSolutions = this.constructive.createSolutions(NUM_INITIAL_SOLUTIONS);
			solutions = diversificator.getDiversity(NUM_DIVERSE_SOLUTIONS, newSolutions, refSet.getList());
			refSet.addAll(solutions);

			iter++;

			if (milliseconds == -1 && iter >= globalIterations) {
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	private SSRefreshMemCombinator<S, I> getIfSSRefreshMemCombinator() {

		if (this.combinator instanceof SSRefreshMemCombinator) {
			return (SSRefreshMemCombinator<S, I>) this.combinator;
		} else {
			return null;
		}		
	}

	private Collection<List<S>> createGroupsToCombine() {

		List<Integer> indexesOfNewSolutionsInRefSet = new ArrayList<Integer>();
		for (S s : newRefSetSolutions) {
			indexesOfNewSolutionsInRefSet.add(newRefSetSolutions.indexOf(s));
		}

		return combinations.getGroupsContainingIndexes(indexesOfNewSolutionsInRefSet, refSet.getList());

	}

	@SuppressWarnings("unchecked")
	private void improveAndRefreshRefSet(S solution) {
		if (!improvedSolutions.containsKey(solution)) {
			S original = (S) solution.createCopy();
			this.improvingMethod.improveSolution(solution);
			setIfBestSolution(solution);

			improvedSolutions.put(original, solution);

			refSet.add(solution);
		} else {
			refSet.add(improvedSolutions.get(solution));
		}
	}

	@Override
	public void setInstance(I instance) {
		super.setInstance(instance);
		this.constructive.setInstance(instance);
	};

	@Override
	public void removeInstance() {
		super.removeInstance();
		this.constructive.removeInstance();
	}

}
