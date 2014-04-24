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
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class ConstructiveImprovementShake<S extends Solution<I>, I extends Instance>
		extends AbstractApproxMethod<S, I> {

	public static final String ITERATIONS_PERFORMED_EVENT = "constructiveImprovement.iterationsPerformed";
	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvementMethod;
	private int iterationsPerformed = 0;
	private int iterations = 1;
	private Shaker<S, I> shaker;

	public ConstructiveImprovementShake(Constructive<S, I> constructive,
			ImprovementMethod<S, I> improvementMethod, Shaker<S, I> shaker,
			int iterations) {
		this.shaker = shaker;
		this.constructive = constructive;
		this.improvementMethod = improvementMethod;
		this.iterations = iterations;
	}

	public ConstructiveImprovementShake(Constructive<S, I> constructive,
			ImprovementMethod<S, I> improvementMethod, Shaker<S, I> shaker) {
		this(constructive, improvementMethod, shaker, 100);
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		if (improvementMethod != null) {
			this.improvementMethod.setImprovementMethodListener(this);
		}

		constructive.initSolutionCreationByNum(1);
		S solution = constructive.createSolution();

		if (duration == -1) {

			for (int i = 0; i < iterations; i++) {

				setIfBestSolution(solution);

				if (improvementMethod != null) {
					improvementMethod.improveSolution(solution);
				}

				setIfBestSolution(solution);

				shaker.shake(solution);
			}

			iterationsPerformed = iterations;

		} else {

			iterationsPerformed = 0;
			long finishTime = System.currentTimeMillis() + duration;

			do {

				// System.out.println("C: "+solution);

				setIfBestSolution(solution);

				if (System.currentTimeMillis() > finishTime) {
					break;
				}

				if (improvementMethod != null) {
					improvementMethod.improveSolution(solution, finishTime
							- System.currentTimeMillis());
					// System.out.println("I: "+solution);
				}

				setIfBestSolution(solution);
				iterationsPerformed++;

				if (iterationsPerformed == iterations) {
					break;
				}

				shaker.shake(solution);

			} while (System.currentTimeMillis() < finishTime);
		}

		if (improvementMethod != null) {
			this.improvementMethod.setImprovementMethodListener(null);
		}

		CurrentExperiment.addEvent(ITERATIONS_PERFORMED_EVENT,
				iterationsPerformed);
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
	
	@Id
	public Shaker<S, I> getShaker() {
		return shaker;
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

}
