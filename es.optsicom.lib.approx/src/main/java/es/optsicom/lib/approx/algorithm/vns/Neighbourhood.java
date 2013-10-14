package es.optsicom.lib.approx.algorithm.vns;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.ImprovementMethod;

public interface Neighbourhood<S extends Solution<I>, I extends Instance> extends ImprovementMethod<S, I> { 

	public S getRandomSolution(S solution);
	
}
