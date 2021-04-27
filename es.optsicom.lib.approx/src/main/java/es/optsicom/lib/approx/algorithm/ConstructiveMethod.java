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
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class ConstructiveMethod<S extends Solution<I>, I extends Instance> extends AbstractApproxMethod<S, I> {

	public static final String ITERATIONS_PERFORMED_EVENT = "constructiveImprovement.iterationsPerformed";
	public static final String ITERATIONS_FEASIBLE_EVENT = "constructiveImprovement.feasibleSolutions";
	public static final String ITERATIONS_INFEASIBLE_EVENT = "constructiveImprovement.infeasibleSolutions";
	
	private Constructive<S, I> constructive;
	private int numIterations = 0;
	private int maxConstructions = 1;
	
	private int feasibleSolutions = 0;
	private int infeasibleSolutions = 0;
	
	public ConstructiveMethod(Constructive<S, I> constructive, int maxConstructions) {
		this.constructive = constructive;
		this.maxConstructions = maxConstructions;
	}

	public ConstructiveMethod(Constructive<S, I> constructive) {
		this(constructive, 1);
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		// Clean feasible and infeasible solutions
		feasibleSolutions = 0;
		infeasibleSolutions = 0;
		
		if (duration == -1) {

			for (int i = 0; i < maxConstructions; i++) {
				S solution = constructive.createSolution();
				setIfBestSolution(solution);
				
				// Count feasible and infeasible solutions
				if (solution != null && solution.isFeasible()) {
					feasibleSolutions++;
				} else {
					infeasibleSolutions++;
				}
			}

			numIterations = maxConstructions;

		} else {

			numIterations = 0;
			long finishTime = System.currentTimeMillis() + duration;
			constructive.initSolutionCreationByTime(duration);
			do {
				S solution = constructive.createSolution();

				setIfBestSolution(solution);
				numIterations++;
				
				// Count feasible and infeasible solutions
				if (solution != null && solution.isFeasible()) {
					feasibleSolutions++;
				} else {
					infeasibleSolutions++;
				}

			} while (System.currentTimeMillis() < finishTime);
		}
		
		if (bestSolution == null) {
			setNullSolution();
		}

		CurrentExperiment.addEvent(ITERATIONS_PERFORMED_EVENT, numIterations);
		CurrentExperiment.addEvent(ITERATIONS_FEASIBLE_EVENT, feasibleSolutions);
		CurrentExperiment.addEvent(ITERATIONS_INFEASIBLE_EVENT, infeasibleSolutions);

	}

	@Id
	public Constructive<S, I> getConstructive() {
		return constructive;
	}

	public void setConstructive(Constructive<S, I> constructive) {
		this.constructive = constructive;
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	public int getNumIterations() {
		return numIterations;
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

	public void setMaxConstructions(int maxConstructions) {
		this.maxConstructions = maxConstructions;
	}
}
