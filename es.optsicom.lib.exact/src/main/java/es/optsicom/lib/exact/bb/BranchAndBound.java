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
import es.optsicom.lib.exact.AbstractExactMethod;
import es.optsicom.lib.exact.ExactMethod;
import es.optsicom.lib.exact.ExactResult;
import es.optsicom.lib.experiment.CurrentExperiment;

/**
 * En el estudio del exacto del MDP hemos implementado dos formas de recorrer el árbol. Una de ellas es la propuesta en nuestro paper
 * (BinaryTreeBranchAndBoundPaper) y la otra es la propuesta en el paper de Pisinger (BinaryTreeBranchAndBoundPisinger). 
 * Ambas clases tienen un mecanismo para cambiar la implementación de la cota. 
 * 
 * Esta clase es genérica para cualquier problema binario con un número de elementos activados fijo. Ideal para MDP y MMDP. 
 * @author mica
 *
 */
public abstract class BranchAndBound<S extends Solution<I>, I extends Instance> extends AbstractExactMethod<S, I> {

	public static enum Status {
		OPTIMAL, TIME_LIMIT
	};

	@Override
	public ExactResult<S> execute(I instance, long timeLimit) {

		long startTime = System.currentTimeMillis();
		this.internalSolve(instance, timeLimit);
		long execTime = System.currentTimeMillis() - startTime;

		BranchAndBound.Status status = this.getStatus();
		ExactResult<S> result = null;

		long totalTreeNodes = getNumTotalNodes();

		CurrentExperiment.addEvent("totalTreeNodes", totalTreeNodes);

		S s = createSolution(getBestSolutionNodes(), instance);

		switch (status) {
		case OPTIMAL:
			result = new ExactResult<S>(execTime, s, getNumVisitedNodes());
			break;

		case TIME_LIMIT: {
			result = new ExactResult<S>(execTime, s, this.getUpperBound(), this.getLowerBound(), getNumVisitedNodes());
			break;
		}
		}

		return result;
	}

	protected abstract S createSolution(int[] bestSolutionNodes, I instance);

	public abstract double getLowerBound();

	public abstract double getUpperBound();

	public abstract double getBestSolutionWeight();

	public abstract Status getStatus();

	public abstract int[] getBestSolutionNodes();

	public abstract long getNumVisitedNodes();

	public abstract long getNumTotalNodes();

	protected abstract void internalSolve(I instance, long timeMillis);

}