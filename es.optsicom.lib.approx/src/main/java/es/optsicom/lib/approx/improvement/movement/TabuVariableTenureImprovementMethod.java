package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.TimeLimitException;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.Id;

public class TabuVariableTenureImprovementMethod<S extends Solution<I>, I extends Instance> extends
		MovementImprovementMethod<S, I> implements MovementManager {

	private final Mode mode;

	private double bestIncrement;
	private Object movementAttributes;

	private BestMode bestMode;

	private final float tabuTenure;
	private final float maxIterWoImpr;
	private Object memory;

	private final TabuProblemAdapter<S, I> tabuAdapter;

	private int numIteration;
	private int itersWoImpr;
	private S bestSolution;

	private final TenureProblemAdapter<S, I> tenureAdapter;

	private double solutionWeight;

	private boolean testTabuMovements = true;

	private long startTime;

	public TabuVariableTenureImprovementMethod(MovementGenerator<S, I> movementGenerator, Mode mode,
			float maxIterWoImpr, float tabuTenure, TabuProblemAdapter<S, I> tabuAdapter,
			TenureProblemAdapter<S, I> tenureAdapter) {
		super(movementGenerator);
		this.mode = mode;
		this.tabuAdapter = tabuAdapter;
		this.tabuTenure = tabuTenure;
		this.tenureAdapter = tenureAdapter;
		this.maxIterWoImpr = maxIterWoImpr;
	}

	@Override
	public boolean internalImproveSolution(S solution, long duration) {
		// Hack to know when algorithm starts
		startTime = System.currentTimeMillis();
		return super.internalImproveSolution(solution, duration);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void moreInternalImproveSolution() {

		this.memory = tabuAdapter.createMemory(solution);
		int maxItersWoImprInt = tabuAdapter.getMaxItersWoImprInt(solution, maxIterWoImpr);

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

			tenureAdapter.finishIteration(solution, numIteration, itersWoImpr);

			// if(solution.getWeight() != bestSolution.getWeight()){
			// System.out.println("NumIterations Wo Impr: "+itersWoImpr+" limit="+maxItersWoImprInt);
			// }

			if (itersWoImpr >= maxItersWoImprInt) {
				break;
			}

			try {
				checkFinishByTime();
			} catch (TimeLimitException e) {
				break;
			}

		} while (true);

		//System.out.println("Iterations: " + numIteration + "; ItersWoImp: " + itersWoImpr);

		solution.asSolution(bestSolution);
	}

	private void applyMovement() {

		// System.out.println("---- ApplyMovement: " + (solutionWeight + bestIncrement) + "("+
		// bestIncrement+") "+ArraysUtil.toStringObj(movementAttributes));

		boolean newBestSolutionFound = false;
		if (bestMode.isBetterThan(solutionWeight + bestIncrement, bestSolution.getWeight())) {

			improvement = true;
			itersWoImpr = 0;
			newBestSolutionFound = true;

			// System.out.println("BestSolFound");

		} else {
			itersWoImpr++;
		}

		// if(!bestMode.isImprovement(bestIncrement)){
		// System.out.println(".");
		// }

		// double originalWeight = solutionWeight;
		movementGenerator.applyMovement(movementAttributes);

		int tenure = tenureAdapter.getTenure(solution, movementAttributes, numIteration, itersWoImpr);

		// System.out.println("    ##-> "+(System.currentTimeMillis()-startTime)+":"+tenure);

		tabuAdapter.markAsTabu(memory, movementAttributes, numIteration, tenure);

		// System.out.println("---- New Solution: " + solution.getWeight());

		// System.out.print(".");

		// System.out.print(solution.getWeight() + ", ");

		// TODO This can be optimized making the copy when the first
		// non-improving
		// movement is applied
		if (solution.isBetterThan(bestSolution)) {
			// System.out.println("New solution found: "+solution.getWeight());
			bestSolution = (S) solution.createCopy();
			newBestSolutionFound(bestSolution);
			// System.out.println(">> "+bestSolution.getWeight());
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

	}

	@Override
	public void testMovement(double increment, Object movementAttributes) {

		boolean tabuMovement = tabuAdapter.isMarkedAsTabu(memory, movementAttributes, numIteration);

		boolean aspirationCriteria = false;

		if (tabuMovement) {
			aspirationCriteria = bestMode.isBetterThan(increment + solutionWeight, bestSolution.getWeight());
			// if(aspirationCriteria){
			// System.out.println("AS: "+(increment + solutionWeight)+" > "+bestSolution.getWeight());
			// }
		}

		if (!tabuMovement || aspirationCriteria) {

			if (this.movementAttributes == null || bestMode.isBetterThan(increment, bestIncrement)) {
				this.bestIncrement = increment;
				this.movementAttributes = this.movementGenerator.createCopy(movementAttributes);

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
	public TenureProblemAdapter<S, I> getTenureAdapter() {
		return tenureAdapter;
	}

	@Id
	public float getMaxIterWoImpr() {
		return maxIterWoImpr;
	}

	public TabuVariableTenureImprovementMethod<S, I> setTestTabuMovements(boolean testTabuMovements) {
		this.testTabuMovements = testTabuMovements;
		return this;
	}

	@Override
	public boolean canTestMovement(Object movementAttributes) {
		if (testTabuMovements) {
			return true;
		} else {
			return !tabuAdapter.isMarkedAsTabu(memory, movementAttributes, numIteration);
		}
	}
}
