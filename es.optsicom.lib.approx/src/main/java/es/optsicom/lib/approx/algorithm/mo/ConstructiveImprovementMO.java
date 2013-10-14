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
package es.optsicom.lib.approx.algorithm.mo;

import es.optsicom.lib.Instance;

import es.optsicom.lib.Method;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.AbstractImprovementMethod;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExperimentExecution;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class ConstructiveImprovementMO<S extends Solution<I>, I extends Instance> extends AbstractApproxMOMethod<S, I> {

	public static final String ITERATIONS_PERFORMED_EVENT = "constructiveImprovement.iterationsPerformed";
	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvementMethod;
	private int iterationsPerformed = 0;
	private int iterations = 1;

	public ConstructiveImprovementMO(Constructive<S, I> constructive, ImprovementMethod<S, I> improvementMethod, int iterations) {
		this.constructive = constructive;
		this.improvementMethod = improvementMethod;
		this.iterations = iterations;
	}

	public ConstructiveImprovementMO(Constructive<S, I> constructive, ImprovementMethod<S, I> improvementMethod) {
		this(constructive, improvementMethod, 100);
	}

	public ConstructiveImprovementMO(Constructive<S, I> constructive, int iterations) {
		this(constructive, null, iterations);
	}
	
	public ConstructiveImprovementMO(Constructive<S, I> constructive) {
		this(constructive, null, 100);
	}

	@Override
	protected void internalCalculateSolution(long duration) {

		if(improvementMethod != null){
			this.improvementMethod.setImprovementMethodListener(this);
		}
		
		if (duration == -1) {

			constructive.initSolutionCreationByNum(iterations);

			for (int i = 0; i < iterations; i++) {
				System.out.println("\nC");
				S solution = constructive.createSolution();
				//System.out.println("C: "+solution);
				
				if(solution != null) {
					setIfNonDominated(solution);
					
					if(improvementMethod != null){
//						System.out.println("\nI");
						improvementMethod.improveSolution(solution);
						//System.out.println("I: "+solution);
					}
					
					setIfNonDominated(solution);
					
				}
			}

			iterationsPerformed = iterations;

		} else {

			iterationsPerformed = 0;
			long finishTime = System.currentTimeMillis() + duration;
			constructive.initSolutionCreationByTime(duration);
			do {
//				System.out.print("\nC");
				S solution = constructive.createSolution();
				//System.out.println("C: "+solution);

				if(solution != null) {
					setIfNonDominated(solution);

					if (System.currentTimeMillis() > finishTime) {
						break;
					}
					
					if(improvementMethod != null){
//						System.out.print("\nI");
						long oneHour = 1000*60*60;
						try {
							improvementMethod.improveSolution(solution, oneHour);
						} catch(NullPointerException e) {
							
						}
						//System.out.println("I: "+solution);
					}				

					setIfNonDominated(solution);
					iterationsPerformed++;
					
				}

			//} while (iterationsPerformed < iterations && System.currentTimeMillis() < finishTime);
			} while (System.currentTimeMillis() < finishTime);
		}
		
		if(improvementMethod != null){
			this.improvementMethod.setImprovementMethodListener(null);
		}
		
		CurrentExperiment.addEvent(ITERATIONS_PERFORMED_EVENT, iterationsPerformed);

		System.out.println("========Pareto:");
		System.out.println("Num solutions: " + this.bestSolutions.size());
		System.out.println(this.bestSolutions);
		
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
		this.bestSolutions.clear();
	}

	@Override
	public void removeInstance() {
		super.removeInstance();
		this.constructive.removeInstance();
		this.bestSolutions.clear();
	}

	@Id
	public int getMaxConstructions() {
		return iterations;
	}

	public void setMaxConstructions(int maxConstructions) {
		this.iterations = maxConstructions;
	}

	public static <S extends Solution<I>, I extends Instance> Method<S,I> create(Constructive<S,I> constructive) {
		return new ConstructiveImprovementMO<S, I>(constructive);
	}
	
	public static <S extends Solution<I>, I extends Instance> Method<S,I> create(Constructive<S,I> constructive, ImprovementMethod<S,I> improvement) {
		return new ConstructiveImprovementMO<S, I>(constructive,improvement);
	}
	
	public static <S extends Solution<I>, I extends Instance> Method<S,I> create(Constructive<S,I> constructive, ImprovementMethod<S,I> improvement, int numIterations) {
		return new ConstructiveImprovementMO<S, I>(constructive,improvement,numIterations);
	}

	public static <S extends Solution<I>, I extends Instance> Method<S,I> create(Constructive<S,I> constructive, int numIterations) {
		return new ConstructiveImprovementMO<S, I>(constructive,numIterations);
	}
}
