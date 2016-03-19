package es.optsicom.lib.approx.improvement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public interface ImprovementMethodListener<S extends Solution<I>, I extends Instance> {

	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod, S newBestSolution);

	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod, double weight);

}
