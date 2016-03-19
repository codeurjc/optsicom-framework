package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface TenureProblemAdapter<S extends Solution<I>, I extends Instance> {

	public int getTenure(S solution, Object movementAttributes, int numIteration, int iterationsWithoutImprovement);

	public void finishIteration(S solution, int numIteration, int iterationsWithoutImprovement);

}
