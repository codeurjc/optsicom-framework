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

public class DurationEventRP extends SummarizeRawProcessor {

	private String fromEventName;
	private String toEventName;

	private SummarizeMode inExecSummarizeMode;

	public DurationEventRP(String fromEventName, String toEventName) {
		this(fromEventName, toEventName, SummarizeMode.AVERAGE);
	}

	public DurationEventRP(String fromEventName, String toEventName, SummarizeMode inExecSummarizeMode) {
		super();
		this.fromEventName = fromEventName;
		this.toEventName = toEventName;
		this.inExecSummarizeMode = inExecSummarizeMode;
	}

	@Override
	public Double[] cookEvents(Execution exec) {

		List<Long> durations = new ArrayList<Long>();

		boolean started = false;
		long startTime = 0;

		for (Event event : exec.getEvents()) {
			if (event.getName().equals(fromEventName)) {
				started = true;
				startTime = event.getTimestamp();
			} else if (event.getName().equals(toEventName)) {
				if (started) {
					durations.add(event.getTimestamp() - startTime);
				}
				started = false;
			}
		}

		// System.out.println(getCookedEventsNames().get(0));
		// for (Long duration : durations) {
		// System.out.println(duration);
		// }

		return new Double[] { inExecSummarizeMode.summarizeValues(durations) };
	}

	@Override
	public List<String> getCookedEventsNames() {
		return Arrays
				.asList("duration from " + fromEventName + " to " + toEventName + "(" + this.inExecSummarizeMode + ")");
	}

}
