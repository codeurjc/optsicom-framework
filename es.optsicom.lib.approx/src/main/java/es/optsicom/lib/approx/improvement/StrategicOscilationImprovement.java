package es.optsicom.lib.approx.improvement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.movement.FactibilityProblemAdapter;
import es.optsicom.lib.util.Id;

public class StrategicOscilationImprovement<S extends Solution<I>, I extends Instance>
		extends AbstractImprovementMethod<S, I> implements ImprovementMethodListener<S, I> {

	private ImprovementMethod<S, I> improvementMethod;
	private int maxK;
	private FactibilityProblemAdapter<S, I> factibilityAdapter;

	public StrategicOscilationImprovement(
			ImprovementMethod<S, I> improvementMethod, int maxK,
			FactibilityProblemAdapter<S, I> factibilityAdapter) {
		this.improvementMethod = improvementMethod;
		this.maxK = maxK;
		this.factibilityAdapter = factibilityAdapter;
	}

	@Id
	public ImprovementMethod<S, I> getImprovementMethod() {
		return improvementMethod;
	}

	@Id
	public int getMaxK() {
		return maxK;
	}

	@Id
	public FactibilityProblemAdapter<S, I> getFactibilityAdapter() {
		return factibilityAdapter;
	}

	@Override
	public boolean internalImproveSolution(S solution, long duration) {

		S originalSolution = solution;

		int k = 1;

		this.improvementMethod.setImprovementMethodListener(this);		
		boolean improved = improvementMethod.improveSolution(solution, duration);
		this.improvementMethod.setImprovementMethodListener(null);
		
		checkFinishByTime();

		S bestSolution = (S) solution.createCopy();

		do {

			S infactSolution = factibilityAdapter.modifyToBeInfeasible(solution, k);

			//System.out.println("Infactible k="+k);
			
			improvementMethod.improveSolution(infactSolution,
					getRemainigDuration());

			//System.out.println("Improved(k="+k+"): "+infactSolution.getWeight());
			
			try {
				checkFinishByTime();
			} catch (TimeLimitException e) {
				originalSolution.asSolution(bestSolution);
				throw e;
			}

			solution = factibilityAdapter.returnToFactibility(infactSolution, k);

			//System.out.println("Returned to factibility: "+solution.getWeight());
			
			try {
				checkFinishByTime();
			} catch (TimeLimitException e) {
				originalSolution.asSolution(bestSolution);
				throw e;
			}

			this.improvementMethod.setImprovementMethodListener(this);			
			improved |= improvementMethod.improveSolution(solution,
					getRemainigDuration());
			this.improvementMethod.setImprovementMethodListener(null);
			
			//System.out.println("Improved: "+solution.getWeight());
			
			if (solution.isBetterThan(bestSolution)) {
				bestSolution = (S) solution.createCopy();
				k = 1;
				//System.out.println("BestSolution. k="+1);
			} else {
				k++;
				//System.out.println("No BestSolution. k="+k);
			}

			try {
				checkFinishByTime();
			} catch (TimeLimitException e) {
				originalSolution.asSolution(bestSolution);
				throw e;
			}

		} while (k <= maxK);

		originalSolution.asSolution(bestSolution);

		//System.out.println("------------------------------------------");
		
		return improved;
	}

	@Override
	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod,
			S newBestSolution) {
		newBestSolutionFound(newBestSolution);		
	}

	@Override
	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod,
			double weight) {
		newBestSolutionFound(weight);		
	}
	
}
