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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.util.ArraysUtil;

/**
 * @author patxi
 * 
 */
public class EnumRP extends SummarizeRawProcessor {
	private boolean percentage = false;
	private String eventName;
	private String[] eventValues;

	private Map<String, Integer> counts = new HashMap<String, Integer>();

	public EnumRP(String event, String... values) {
		this.eventName = event;
		this.eventValues = values;
	}

	@Override
	public Double[] cookEvents(Execution exec) {
		Double[] result = ArraysUtil.createDoubleArray(eventValues.length);

		double numEvents = 0;
		for (int index = 0; index < eventValues.length; index++) {
			counts.put(eventValues[index], 0);
		}

		for (Event event : exec.getEvents()) {
			if (event.getName().equals(eventName)) {
				String eventValue = (String) event.getValue();
				counts.put(eventValue, counts.get(eventValue) + 1);
				numEvents++;
			}
		}

		for (int index = 0; index < eventValues.length; index++) {
			result[index] = Double.valueOf(counts.get(eventValues[index]));
		}

		if (percentage) {
			for (int i = 0; i < result.length; i++) {
				result[i] = result[i] / numEvents;
			}
		}

		return result;
	}

	@Override
	public List<String> getCookedEventsNames() {
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < eventValues.length; i++) {
			names.add("=" + eventValues[i]);
		}
		return names;
	}

}
