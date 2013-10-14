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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.util.SummarizeMode;

public class SummarizeExecRP extends SummarizeRawProcessor {

	public SummarizeMode execSummarizeMode;
	public String eventName;

	public SummarizeExecRP(String eventName, SummarizeMode execSummarizeMode) {
		this.execSummarizeMode = execSummarizeMode;
		this.eventName = eventName;
	}

	@Override
	public Double[] cookEvents(Execution exec) {

		List<Number> values = new ArrayList<Number>();
		for (Event event : exec.getEvents()) {
			if (event.getName().equals(eventName)) {
				values.add((Number) event.getValue());
			}
		}
		return new Double[] { summarizeMode.summarizeValues(values) };
	}

	@Override
	public List<String> getCookedEventsNames() {
		return Arrays.asList(execSummarizeMode + " of " + eventName);
	}
}
