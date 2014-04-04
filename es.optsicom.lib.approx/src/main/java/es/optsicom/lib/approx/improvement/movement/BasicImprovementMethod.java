package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.MathUtil;

public class BasicImprovementMethod<S extends Solution<I>, I extends Instance>
		extends MovementImprovementMethod<S, I> implements MovementManager {
	
	private Mode mode;
	
	private double bestIncrement;
	private Object movementAttributes;

	private BestMode bestMode;

	public BasicImprovementMethod(MovementGenerator<S, I> movementGenerator, Mode mode) {
		super(movementGenerator);
		this.mode = mode;
	}

	@Override
	protected void moreInternalImproveSolution() {
		
		//System.out.print(".");
		
		bestMode = instance.getProblem().getMode();
		
		do {

			bestIncrement = 0;
			movementAttributes = null;

			try {
				
				originalWeight = solution.getWeight();
				movementGenerator.generateMovements(this);
				
				if(mode == Mode.BEST && bestMode.isImprovement(bestIncrement)){
					applyMovement();
				} else {
					break;
				}
				
			} catch (FinishGeneratingMovementsException e){			
				//This will be executed en FIRST or MIXED mode
				applyMovement();
			}
							
			checkFinishByTime();

		} while (true);

	}

	private void applyMovement() {
		
		improvement = true;			
		movementGenerator.applyMovement(movementAttributes);
		
		//TODO Correct implementation test. We need to found a better way
		//to enable or disable this kind of things in all code.
//		if(!MathUtil.efectiveEquals(originalWeight + bestIncrement,solution.getWeight())){
//			throw new RuntimeException("Applying the movement doesn't increment the solution value as expected. It should be "+(originalWeight + bestIncrement)+" and is "+solution.getWeight());
//		}
		
		newBestSolutionFound(solution);
	}

	@Override
	public void testMovement(double increment, Object movementAttributes) {		
				
		//System.out.println("["+solution.getWeight()+"|"+increment+":"+ArraysUtil.toStringObj(movementAttributes)+"]");
		
		if(this.movementAttributes == null || bestMode.isBetterThan(increment, bestIncrement)){
			this.bestIncrement = increment;
			this.movementAttributes = movementGenerator.createCopy(movementAttributes);
		}
		
		if(mode == Mode.FIRST){
			if(bestMode.isImprovement(increment)){
				throw new FinishGeneratingMovementsException();
			}
		}
	}

	@Override
	public void finishMovementGroup() {
		if(mode == Mode.MIXED && bestMode.isImprovement(bestIncrement)){
			throw new FinishGeneratingMovementsException();
		}		
	}

	@Id
	public Mode getMode() {
		return mode;
	}
	
	@Override
	public boolean canTestMovement(Object movementAttributes) {
		return true;
	}
	
}
