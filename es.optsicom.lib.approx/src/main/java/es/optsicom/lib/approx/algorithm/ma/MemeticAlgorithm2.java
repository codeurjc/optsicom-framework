package es.optsicom.lib.approx.algorithm.ma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.algorithm.ConstructiveMethod;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.RandomIterator;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.lib.util.RandomizedSelector;
import es.optsicom.lib.util.RandomizedSelector.Proportionality;

/**
 * Memetic Algorithm described in Fan2010 (MDGP Problem)
 */
public class MemeticAlgorithm2<S extends Solution<I>, I extends Instance>
		extends AbstractApproxMethod<S, I> {

	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvement;
	private int ps = 30;
	private CrossOver<S, I> crossOver;
	private Repairer<S, I> repairer;
	private Mutator<S, I> mutator;
	private double mutationProb = 0.01;
	private int numGenerations = 20;

	public MemeticAlgorithm2(Constructive<S, I> constructive,
			ImprovementMethod<S, I> improvement, CrossOver<S, I> crossOver,
			Repairer<S, I> repairer, Mutator<S, I> mutator) {
		this.constructive = constructive;
		this.improvement = improvement;
		this.crossOver = crossOver;
		this.repairer = repairer;
		this.mutator = mutator;
	}

	@Override
	protected void internalCalculateSolution(long timeout) {

		if(improvement != null){
			this.improvement.setImprovementMethodListener(this);
		}
		
		long finishTime = -1;
		if(timeout != -1){
			finishTime = System.currentTimeMillis() + timeout;
		}

		List<S> population = new ArrayList<S>();

		for (int i = 0; i < ps; i++) {
			S solution = constructive.createSolution();
			setIfBestSolution(solution);
			population.add(solution);
			if (finishTime != -1 && System.currentTimeMillis() > finishTime) {
				return;
			}
		}

		int numExecGenerations = 0;
		while (true) {
			
			if(finishTime != -1){
				if(System.currentTimeMillis() > finishTime){
					break;
				}								
			} else {
				if(numExecGenerations >= numGenerations){
					break;
				}
				numExecGenerations++;
			}

			List<S> crossPopulation = new ArrayList<S>();

			//Select ps pairs according to rank-based roulette-wheel strategy	
			RandomizedSelector<S> rs = new RandomizedSelector<S>(Proportionality.DIRECTLY);
			Collections.sort(population, Collections.reverseOrder());
			for(int j=0; j<population.size(); j++){
				rs.add(population.get(j), ps-j);
			}
			
			for(int i=0; i<ps; i++){

				S solutionA = rs.selectElement();
				S solutionB = rs.selectElement();
				
				S solution = crossOver.createSolution(solutionA, solutionB);
				
				//System.out.print("CO: "+solutionA.getWeight()+", "+solutionB.getWeight()+" -> "+solution.getWeight()+" - repair -> ");
				
				if(RandomManager.nextDouble() < mutationProb){
					//System.out.print("Mut: "+solution.getWeight()+" -> ");
					mutator.doMutation(solution);
					//System.out.println(solution.getWeight());
				}
				
				repairer.repairSolution(solution);
				
				//System.out.print(solution.getWeight());
				
				if(improvement != null){
					improvement.improveSolution(solution);
				}
				
				//boolean betterThanParents = solution.getWeight() > solutionA.getWeight() && solution.getWeight() > solutionB.getWeight();
				//System.out.println(" -> "+solution.getWeight()+(betterThanParents?"  IMPR":""));

				setIfBestSolution(solution);

				crossPopulation.add(solution);

				if (finishTime != -1 && System.currentTimeMillis() > finishTime) {
					return;
				}
			}

			population = selection(crossPopulation, population);

			//System.out.println("-");
		}
	}

	private List<S> selection(List<S> crossPopulation, List<S> population) {

		Collections.sort(crossPopulation, Collections.reverseOrder());
		Collections.sort(population, Collections.reverseOrder());

		List<S> newPopulation = new ArrayList<S>();

		while (newPopulation.size() < this.ps) {

			if (crossPopulation.size() > 0 && population.size() > 0) {

				S cps = crossPopulation.get(0);
				S ps = population.get(0);

				if (cps.isBetterThan(ps)) {
					crossPopulation.remove(0);
					newPopulation.add(cps);
				} else if (cps.isBetterThan(ps)) {
					population.remove(0);
					newPopulation.add(ps);
				} else {
					crossPopulation.remove(0);
					population.remove(0);
					if (cps.equals(ps)) {
						newPopulation.add(cps);
					} else {
						newPopulation.add(cps);
						newPopulation.add(ps);
					}
				}
			} else if(crossPopulation.size() > 0){
				while(newPopulation.size() < this.ps){
					newPopulation.add(crossPopulation.remove(0));
				}
			} else {
				while(newPopulation.size() < this.ps){
					newPopulation.add(population.remove(0));
				}
			}
		}

		return newPopulation;
	}

	@Override
	public void setInstance(I instance) {
		super.setInstance(instance);
		this.constructive.setInstance(instance);
	}

	public void setNumGenerations(int numGenerations) {
		this.numGenerations = numGenerations;
	}
}
