package es.optsicom.lib.approx.algorithm.vns;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;

public class ReducedVariableNeighbourhoodSearch<S extends Solution<I>, I extends Instance> extends
		VariableNeighbourhoodSearch<S, I> {

	public ReducedVariableNeighbourhoodSearch(Constructive<S, I> constructive, List<Neighbourhood<S, I>> neighbourhoods, int maxIterWoImpr) {
		super(constructive, neighbourhoods, maxIterWoImpr);
	}

	@Override
	protected void exploreNeighbourhood(S solution, ImprovementMethod<S, I> neighbourhood) {
		Neighbourhood<S, I> n = (Neighbourhood<S, I>) neighbourhood; 
		S randomSolution = n.getRandomSolution(solution);
		solution.asSolution(randomSolution);
	}

}
