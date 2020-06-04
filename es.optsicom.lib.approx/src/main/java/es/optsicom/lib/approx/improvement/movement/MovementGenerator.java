package es.optsicom.lib.approx.improvement.movement;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.RandomList;
import es.optsicom.lib.util.RandomizedFor;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class MovementGenerator<S extends Solution<I>, I extends Instance> implements Descriptive {

	protected S solution;
	protected I instance;

	protected final boolean randomized;

	public MovementGenerator() {
		this.randomized = false;
	}

	public MovementGenerator(boolean randomized) {
		this.randomized = randomized;
	}

	public void startImprovement(S solution, I instance) {
		this.solution = solution;
		this.instance = instance;
	}

	public void finishImprovement(S solution, I instance) {

	}

	public abstract void generateMovements(MovementManager movementManager);

	public abstract void applyMovement(Object movementAttributes);

	public abstract Object createCopy(Object movementAttributes);

	@Id
	public boolean isRandomized() {
		return randomized;
	}

	protected <E> Iterable<E> randomize(List<E> list) {
		if (randomized) {
			return RandomList.create(list);
		} else {
			return list;
		}
	}

	protected RandomizedFor createRandomizedFor(int max) {
		return RandomizedFor.create(randomized, max);
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
