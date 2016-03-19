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
package es.optsicom.lib.approx.diversificator;

import java.util.Collection;
import java.util.List;

import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.Descriptive;

public interface Diversificator<S extends Solution<?>> extends Descriptive {

	public List<S> getDiversity(int num, Collection<S> solutions, List<S> refSolutions);

}
