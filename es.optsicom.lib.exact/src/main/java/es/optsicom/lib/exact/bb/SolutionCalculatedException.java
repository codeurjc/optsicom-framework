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

import es.optsicom.lib.Solution;

@SuppressWarnings("unchecked")
public class SolutionCalculatedException extends RuntimeException {

	private Solution solution;

	private static final long serialVersionUID = -5670786011704278809L;

	public SolutionCalculatedException(Solution solution) {
		this.solution = solution;
	}

	public Solution getSolution() {
		return solution;
	}

}
