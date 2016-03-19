package es.optsicom.lib.approx.algorithm.vns;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.Id;

public abstract class VariableNeighbourhoodSearch<S extends Solution<I>, I extends Instance>
		extends AbstractApproxMethod<S, I> {

	private Constructive<S, I> constructive;
	private List<? extends ImprovementMethod<S, I>> neighbourhoods;
	private int maxIterations;

	public VariableNeighbourhoodSearch(Constructive<S, I> constructive,
			List<? extends ImprovementMethod<S, I>> improvements, int maxIterWoImpr) {
		this.constructive = constructive;
		this.neighbourhoods = improvements;
		this.maxIterations = maxIterWoImpr;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void internalCalculateSolution(long timeout) {

		long startTime = System.currentTimeMillis();

		S solution = constructive.createSolution();		
		S bestSolutionFound = (S) solution.createCopy();
		setIfBestSolution(bestSolutionFound);
		int iterations = 1;
		global: do {

			int k = 0;
			do {
				exploreNeighbourhood(solution, neighbourhoods.get(k));

				if (solution.isBetterThan(bestSolutionFound)) {
					bestSolutionFound = (S) solution.createCopy();
					setIfBestSolution(bestSolutionFound);
					// System.out.println("Improved on neighbourhood #" + k);
					k = 0;
				} else {
					solution = (S) bestSolutionFound.createCopy();
					k++;
				}

				if (timeout != -1 && System.currentTimeMillis() >= startTime + timeout) {
					break global;
				}

			} while (k < neighbourhoods.size());

			if (iterations >= maxIterations) {
				break;
			}
			iterations++;

			if (timeout != -1 && System.currentTimeMillis() >= startTime + timeout) {
				break;
			}

		} while (true);

	}

	@Id
	public int getMaxIterWithoutImpr() {
		return maxIterations;
	}

	protected abstract void exploreNeighbourhood(S solution, ImprovementMethod<S, I> neighbourhood);

	public void setInstance(I instance) {
		super.setInstance(instance);
		this.constructive.setInstance(instance);
	};
}
