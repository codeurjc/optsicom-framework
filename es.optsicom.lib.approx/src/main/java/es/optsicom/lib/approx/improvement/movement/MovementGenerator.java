package es.optsicom.lib.approx.improvement.movement;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class MovementGenerator<S extends Solution<I>, I extends Instance> implements Descriptive {
	
	protected S solution;
	protected I instance;
	
	public void startImprovement(S solution, I instance){
		this.solution = solution;
		this.instance = instance;
	}
	
	public void finishImprovement(S solution, I instance){
		
	}
	
	public abstract void generateMovements(MovementManager movementManager);

	public abstract void applyMovement(Object movementAttributes);
	
	public abstract Object createCopy(Object movementAttributes);
	
	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
