package es.optsicom.lib.exact;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;

public class ExactApproxMethod<S extends Solution<I>, I extends Instance> extends AbstractApproxMethod<S,I> {

	private ExactMethod<S,I> exactMethod;

	public ExactApproxMethod(ExactMethod<S,I> exactMethod) {
		this.exactMethod = exactMethod;
	}

	@Override
	protected void internalCalculateSolution(long timeLimit) {

		ExactResult<S> result = exactMethod.execute(instance, timeLimit);

		S solution = result.getSolution();

		if(solution != null){
			setIfBestSolution(solution);
		}
	}
}
