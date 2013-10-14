package es.optsicom.lib.approx.improvement.movement.info;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.AbstractImprovementMethod;
import es.optsicom.lib.approx.improvement.TimeLimitException;
import es.optsicom.lib.util.Id;

/**
 * This class is very similar to MovementImprovementMethod but differs in the
 * way that "movement value" is managed. In MovementImprovementMethod, all
 * movements have an associated value (indicating the change in objective
 * function). In other hand, in this class, instead of value, a full object is
 * provided for each movement and the user has to provide a function to
 * determine which movement has to be finally applied.
 * 
 * We have decided to copy&paste the entire micro-framework of movements for
 * performance reasons. We have tried to mantain the same interfaces where possible
 * to allow a drop-in replacement.
 * 
 * @author mica
 * 
 * @param <S>
 * @param <I>
 */
public abstract class MovementInfoImprovementMethod<S extends Solution<I>, I extends Instance, IN>
		extends AbstractImprovementMethod<S, I> {

	protected MovementInfoGenerator<S, I, IN> movementGenerator;
	protected S solution;
	protected I instance;
	protected boolean improvement;
	protected double originalWeight;
	protected MovementSelectorByInfo<S, IN> movSelector;

	public MovementInfoImprovementMethod(
			MovementInfoGenerator<S, I, IN> movementGenerator, MovementSelectorByInfo<S, IN> movSelector) {
		this.movementGenerator = movementGenerator;
		this.movSelector = movSelector;
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
	public MovementInfoGenerator<S, I, IN> getMovementGenerator() {
		return movementGenerator;
	}
}
