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

public class GapRP extends SummarizeRawProcessor {

	@Override
	public Double[] cookEvents(Execution exec) {

		Event ubEvent = exec.getLastEvent("upperBound");
		Event lbEvent = exec.getLastEvent("lowerBound");

		if (ubEvent == null) {
			return new Double[] { 0d, 1d };
		} else {

			double lowerBound = (Double) lbEvent.getValue();
			double upperBound = (Double) ubEvent.getValue();

			double gap = (upperBound - lowerBound) / upperBound;
			return new Double[] { gap, 0d };
		}

	}

	@Override
	public List<String> getCookedEventsNames() {
		return Arrays.asList("Gap", "#Opt");
	}

}
