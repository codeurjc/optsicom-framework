/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.approx.algorithm;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Method;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class MemConstructiveImprovement<S extends Solution<I>, I extends Instance> extends AbstractApproxMethod<S, I> {

	public static final String ITERATIONS_PERFORMED_EVENT = "constructiveImprovement.iterationsPerformed";
	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvementMethod;
	private int iterationsPerformed = 0;
	private int iterations = 1;
	private SolutionsMemory<S, I> memory;

	private boolean updateMemoryWithConstructions = true;
	private boolean updateMemoryWithImprovements = true;

	public MemConstructiveImprovement(Constructive<S, I> constructive, ImprovementMethod<S, I> improvementMethod,
			SolutionsMemory<S, I> memory, int iterations) {
		this.constructive = constructive;
		this.improvementMethod = improvementMethod;
		this.iterations = iterations;
		this.memory = memory;
	}

	public MemConstructiveImprovement(Constructive<S, I> constructive, ImprovementMethod<S, I> improvementMethod,
			SolutionsMemory<S, I> memory) {
		this(constructive, improvementMethod, memory, 100);
	}

	public MemConstructiveImprovement(Constructive<S, I> constructive, int iterations) {
		this(constructive, null, null, iterations);
	}

	public MemConstructiveImprovement(Constructive<S, I> constructive) {
		this(constructive, null, null, 100);
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		if (improvementMethod != null) {
			this.improvementMethod.setImprovementMethodListener(this);
		}

		if (duration == -1) {

			constructive.initSolutionCreationByNum(iterations);

			for (int i = 0; i < iterations; i++) {
				S solution = constructive.createSolution();

				if (this.updateMemoryWithConstructions) {
					memory.addSolution(solution);
				}

				setIfBestSolution(solution);

				if (improvementMethod != null) {
					improvementMethod.improveSolution(solution);
					if (this.updateMemoryWithImprovements) {
						memory.addSolution(solution);
					}
				}

				setIfBestSolution(solution);
			}

			iterationsPerformed = iterations;

		} else {

			iterationsPerformed = 0;
			long finishTime = System.currentTimeMillis() + duration;
			constructive.initSolutionCreationByTime(duration);
			do {
				S solution = constructive.createSolution();

				if (this.updateMemoryWithConstructions) {
					memory.addSolution(solution);
				}
				setIfBestSolution(solution);

				if (System.currentTimeMillis() > finishTime) {
					break;
				}

				if (improvementMethod != null) {
					improvementMethod.improveSolution(solution, finishTime - System.currentTimeMillis());
					if (this.updateMemoryWithImprovements) {
						memory.addSolution(solution);
					}
				}

				setIfBestSolution(solution);
				iterationsPerformed++;

				// } while (iterationsPerformed < iterations &&
				// System.currentTimeMillis() < finishTime);
			} while (System.currentTimeMillis() < finishTime);
		}

		if (improvementMethod != null) {
			this.improvementMethod.setImprovementMethodListener(null);
		}

		CurrentExperiment.addEvent(ITERATIONS_PERFORMED_EVENT, iterationsPerformed);

	}

	@Id
	public Constructive<S, I> getConstructive() {
		return constructive;
	}

	public void setConstructive(Constructive<S, I> constructive) {
		this.constructive = constructive;
	}

	@Id
	public ImprovementMethod<S, I> getImprovementMethod() {
		return improvementMethod;
	}

	public void setImprovingMethod(ImprovementMethod<S, I> improvingMethod) {
		this.improvementMethod = improvingMethod;
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	public int getIterationsPerformed() {
		return iterationsPerformed;
	}

	@Override
	public I getInstance() {
		return constructive.getInstance();
	}

	@Override
	public void setInstance(I instance) {
		super.setInstance(instance);
		this.constructive.setInstance(instance);
		this.memory.setInstance(instance);
	}

	@Override
	public void removeInstance() {
		super.removeInstance();
		this.constructive.removeInstance();
	}

	@Id
	public int getMaxConstructions() {
		return iterations;
	}

	public void setMaxConstructions(int maxConstructions) {
		this.iterations = maxConstructions;
	}

	public static <S extends Solution<I>, I extends Instance> Method<S, I> create(Constructive<S, I> constructive) {
		return new MemConstructiveImprovement<S, I>(constructive);
	}

	public static <S extends Solution<I>, I extends Instance> Method<S, I> create(Constructive<S, I> constructive,
			ImprovementMethod<S, I> improvement, SolutionsMemory<S, I> memory) {
		return new MemConstructiveImprovement<S, I>(constructive, improvement, memory);
	}

	public static <S extends Solution<I>, I extends Instance> Method<S, I> create(Constructive<S, I> constructive,
			ImprovementMethod<S, I> improvement, SolutionsMemory<S, I> memory, int numIterations) {
		return new MemConstructiveImprovement<S, I>(constructive, improvement, memory, numIterations);
	}

	public static <S extends Solution<I>, I extends Instance> Method<S, I> create(Constructive<S, I> constructive,
			int numIterations) {
		return new MemConstructiveImprovement<S, I>(constructive, numIterations);
	}

	public boolean isUpdateMemoryWithConstructions() {
		return updateMemoryWithConstructions;
	}

	public void setUpdateMemoryWithConstructions(boolean updateMemoryWithConstructions) {
		this.updateMemoryWithConstructions = updateMemoryWithConstructions;
	}

	public boolean isUpdateMemoryWithImprovements() {
		return updateMemoryWithImprovements;
	}

	public void setUpdateMemoryWithImprovements(boolean updateMemoryWithImprovements) {
		this.updateMemoryWithImprovements = updateMemoryWithImprovements;
	}

}
