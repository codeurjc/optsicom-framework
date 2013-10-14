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

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;


public class CounterEventRP extends SummarizeRawProcessor {

	private String eventName;

	public CounterEventRP(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public List<String> getCookedEventsNames() {
		return Arrays.asList("count " + eventName);
	}

	@Override
	public Double[] cookEvents(Execution exec) {

		int counter = 0;

		for (Event event : exec.getEvents()) {
			if (event.getName().equals(eventName)) {
				counter++;
			}
		}

		return new Double[] { Double.valueOf(counter) };

	}

}
