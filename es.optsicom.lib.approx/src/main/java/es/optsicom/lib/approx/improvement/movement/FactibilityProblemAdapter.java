package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface FactibilityProblemAdapter<S extends Solution<I>, I extends Instance> {
	
	public S modifyToBeInfeasible(S solution, int k);
	
	public S returnToFactibility(S solution, int k);

}
