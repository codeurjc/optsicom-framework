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
package es.optsicom.lib.approx.combinator;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.Descriptive;

public interface Combinator<S extends Solution<I>, I extends Instance> extends Descriptive {

	public List<S> combineSolutions(List<S> solutions);

	public List<S> combineGroups(List<List<S>> groups);

	public S combineGroup(List<S> group);

}
