package es.optsicom.lib.multiobjective;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.BestMode;

public interface MultiObjectiveSolution {

	public double getWeight(int index);
	
	public BestMode getBestMode(int index);

	public boolean isDominated(MultiObjectiveSolution ps);
	
}
