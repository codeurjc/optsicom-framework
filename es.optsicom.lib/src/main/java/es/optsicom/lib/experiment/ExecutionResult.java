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
package es.optsicom.lib.experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Solution;
import es.optsicom.lib.experiment.ExecutionLogger.Event;

public abstract class ExecutionResult implements Serializable {

	private static final long serialVersionUID = -8382051553384470138L;

	protected List<Event> rawEvents = new ArrayList<Event>();

	protected abstract List<Event> createFinishEvents();

	@SuppressWarnings("rawtypes")
	public abstract Solution getBestSolution();

	public void addEvent(Event event) {
		rawEvents.add(event);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Event event : rawEvents) {
			sb.append(event.toString()).append("\r\n");
		}
		return sb.toString();
	}

}
