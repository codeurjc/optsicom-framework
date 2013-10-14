package es.optsicom.lib.approx.improvement.movement.info;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.improvement.movement.FinishGeneratingMovementsException;
import es.optsicom.lib.approx.improvement.movement.Mode;
import es.optsicom.lib.approx.improvement.movement.TabuProblemAdapter;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.Id;

public class TabuInfoImprovementMethod<S extends Solution<I>, I extends Instance, IN>
		extends MovementInfoImprovementMethod<S, I, IN> implements
		MovementInfoManager<IN> {

	private Mode mode;

	private float tabuTenure;
	private float maxIterWoImpr;
	private Object memory;

	private TabuProblemAdapter<S, I> tabuAdapter;

	private int numIteration;
	private int itersWoImpr;
	private S bestSolution;

	private int tabuTenureInt;

	private List<IN> testedMovInfo;
	
	private boolean applyLastTestedMovement;

	public TabuInfoImprovementMethod(
			MovementInfoGenerator<S, I, IN> movementGenerator, Mode mode,
			float maxIterWoImpr, float tabuTenure,
			TabuProblemAdapter<S, I> tabuAdapter,
			MovementSelectorByInfo<S, IN> movSelector) {
		super(movementGenerator, movSelector);
		
		if(mode == Mode.MIXED){
			throw new UnsupportedOperationException();
		}
		
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

		numIteration = 1;
		itersWoImpr = 0;

		bestSolution = (S) solution.createCopy();
		
		do {

			testedMovInfo = new ArrayList<IN>();
						
			// System.out.println("NumIteration: "+numIteration);
			applyLastTestedMovement = false;

			movSelector.startNewIteration(solution);

			try {

				movementGenerator.generateMovements(this);
				applyMovement();

			} catch (FinishGeneratingMovementsException e) {
				applyLastTestedMovement = true;
				applyMovement();
			}

			numIteration++;

			// if(solution.getWeight() != bestSolution.getWeight()){
			// System.out.println("NumIterations Wo Impr: "+itersWoImpr+" limit="+maxItersWoImprInt);
			// }

			if (itersWoImpr >= maxItersWoImprInt) {
				break;
			}

			checkFinishByTime();

		} while (true);

		solution.asSolution(bestSolution);
	}

	private void applyMovement() {

		boolean newBestSolutionFound = false;

		IN movToApply;
		if (applyLastTestedMovement) {
			movToApply = testedMovInfo.get(testedMovInfo.size() - 1);
		} else {
			movToApply = movSelector.selectMovementToApply(testedMovInfo);
		}

		//System.out.println("----- "+movToApply);

		if (movSelector.isBetterThan(movToApply, bestSolution)) {

			improvement = true;
			itersWoImpr = 0;
			newBestSolutionFound = true;

		} else {
			itersWoImpr++;
		}

		tabuAdapter.markAsTabu(memory, movToApply, numIteration,
				tabuTenureInt);

		// if(!bestMode.isImprovement(bestIncrement)){
		// System.out.println(".");
		// }

		//double originalWeight = solution.getWeight();
		movementGenerator.applyMovement(movToApply);
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
	public void testMovement(IN info) {

		boolean tabuMovement = tabuAdapter.isMarkedAsTabu(memory,
				info, numIteration);

		boolean aspirationCriteria = false;

		if (tabuMovement) {
			aspirationCriteria = movSelector.isBetterThan(info, bestSolution);
//			if(aspirationCriteria){
//				System.out.println("AS: "+info);
//			}
		}

		if (!tabuMovement || aspirationCriteria) {

			testedMovInfo.add(this.movementGenerator.createCopy(info));
			
			if (mode == Mode.FIRST) {
				if (movSelector.isImprovement(info)) {					
					throw new FinishGeneratingMovementsException();
				}
			} 
		}
	}

	@Override
	public void finishMovementGroup() {
		throw new UnsupportedOperationException();
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

}
