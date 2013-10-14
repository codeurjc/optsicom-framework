package es.optsicom.lib.approx.diversificator;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;

public class SolutionDistanceInfo<D extends DistanceCalculator<S, I>, S extends Solution<I>, I extends Instance> implements Comparable<SolutionDistanceInfo<D, S, I>>{

	private S solution;
	private double minDistance;
	private D distanceCalculator;
		
	public SolutionDistanceInfo(S solution, D distanceCalculator) {
		this.solution = solution;
		this.distanceCalculator = distanceCalculator;
	}
	
	public void addDistanceToSolutions(List<S> solutions) {
		this.minDistance = Integer.MAX_VALUE;
		for (S s : solutions) {
			double distance = distanceCalculator.computeDistance(solution, s);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
	}
	
	@Override
	public int compareTo(SolutionDistanceInfo<D, S, I> o) {
		return (int)(this.minDistance - o.minDistance);
	}
	
	public S getSolution() {
		return solution;
	}

}
