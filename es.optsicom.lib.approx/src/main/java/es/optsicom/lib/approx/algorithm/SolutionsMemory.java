package es.optsicom.lib.approx.algorithm;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface SolutionsMemory<S extends Solution<I>, I extends Instance> {

	public void addSolution(S solution);

	public void setInstance(I instance);

}
