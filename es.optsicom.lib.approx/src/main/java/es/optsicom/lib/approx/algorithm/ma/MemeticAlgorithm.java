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

/**
 * Memetic Algorithm described in KatayamaNarihisa2005 (MDP Problem)
 */
public class MemeticAlgorithm<S extends Solution<I>, I extends Instance>
		extends AbstractApproxMethod<S, I> {

	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvement;
	private int ps = 40;
	private CrossOver<S, I> crossOver;
	private Repairer<S, I> repairer;
	private Mutator<S, I> mutator;
	private boolean diversification = true;

	public MemeticAlgorithm(Constructive<S, I> constructive,
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

		long finishTime = System.currentTimeMillis() + timeout;

		List<S> population = new ArrayList<S>();

		for (int i = 0; i < ps; i++) {
			S solution = constructive.createSolution();
			setIfBestSolution(solution);
			improvement.improveSolution(solution);
			setIfBestSolution(solution);
			population.add(solution);
			if (System.currentTimeMillis() > finishTime) {
				return;
			}
		}

		while (System.currentTimeMillis() < finishTime) {

			List<S> crossPopulation = new ArrayList<S>();

			RandomIterator<S> randomIt = new RandomIterator<S>(population);

			while (randomIt.hasNext()) {

				S solutionA = randomIt.next();
				S solutionB;
				if (randomIt.hasNext()) {
					solutionB = randomIt.next();
				} else {
					break;
				}

				S solution = crossOver.createSolution(solutionA, solutionB);
				repairer.repairSolution(solution);
				improvement.improveSolution(solution);

				setIfBestSolution(solution);

				crossPopulation.add(solution);

				if (System.currentTimeMillis() > finishTime) {
					return;
				}
			}

			population = selection(crossPopulation, population);

			if (diversification) {
				S popBestSolution = population.get(0);
				for (S solution : population) {
					if (solution != popBestSolution) {
						mutator.doMutation(solution);
						repairer.repairSolution(solution);
						improvement.improveSolution(solution);
						setIfBestSolution(solution);
						if (System.currentTimeMillis() > finishTime) {
							return;
						}
					}
				}
			}

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

}
