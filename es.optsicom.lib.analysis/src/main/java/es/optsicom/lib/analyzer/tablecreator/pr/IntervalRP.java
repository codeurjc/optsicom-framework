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
import es.optsicom.lib.util.ArraysUtil;

/**
 * Se cuenta el porcentaje de veces que el número es menor o igual que cada
 * valor del array de intervalos
 * 
 * @author mica
 *
 */
public class IntervalRP extends SummarizeRawProcessor {

	public String eventName;
	public double[] intervals;
	public boolean percentage = true;

	public boolean isPercentage() {
		return percentage;
	}

	public IntervalRP setPercentage(boolean percentage) {
		this.percentage = percentage;
		return this;
	}

	public IntervalRP(String eventName, double[] intervals) {
		this.eventName = eventName;
		this.intervals = intervals;
	}

	@Override
	public Double[] cookEvents(Execution exec) {

		Double[] result = ArraysUtil.createDoubleArray(intervals.length + 1);

		int numEvents = 0;
		for (Event event : exec.getEvents()) {
			if (event.getName().equals(eventName)) {
				double value = ((Number) event.getValue()).doubleValue();
				int index = Arrays.binarySearch(intervals, value);
				if (index < 0) {
					index = -index - 1;
				}
				result[index]++;
				numEvents++;
			}
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
		for (double interval : intervals) {
			names.add("<=" + interval);
		}
		names.add(">" + intervals[intervals.length - 1]);
		return names;
	}
}
