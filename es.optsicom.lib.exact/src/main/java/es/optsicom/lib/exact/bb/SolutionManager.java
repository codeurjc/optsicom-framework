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

public interface SolutionManager<S extends Solution<I>, I extends Instance> {

	int[] getNodeIndexes(S solution);

	int getNumSelectedNodes(I instance);

	int getNumNodes(I instance);

	S createSolution(int[] selectedNodes, I instance);

}
