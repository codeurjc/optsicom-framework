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
package es.optsicom.lib.analyzer.tablecreator.pr;

import java.util.List;

import es.optsicom.lib.expresults.model.Execution;

public abstract class RawProcessor {

	public abstract Double[] cookEvents(List<Execution> execs);

	public abstract List<String> getCookedEventsNames();

}
