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

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.ApproxMethodListener;

public interface ScatterSearchListener<S extends Solution<I>, I extends Instance> extends ApproxMethodListener<S, I> {

	void initialSolutionsCreated(ScatterSearch<S, I> scatterSearch);

	void initialSolutionsImproved(ScatterSearch<S, I> scatterSearch);

	void refSetCreated(ScatterSearch<S, I> scatterSearch);

	void refSetCombined(ScatterSearch<S, I> scatterSearch);

	void refSetCombinationsImproved(ScatterSearch<S, I> scatterSearch);

	void refSetReconstructed(ScatterSearch<S, I> scatterSearch);

	void initIteration(ScatterSearch<S, I> scatterSearch);

	void refSetRefreshed(ScatterSearch<S, I> scatterSearch);

	void newRefSetSolCalculated(ScatterSearch<S, I> scatterSearch);

	void setInstance(ScatterSearch<S, I> scatterSearch);

	void removeInstance(ScatterSearch<S, I> scatterSearch);
}
