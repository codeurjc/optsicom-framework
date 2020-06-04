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
package es.optsicom.lib.approx.algorithm.pr;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class PathRelinking<S extends Solution<I>, I extends Instance> implements Descriptive {

	public enum PrMode {
		TWO_WAY, FORWARD, BACKWARD
	}

	protected PrMode prMode = PrMode.TWO_WAY;

	protected abstract S createSolution(S initiatingSol, S guidingSol);

	@Id
	public PrMode getPrMode() {
		return prMode;
	}

	public void setPrMode(PrMode prMode) {
		this.prMode = prMode;
	}

	public S pathRelinking(S solution, S esSolution) {

		switch (prMode) {
		case TWO_WAY:
			return pathRelinkingTwoWay(solution, esSolution);
		case FORWARD:
			return pathRelinkingForward(solution, esSolution);
		case BACKWARD:
			return pathRelinkingBackward(solution, esSolution);
		default:
			throw new Error();
		}
	}

	private S pathRelinkingBackward(S solution, S esSolution) {

		S bestSolution;
		S worstSolution;

		if (solution.getWeight() > esSolution.getWeight()) {
			bestSolution = solution;
			worstSolution = esSolution;
		} else {
			bestSolution = esSolution;
			worstSolution = solution;
		}

		return this.createSolution(worstSolution, bestSolution);
	}

	private S pathRelinkingForward(S solution, S esSolution) {

		S bestSolution;
		S worstSolution;

		if (solution.getWeight() > esSolution.getWeight()) {
			bestSolution = solution;
			worstSolution = esSolution;
		} else {
			bestSolution = esSolution;
			worstSolution = solution;
		}

		return this.createSolution(bestSolution, worstSolution);
	}

	private S pathRelinkingTwoWay(S solution, S esSolution) {
		// CurrentExperiment.addEvent("prStarted");

		S solX = this.createSolution(solution, esSolution);
		S solXAux = this.createSolution(esSolution, solution);

		// CurrentExperiment.addEvent("prFinished");

		if (solX == null) {
			return solXAux;
		} else if (solXAux == null) {
			return solX;
		}

		if (solXAux.getWeight() > solX.getWeight()) {
			solX = solXAux;
		}

		return solX;
	}

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
