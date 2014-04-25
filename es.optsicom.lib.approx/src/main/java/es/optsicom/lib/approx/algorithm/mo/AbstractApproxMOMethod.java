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
package es.optsicom.lib.approx.algorithm.mo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.optsicom.lib.AbstractMethod;
import es.optsicom.lib.Instance;
import es.optsicom.lib.Problem;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.AbstractApproxMethod;
import es.optsicom.lib.approx.experiment.ApproxExecResult;
import es.optsicom.lib.approx.improvement.ImprovementMethod;
import es.optsicom.lib.approx.improvement.ImprovementMethodListener;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExecLogger;
import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.experiment.StopMethodException;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.multiobjective.MultiObjectiveSolution;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

/**
 * 
 * This an abstract class that contains utility methods for implementing
 * SolutionCalculators. It can be used for maximization and minimization
 * problems.
 * 
 * Subclasses must implement {@link #internalCalculateSolution(long)}
 * 
 * @param <S>
 *            class that represents problem's solutions
 * @param <I>
 *            class that represents problem's instances
 */
public abstract class AbstractApproxMOMethod<S extends Solution<I>, I extends Instance> extends AbstractApproxMethod<S, I> {

	protected List<S> bestSolutions = new ArrayList<S>();
	
	protected void setIfNonDominated(S solution) {

		MultiObjectiveSolution pSolution = (MultiObjectiveSolution) solution;
		List<S> dominatedSolutions = new ArrayList<S>();
		for(S s : bestSolutions) {
			MultiObjectiveSolution ps = (MultiObjectiveSolution) s;
			if(pSolution.isDominated(ps)) {
				return;
			}
			if(ps.isDominated(pSolution)) {
				dominatedSolutions.add(s);
			}
		}
		
		ExecLogger.newSolutionFound(solution);
		
		bestSolutions.removeAll(dominatedSolutions);
		bestSolutions.add((S) solution.createCopy());
		
	}
	
	@Override
	public void newBestSolutionFound(ImprovementMethod<S, I> improvementMethod, double weight) {
		throw new UnsupportedOperationException("Can't test dominance without a solution");
	}
	
	public void newBestSolutionFound(es.optsicom.lib.approx.improvement.ImprovementMethod<S,I> improvementMethod, S newBestSolution) {
		this.setIfNonDominated(newBestSolution);
	};
	
	public List<S> getBestSolutions() {
		return Collections.unmodifiableList(bestSolutions);
	}
}
