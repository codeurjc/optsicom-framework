package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.TimeLimitException;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.MathUtil;

public class TabuImprovementMethod<S extends Solution<I>, I extends Instance>
		extends MovementImprovementMethod<S, I> implements MovementManager {

	private Mode mode;

	private double bestIncrement;
	private Object movementAttributes;

	private BestMode bestMode;

	private float tabuTenure;
	private float maxIterWoImpr;
	private Object memory;

	private TabuProblemAdapter<S, I> tabuAdapter;

	private int numIteration;
	private int itersWoImpr;
	private S bestSolution;

	private int tabuTenureInt;

	private double solutionWeight;

	public TabuImprovementMethod(MovementGenerator<S, I> movementGenerator,
			Mode mode, float maxIterWoImpr, float tabuTenure,
			TabuProblemAdapter<S, I> tabuAdapter) {
		super(movementGenerator);
		this.mode = mode;
		this.tabuAdapter = tabuAdapter;
		this.tabuTenure = tabuTenure;
		this.maxIterWoImpr = maxIterWoImpr;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void moreInternalImproveSolution() {

		this.memory = tabuAdapter.createMemory(solution);
		this.tabuTenureInt = tabuAdapter.getTabuTenureInt(solution, tabuTenure);
		int maxItersWoImprInt = tabuAdapter.getMaxItersWoImprInt(solution,
				maxIterWoImpr);

		bestMode = instance.getProblem().getMode();
		numIteration = 1;
		itersWoImpr = 0;

		bestSolution = (S) solution.createCopy();

		do {

			// System.out.println("NumIteration: "+numIteration);
			bestIncrement = 0;
			movementAttributes = null;
			solutionWeight = solution.getWeight();

			try {

				movementGenerator.generateMovements(this);
				applyMovement();

			} catch (FinishGeneratingMovementsException e) {
				applyMovement();
			}

			numIteration++;

			// if(solution.getWeight() != bestSolution.getWeight()){
			// System.out.println("NumIterations Wo Impr: "+itersWoImpr+" limit="+maxItersWoImprInt);
			// }

			if (itersWoImpr >= maxItersWoImprInt) {
				break;
			}

			try{
				checkFinishByTime();
			} catch(TimeLimitException e) {
				break;
			}

		} while (true);

		solution.asSolution(bestSolution);
	}

	private void applyMovement() {

		//System.out.println("---- ApplyMovement: " + (solutionWeight + bestIncrement) + "("+ bestIncrement+") "+ArraysUtil.toStringObj(movementAttributes));
		
		boolean newBestSolutionFound = false;
		if (bestMode.isBetterThan(solutionWeight + bestIncrement,
				bestSolution.getWeight())) {

			improvement = true;
			itersWoImpr = 0;
			newBestSolutionFound = true;
			
			//System.out.println("BestSolFound");

		} else {
			itersWoImpr++;
		}		

		tabuAdapter.markAsTabu(memory, movementAttributes, numIteration,
				tabuTenureInt);

		// if(!bestMode.isImprovement(bestIncrement)){
		// System.out.println(".");
		// }

		//double originalWeight = solutionWeight;
		movementGenerator.applyMovement(movementAttributes);
		
		//System.out.println("---- New Solution: " + solution.getWeight());
		
		// System.out.print(".");

		// System.out.print(solution.getWeight() + ", ");

		// TODO This can be optimized making the copy when the first
		// non-improving
		// movement is applied
		if (newBestSolutionFound) {
			//System.out.println("New solution found: "+solution.getWeight());
			bestSolution = (S) solution.createCopy();
		}

		// TODO Correct implementation test. We need to found a good way
		// to enable or disable this kind of thing in all code.
		// if (!MathUtil.efectiveEquals(originalWeight + bestIncrement,
		// solution.getWeight())) {
		// throw new RuntimeException(
		// "Applying the movement doesn't increment the solution value as expected. It should be "
		// + (originalWeight + bestIncrement)
		// + " and is "
		// + solution.getWeight());
		// }

		newBestSolutionFound(bestSolution);

	}

	@Override
	public void testMovement(double increment, Object movementAttributes) {

		boolean tabuMovement = tabuAdapter.isMarkedAsTabu(memory,
				movementAttributes, numIteration);

		boolean aspirationCriteria = false;

		if (tabuMovement) {
			aspirationCriteria = bestMode.isBetterThan(
					increment + solutionWeight, bestSolution.getWeight());
//			if(aspirationCriteria){
//				System.out.println("AS: "+(increment + solutionWeight)+" > "+bestSolution.getWeight());
//			}
		}

		if (!tabuMovement || aspirationCriteria) {

			if (this.movementAttributes == null
					|| bestMode.isBetterThan(increment, bestIncrement)) {
				this.bestIncrement = increment;
				this.movementAttributes = this.movementGenerator
						.createCopy(movementAttributes);

				// if(!bestMode.isImprovement(increment)){
				// System.out.println("Best non-improving movement: "+increment
				// + " -> "
				// + solution.getWeight() + " [" +
				// Arrays.toString((int[])movementAttributes) + "]");
				// }

			}

			if (mode == Mode.FIRST) {
				if (bestMode.isImprovement(increment)) {
					throw new FinishGeneratingMovementsException();
				}
			}
		}
	}

	@Override
	public void finishMovementGroup() {
		if (mode == Mode.MIXED && bestMode.isImprovement(bestIncrement)) {
			throw new FinishGeneratingMovementsException();
		}
	}

	@Id
	public Mode getMode() {
		return mode;
	}

	@Id
	public float getTabuTenure() {
		return tabuTenure;
	}

	@Id
	public TabuProblemAdapter<S, I> getTabuAdapter() {
		return tabuAdapter;
	}

	@Id
	public float getMaxIterWoImpr() {
		return maxIterWoImpr;
	}
	
	@Override
	public boolean canTestMovement(Object movementAttributes) {
		return true;
	}

}
