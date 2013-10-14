package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface TabuProblemAdapter<S extends Solution<I>, I extends Instance> {

	Object createMemory(S solution);

	void markAsTabu(Object memory, Object movementAttributes, int numIteration, int tabuTenure);

	boolean isMarkedAsTabu(Object memory,
			Object movementAttributes, int numIteration);

	int getMaxItersWoImprInt(S solution, float maxIterWoImpr);
	
	int getTabuTenureInt(S solution, float tabuTenure);

}
