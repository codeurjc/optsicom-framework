package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.AbstractImprovementMethod;
import es.optsicom.lib.approx.improvement.TimeLimitException;
import es.optsicom.lib.util.Id;

public abstract class MovementImprovementMethod<S extends Solution<I>, I extends Instance>
		extends AbstractImprovementMethod<S, I> {

	protected MovementGenerator<S, I> movementGenerator;
	protected S solution;
	protected I instance;
	protected boolean improvement;
	protected double originalWeight;

	public MovementImprovementMethod(MovementGenerator<S, I> movementGenerator) {
		this.movementGenerator = movementGenerator;
	}

	@Override
	public boolean internalImproveSolution(S solution, long duration) {

		I instance = solution.getInstance();

		this.solution = solution;
		this.originalWeight = solution.getWeight();
		
		this.instance = instance;

		this.movementGenerator.startImprovement(solution, instance);

		try {
			moreInternalImproveSolution();
		} catch (TimeLimitException e) {
		} 

		this.movementGenerator.finishImprovement(solution, instance);

		return improvement;
	}

	protected abstract void moreInternalImproveSolution();

	@Id
	public MovementGenerator<S, I> getMovementGenerator() {
		return movementGenerator;
	}
}
