package es.optsicom.lib.approx.algorithm.vns;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.AbstractImprovementMethod;

public abstract class AbstractNeighbourhood<S extends Solution<I>, I extends Instance> extends AbstractImprovementMethod<S, I> implements Neighbourhood<S, I> {
	
}
