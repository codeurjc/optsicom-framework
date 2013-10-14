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
package es.optsicom.lib.exact.bb;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.Id;

/**
 * Esta clase implementa el algoritmo de construcción del árbol de Pisinger. Es genérica para un límite de m elementos
 * seleccionados. Esto es debido a que no explora
 * soluciones con más de m nodos seleccionados, bien porque están realmente seleccionados o
 * bien porque el número de nodos no seleccionados es igual a (n-m). 
 * @author mica
 *
 */
public class BinaryTreeBBStandard<S extends Solution<I>, I extends Instance> extends FixedBinaryTreeBB<S, I> {

	private BoundCalcBinaryTreeBBStandard<I> boundCalc = null; //new BoundCalcPisinger();

	private int numUnselectedNodes = 0;
	private boolean reduction = true;

	//	public BinaryTreeBBStandard(SolutionManager<S, I> solutionManager) {
	//		this.solutionManager = solutionManager;
	//	}

	@Override
	protected void internalSolve(I instance, long timeMillis) {

		//System.out.println("ARBOL ------------------------------------------");

		if (timeMillis != -1) {
			timeLimit = System.currentTimeMillis() + timeMillis;
		} else {
			timeLimit = Long.MAX_VALUE;
		}

		this.instance = instance;
		this.n = solutionManager.getNumNodes(instance);
		this.m = solutionManager.getNumSelectedNodes(instance);

		this.numUnselectedNodes = 0;

		this.numVisitedNodes = 0;

		S solution = null;

		if (approxMethod != null) {
			this.approxMethod.setInstance(instance);
			solution = approxMethod.calculateSolution();

			System.out.println("Heuristic solution: " + solution);

			this.bestSolutionWeight = solution.getWeight();
			this.bestSolutionNodes = solutionManager.getNodeIndexes(solution);
		} else {
			this.bestSolutionWeight = Double.NEGATIVE_INFINITY;
		}

		this.boundCalc.setInstance(instance);

		this.upperBound = boundCalc.getUpperBound();

		if (approxMethod != null && reduction && boundCalc.isRandomFixedAllowed()) {

			boolean[] heuristicSolutionNodes = ArraysUtil.toBooleanArray(solutionManager.getNodeIndexes(solution), n);

			for (int i = 0; i < n; i++) {
				boundCalc.fixNode(i, !heuristicSolutionNodes[i]);

				if (boundCalc.getUpperBound() < solution.getWeight()) {

					//TODO: Change instance to fix this nodes.
					System.out.println("Node " + i + " must be fixed to " + heuristicSolutionNodes[i]);
				}

				boundCalc.freeLastFixedNode();
			}

		}

		try {

			//Start tree traversal
			processNode(0);

			upperBound = this.bestSolutionWeight;

			status = Status.OPTIMAL;

		} catch (TimeLimitException e) {

			this.status = Status.TIME_LIMIT;

		}

	}

	private void processNode(int numNode) {

		numVisitedNodes++;

		//boundCalc.printBoundState();
		//System.out.println("Solution nodes: " + Arrays.toString(solution) + " " + boundCalc.getUpperBound());

		int numFreeNodes = n - boundCalc.getNumFixedNodes();
		int numSelectedNodes = boundCalc.getNumSelectedNodes();

		if (numSelectedNodes + numFreeNodes < m + 1) {

			boolean[] selectedNodes = boundCalc.getSelectedNodes();

			int[] solutionNodes = new int[m];

			int counter = 0;
			for (int i = 0; i < selectedNodes.length; i++) {
				boolean selected = selectedNodes[i];
				if (selected) {
					solutionNodes[counter] = i;
					counter++;
				}
			}

			for (int i = boundCalc.getNumFixedNodes(); i < n; i++) {
				solutionNodes[counter] = i;
				counter++;
			}

			solutionCalculated(solutionNodes);

		} else if (numSelectedNodes == m) {

			solutionCalculated(ArraysUtil.toIntArray(boundCalc.getSelectedNodes()));

		} else {

			//System.out.println("Starting node[" + numNode + "] => 1");
			//boundCalc.fixedNode(numNode, true);
			boundCalc.fixNextNode(true);
			//System.out.println("Finish node[" + numNode + "] => 1");

			checkTime();

			if (!prone()) {
				processNode(numNode + 1);
			}

			//System.out.println("Starting Free " + numNode);
			boundCalc.freeLastFixedNode();
			//System.out.println("Finish Free " + numNode);

			//System.out.println("Starting node[" + numNode + "] => 0");
			//boundCalc.fixedNode(numNode, false);
			boundCalc.fixNextNode(false);
			//System.out.println("Finish node[" + numNode + "] => 0");

			numUnselectedNodes++;

			checkTime();

			if (!prone()) {

				if (numUnselectedNodes == numNode + 1) {
					setUpperBound(boundCalc.getUpperBound());
				}

				processNode(numNode + 1);
			}

			//System.out.println("Starting Free " + numNode);
			boundCalc.freeLastFixedNode();
			//System.out.println("Finish Free " + numNode);

			numUnselectedNodes--;
		}
	}

	private void setUpperBound(double upperBound) {
		//System.out.println("NumUnselectedNodes: " + numUnselectedNodes);
		//System.out.println("UpperBound: " + upperBound);
		this.upperBound = upperBound;
	}

	private boolean prone() {

		//Cambio para optimizar, pasamos el valor del heurístico para que lo calcule más rápido
		//boolean prone = this.boundCalc.getUpperBound() <= bestSolutionWeight;

		boolean prone = this.boundCalc.prone(bestSolutionWeight);

		//		if (prone) {
		//			System.out.println("--------> Prone");
		//		}
		return prone;
		//Hay veces que interesa desactivar la poda :)
		//return false;
	}

	private void solutionCalculated(int[] solutionNodes) {

		double upperBound = boundCalc.getUpperBound();

		if (bestSolutionWeight < upperBound) {
			bestSolutionWeight = upperBound;
			this.bestSolutionNodes = solutionNodes;
		}
	}

	@Id
	public BoundCalcBinaryTreeBBStandard<I> getBoundCalc() {
		return boundCalc;
	}

	public BinaryTreeBBStandard<S, I> setBoundCalculator(BoundCalcBinaryTreeBBStandard<I> boundCalc) {
		this.boundCalc = boundCalc;
		return this;
	}

	@Id
	public boolean isReduction() {
		return reduction;
	}

	public void setReduction(boolean reduction) {
		this.reduction = reduction;
	}

}
