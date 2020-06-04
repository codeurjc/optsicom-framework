package es.optsicom.lib.approx.algorithm.vns;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;

public class VariableNeighbourhoodDescent<S extends Solution<I>, I extends Instance>
		extends VariableNeighbourhoodSearch<S, I> {

	public VariableNeighbourhoodDescent(Constructive<S, I> constructive,
			List<ImprovementMethod<S, I>> improvementMethods, int maxIterWoImpr) {
		super(constructive, improvementMethods, maxIterWoImpr);
	}

	protected void exploreNeighbourhood(S solution, ImprovementMethod<S, I> neighbourhood) {
		neighbourhood.improveSolution(solution);
	};
}
