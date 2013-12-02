package es.optsicom.lib.multiobjective;

import es.optsicom.lib.util.BestMode;

public interface MultiObjectiveSolution {

	public double getWeight(int index);
	
	public BestMode getBestMode(int index);

	public boolean isDominated(MultiObjectiveSolution ps);
	
}
