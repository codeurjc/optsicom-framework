/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.approx.algorithm.ss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.combinator.Combinator;
import es.optsicom.lib.approx.constructive.Constructive;
import es.optsicom.lib.approx.diversificator.Diversificator;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.BiggestInvertSortLimitedList;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.LowestSortLimitedList;
import es.optsicom.lib.util.SortedLimitedList;

public class ScatterSearch<S extends Solution<I>, I extends Instance> extends AbstractApproxMethod<S, I> {

	public enum RegenerationMode {
		GENERATE_AND_SELECT_DIVERSE, GENERATE_NECESSARY, GENERATE_IF_NOT_ENOUGH
	}

	public enum ImpMode {
		WITH_IMPR, WITHOUT_IMPR, WI_ONLY_BEST
	}

	private int numBestSolutionsToImprove = 5;

	private ImpMode impMode = ImpMode.WITH_IMPR;

	private int numInitialSolutions = 100;

	private ScatterSearchListener<S, I> scatterSearchListener;

	private long finishTime;

	private Constructive<S, I> constructive;
	private ImprovementMethod<S, I> improvingMethod = null;
	private Diversificator<S> diversificator = null;
	private Combinator<S, I> combinator = null;

	private int numBestSolutions = 5;
	private int numDiverseSolutions = 5;

	private int numIterations = 0;
	private int maxNumIterations = 100;

	private Collection<S> initialSolutions;

	private SortedLimitedList<S> refSet;

	private List<S> refSetCombinations;

	private List<S> newRefSetSolutions;

	private SSCombinationsGen combinations;

	private boolean filtered = false;
	private int numTotalImprovements = 0;
	private int numFilteredImprovements = 0;

	private Map<S, S> improvedSolutions;

	private RegenerationMode regenerationMode = RegenerationMode.GENERATE_AND_SELECT_DIVERSE;

	private int numSolutions = 0;

	private boolean removeUsed = false;

	public int getNumSolutions() {
		return numSolutions;
	}

	public SSCombinationsGen getCombinations() {
		return combinations;
	}

	public void setCombinations(SSCombinationsGen combinations) {
		this.combinations = combinations;
	}

	public int getNumBestSolutions() {
		return numBestSolutions;
	}

	public void setNumBestSolutions(int bestSolutions) {
		this.numBestSolutions = bestSolutions;
	}

	public int getNumDiverseSolutions() {
		return numDiverseSolutions;
	}

	public void setNumDiverseSolutions(int diverseSolutions) {
		this.numDiverseSolutions = diverseSolutions;
	}
	
	private SortedLimitedList<S> createSortedLimitedList(int numSolutions) {
		if(this.instance.getProblem().getMode() == BestMode.MAX_IS_BEST) {
			return new BiggestInvertSortLimitedList<S>(numSolutions);
		} 
		return new LowestSortLimitedList<S>(numSolutions);
	}

	@Override
	public void internalCalculateSolution(long milliseconds) {

		numSolutions = 0;

		if (filtered) {
			improvedSolutions = new HashMap<S, S>();
		}

		if (combinations == null) {
			this.combinations = new SSCombinationsGen(2, getRefSetSize());
		}

		// this.source = new Source(numBestSolutions + numDiverseSolutions);
		if (milliseconds == -1) {
			this.finishTime = Long.MAX_VALUE;
		} else {
			this.finishTime = System.currentTimeMillis() + milliseconds;
		}

		// System.out.println("create initial solutions");

		if (removeUsed) {
			// TODO: Aquí habría que utilizar un HashSet, el problema es que el
			// HashSet utiliza internamente
			// un HashMap, y asocia el objeto a la clave utilizando su hashCode.
			// Si la solución cambia
			// (por ejemplo, como resultado de un procedimiento de mejora) la
			// asociación ya no es válida:
			// al ir a eliminarla del Set, se le pregunta su hashCode, como no
			// coincide con el de antes, no
			// se encuentra la solución en el HashMap, y acabamos con soluciones
			// duplicadas.
			// Mica plantea como solución utilizar arrays (dado que se conoce el
			// número de elementos que
			// vamos a tener) y tener una propiedad en los objetos de tipo
			// solución donde poder dejar
			// la posición en el array de dicho objeto.
			this.initialSolutions = new ArrayList<S>();
		} else {
			this.initialSolutions = new ArrayList<S>();
		}

		this.constructive.initSolutionCreationByNum(numInitialSolutions);
		for (int i = 0; i < numInitialSolutions; i++) {
			S solution = this.constructive.createSolution();

			if (this.initialSolutions.add(solution)) {
				setIfBestSolution(solution);
			}
		}

		numSolutions += initialSolutions.size();

		// System.out.println("created initial solutions");

		if (this.scatterSearchListener != null) {
			scatterSearchListener.initialSolutionsCreated(this);
		}

		System.out.print("%1");
		if (System.currentTimeMillis() > this.finishTime) {
			return;
		}
		
		refSet = createSortedLimitedList(numBestSolutions);
		// Debug.debugln("created refSet");

		switch (impMode) {
			case WITH_IMPR: {
				// Debug.debugln("starting to improve all initial solutions");

				improveAndRefreshRefSet(initialSolutions);

				// Debug.debugln("improved all initial solutions");
				if (this.scatterSearchListener != null) {
					scatterSearchListener.initialSolutionsImproved(this);
				}

				break;
			}
			case WITHOUT_IMPR: {
				refSet.addAll(initialSolutions);
				break;
			}
			case WI_ONLY_BEST: {
				// Debug.debugln("improve only best initial solutions");

				SortedLimitedList<S> bestSolutionsSL = createSortedLimitedList(numBestSolutionsToImprove);

				bestSolutionsSL.addAll(initialSolutions);

				List<S> bestSolutions = bestSolutionsSL.getList();

				improveAndRefreshRefSet(bestSolutions);

				// System.out.println("improved only best initial solutions");

				if (this.scatterSearchListener != null) {
					scatterSearchListener.initialSolutionsImproved(this);
				}

				refSet.addAll(bestSolutions);

				break;
			}
		}
		
		System.out.print("%2");		
		if (System.currentTimeMillis() > this.finishTime) {
			return;
		}
		
		// OJO. Esto lo usa Patxi pero yo no.. La idea es que hay soluciones que
		// se pueden seguir
		// considerando diversas.
		if (removeUsed) {
			initialSolutions.removeAll(refSet.getList());
		}

		// setIfBestSolution(refSet.getBiggest());

		numIterations = 0;

		refSet.setMaxSize(numBestSolutions + numDiverseSolutions);

		// System.out.println("add diversity to refset");

		refSet.addAll(diversificator.getDiversity(numDiverseSolutions, initialSolutions, refSet.getList()));

		// System.out.println("Diverse solutions added");

		int globalIters = 0;
		global: while (true) {

			globalIters++;
			
			// OJO: Hay que incluir el nuevo mecanismo de filtrado de soluciones
			// q ya están en el RS
			// refreshRefSetIndexes();
			// showRefSet();

			if (this.scatterSearchListener != null) {
				scatterSearchListener.refSetCreated(this);
			}

			// System.out.println("combine refset solutions");

			refSetCombinations = combinator.combineSolutions(refSet.getList());
			for (S solution : refSetCombinations) {
				setIfBestSolution(solution);
			}

			if (this.scatterSearchListener != null) {
				scatterSearchListener.refSetCombined(this);
			}

			do {

				if(globalIters==1){
					//System.out.print("#"+(numIterations));
				}
				
				numIterations++;
				if (numIterations > maxNumIterations) {
					break;
				}

				// Mecanismo que permite crear métodos de combinación con
				// memoria que se actualiza en eventos del SS
				if (this.combinator instanceof SSRefreshMemCombinator) {
					SSRefreshMemCombinator<S, I> comb = (SSRefreshMemCombinator<S, I>) this.combinator;
					for (S solution : refSetCombinations) {
						comb.refreshMemory(solution);
					}
				}

				numSolutions += refSetCombinations.size();

				if (this.scatterSearchListener != null) {
					scatterSearchListener.initIteration(this);
				}

				List<S> oldRefSet = new ArrayList<S>(refSet.getList());

				switch (impMode) {
					case WITH_IMPR: {
						// Debug
						// .debugln("starting to improve all combinated
						// solutions");

						improveAndRefreshRefSet(refSetCombinations);

						// Debug.debugln("improved all combinated solutions");

						if (this.scatterSearchListener != null) {
							scatterSearchListener.refSetCombinationsImproved(this);
						}
						break;
					}
					case WITHOUT_IMPR:
						break;
					case WI_ONLY_BEST: {

						// System.out.println("starting to improve only best
						// combinated solutions");

						SortedLimitedList<S> bestSolutionsSL = createSortedLimitedList(numBestSolutionsToImprove);

						bestSolutionsSL.addAll(refSetCombinations);

						refSetCombinations = bestSolutionsSL.getList();

						improveAndRefreshRefSet(refSetCombinations);

						// System.out.println("improve only best combinated
						// solutions");

						if (this.scatterSearchListener != null) {
							scatterSearchListener.refSetCombinationsImproved(this);
						}

						break;
					}
				}

				// OJO: Hay que incluir el nuevo mecanismo de filtrado de
				// soluciones q ya están en el RS
				// refreshRefSetIndexes();
				// showRefSet();

				if (System.currentTimeMillis() > this.finishTime) {
					break global;
				}

				if (this.scatterSearchListener != null) {
					scatterSearchListener.refSetRefreshed(this);
				}

				// setIfBestSolution(refSet.getBiggest());

				newRefSetSolutions = new ArrayList<S>(refSet.getList());
				newRefSetSolutions.removeAll(oldRefSet);

				oldRefSet.retainAll(refSet.getList());

				// Debug.debugln("New solutions in refSet: "
				// + newRefSetSolutions.size());

				if (newRefSetSolutions.size() == 0) {
					break;
				}

				if (this.scatterSearchListener != null) {
					scatterSearchListener.newRefSetSolCalculated(this);
				}

				// OJO: Aqui habría que poner el filtro de las combinaciones ya
				// realizadas.
				// Debug.debug("New refSet Solutions positions: ");
				// List<Integer> indexes = new ArrayList<Integer>();
				// for (S sol : newRefSetSolutions) {
				// //Debug.debug(sol.getAscendant().getRefSetPosition() + ",");
				// indexes.add(sol.getAscendant().getRefSetPosition());
				// }
				// Debug.debugln("");

				// Sólo combinamos las nuevas
				// OJO: Esto se podría hacer de forma más eficiente.
				List<Integer> indexesOfNewSolutionsInRefSet = new ArrayList<Integer>();
				for (S s : newRefSetSolutions) {
					indexesOfNewSolutionsInRefSet.add(newRefSetSolutions.indexOf(s));
				}

				refSetCombinations = new ArrayList<S>();
				for (List<S> group : createGroupsToCombine()) {
					S solution = combinator.combineGroup(group);
					refSetCombinations.add(solution);
					setIfBestSolution(solution);
				}

				if (this.scatterSearchListener != null) {
					scatterSearchListener.refSetCombined(this);
				}

			} while (true);

			//System.out.print("%3");
			if (System.currentTimeMillis() > this.finishTime) {
				break global;
			}

			if (refSet.size() > numBestSolutions) {
				refSet.clear(numBestSolutions, refSet.size());
			}

			List<S> solutions = null;

			switch (regenerationMode) {
				case GENERATE_AND_SELECT_DIVERSE:

					List<S> newSolutions = this.constructive.createSolutions(numInitialSolutions);
					solutions = diversificator.getDiversity(numDiverseSolutions, newSolutions, refSet.getList());
					numSolutions += newSolutions.size();

					break;
				case GENERATE_NECESSARY:
					solutions = this.constructive.createSolutions(numDiverseSolutions);
					numSolutions += solutions.size();
					break;
				case GENERATE_IF_NOT_ENOUGH:
					// if (initialSolutions.size() < numDiverseSolutions) {
					// List<S> aux = new ArrayList<S>();
					// for (S solution : initialSolutions) {
					// if (!aux.contains(solution)) {
					// aux.add(solution);
					// }
					// }
					// initialSolutions = aux;
					// }
					while (initialSolutions.size() < numDiverseSolutions) {
						S newSolution = constructive.createSolution();
						if (!initialSolutions.contains(newSolution)) {
							numSolutions++;
							initialSolutions.add(newSolution);
						}
					}
					solutions = diversificator.getDiversity(numDiverseSolutions, initialSolutions, refSet.getList());
					if (solutions.size() < numDiverseSolutions) {
						solutions.addAll(this.constructive.createSolutions(numDiverseSolutions - solutions.size()));
						numSolutions += solutions.size();
					}
					break;
			}

			// Debug.debugln("New solutions created: " + solutions.size());
			// int oldSolutions = refSet.getList().size();
			refSet.addAll(solutions);
			// Debug.debugln("New solutions added: " +
			// (refSet.getList().size() - oldSolutions));

			if (scatterSearchListener != null) {
				scatterSearchListener.refSetReconstructed(this);
			}

			// OJO, esto lo hace Patxi, yo no.
			if (removeUsed) {
				initialSolutions.removeAll(refSet.getList());
			}
		}

		//		Log.debugln("Iterations " + numIterations);

		if (this.scatterSearchListener != null) {
			scatterSearchListener.calculationFinished(this);
		}
	}

	private Collection<List<S>> createGroupsToCombine() {

		List<Integer> indexesOfNewSolutionsInRefSet = new ArrayList<Integer>();
		for (S s : newRefSetSolutions) {
			indexesOfNewSolutionsInRefSet.add(newRefSetSolutions.indexOf(s));
		}

		return combinations.getGroupsContainingIndexes(indexesOfNewSolutionsInRefSet, refSet.getList());

	}

	// private void showRefSet() {
	// Log.debugln("RefSet: ");
	// for (S solution : refSet.getList()) {
	// Log.debugln(solution.getWeight() /* + ": " + solution.getAscendant() */);
	// }
	// Log.debugln("");
	// }

	@SuppressWarnings("unchecked")
	private void improveAndRefreshRefSet(Collection<S> solutions) {
		if (filtered) {
			for (S solution : solutions) {
				numTotalImprovements++;
				// Debug.debug("Improve Solution: " + solution.getWeight()
				// + " -> ");
				if (!improvedSolutions.containsKey(solution)) {
					// long now = System.currentTimeMillis();
					S original = (S) solution.createCopy();
					
					//this.improvingMethod.improveSolution(solution, finishTime - System.currentTimeMillis());
					this.improvingMethod.improveSolution(solution);
					
					// Debug.debugln(solution.getWeight()+" in
					// "+(System.currentTimeMillis()-now)+" millis");
					setIfBestSolution(solution);
					numSolutions += this.improvingMethod.getLastImprovVisitedSolutions();
					improvedSolutions.put(original, solution);
					refSet.add(solution);
				} else {
					// Debug.debugln("Filtered");

					// Debug.debugln("New Solution: "+solution);
					// MDPSolution oldKey = null;
					// for(MDPSolution sol: improvedSolutions.keySet()){
					// if(solution.equals(sol)){
					// oldKey = sol;
					// break;
					// }
					// }
					//					
					// Debug.debugln("Old Solution: "+oldKey);

					numFilteredImprovements++;
					refSet.add(improvedSolutions.get(solution));
				}
				
				if (System.currentTimeMillis() > this.finishTime) {
					return;
				}

			}
		} else {
			for (S solution : solutions) {
				// Debug.debug("Improve Solution: " + solution.getWeight()
				// + " -> ");
				this.improvingMethod.improveSolution(solution, finishTime - System.currentTimeMillis());
				setIfBestSolution(solution);
				numSolutions += this.improvingMethod.getLastImprovVisitedSolutions();
				// Debug.debugln(solution.getWeight());
				
				if (System.currentTimeMillis() > this.finishTime) {
					return;
				}
			}
			refSet.addAll(solutions);
		}
	}

	@Id
	public int getRefSetSize() {
		return numBestSolutions + numDiverseSolutions;
	}

	// private void refreshRefSetIndexes() {
	// List<S> refSetList = refSet.getList();
	// int index = 0;
	// for (S sol : refSetList) {
	// Ascendant parentInf = sol.getAscendant();
	// if (parentInf == null) {
	// parentInf = new Ascendant(numBestSolutions + numDiverseSolutions);
	// sol.setAscendant(parentInf);
	// }
	// parentInf.setRefSetPosition(index);
	// parentInf.incAge();
	// index++;
	// }
	// }

	public void setScatterSearchMDPListener(ScatterSearchListener<S, I> scatterSearchMDPListener) {
		this.scatterSearchListener = scatterSearchMDPListener;
		setSolutionCalculatorListener(scatterSearchMDPListener);
	}

	@Id
	public Diversificator<S> getDiversificator() {
		return diversificator;
	}

	public void setDiversificator(Diversificator<S> diversificator) {
		this.diversificator = diversificator;
	}

	@Id
	public Combinator<S, I> getCombinator() {
		return combinator;
	}

	public void setCombinator(Combinator<S, I> combinator) {
		this.combinator = combinator;
	}

	public int getNumIterations() {
		return numIterations;
	}

	@Id
	public Constructive<S, I> getConstructive() {
		return constructive;
	}

	public void setConstructive(Constructive<S, I> constructive) {
		this.constructive = constructive;
	}

	@Id
	public ImprovementMethod<S, I> getImprovingMethod() {
		return improvingMethod;
	}

	public void setImprovingMethod(ImprovementMethod<S, I> improvingMethod) {
		this.improvingMethod = improvingMethod;
	}

	@Id
	public int getNumInitialSolutions() {
		return numInitialSolutions;
	}

	public void setNumInitialSolutions(int numInitialSolutions) {
		this.numInitialSolutions = numInitialSolutions;
	}

	public Collection<S> getInitialSolutions() {
		return initialSolutions;
	}

	public List<S> getNewRefSetSolutions() {
		return newRefSetSolutions;
	}

	public SortedLimitedList<S> getRefSet() {
		return refSet;
	}

	public List<S> getRefSetCombinations() {
		return refSetCombinations;
	}

	// @Override
	// public I getInstance() {
	// return instance;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.optsicom.AbstractSolutionCalculator#setInstance(es.optsicom.Instance)
	 */
	@Override
	public void setInstance(I instance) {
		super.setInstance(instance);
		this.constructive.setInstance(instance);
		if (this.scatterSearchListener != null) {
			this.scatterSearchListener.setInstance(this);
		}
	}

	public void setImpMode(ImpMode impMode) {
		this.impMode = impMode;
	}

	@Id
	public boolean isFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	public int getNumFilteredImprovements() {
		return numFilteredImprovements;
	}

	public int getNumTotalImprovements() {
		return numTotalImprovements;
	}

	@Id
	public RegenerationMode getRegenerationMode() {
		return regenerationMode;
	}

	public void setRegenerationMode(RegenerationMode regenerationMode) {
		this.regenerationMode = regenerationMode;
	}

	public ImpMode getImpMode() {
		return impMode;
	}

	@Id
	public boolean isRemoveUsed() {
		return removeUsed;
	}

	public void setRemoveUsed(boolean removeUsed) {
		this.removeUsed = removeUsed;
	}

	@Override
	public void removeInstance() {
		super.removeInstance();
		this.constructive.removeInstance();
		if (this.scatterSearchListener != null) {
			this.scatterSearchListener.removeInstance(this);
		}
	}

	@Id
	public int getMaxNumIterations() {
		return maxNumIterations;
	}

	public void setMaxNumIterations(int maxNumIterations) {
		this.maxNumIterations = maxNumIterations;
	}

	@Id
	public int getNumBestSolutionsToImprove() {
		return numBestSolutionsToImprove;
	}

	public void setNumBestSolutionsToImprove(int numBestSolutionsToImprove) {
		this.numBestSolutionsToImprove = numBestSolutionsToImprove;
	}
	
	
}
