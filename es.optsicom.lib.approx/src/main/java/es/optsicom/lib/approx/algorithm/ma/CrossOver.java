package es.optsicom.lib.approx.algorithm.ma;

public interface CrossOver<S, I> {

	public S createSolution(S solutionA, S solutionB);

}
