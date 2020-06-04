package es.optsicom.lib.approx.diversificator;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface DistanceCalculator<S extends Solution<I>, I extends Instance> {

	double computeDistance(S solA, S solB);

}
