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
import java.util.List;

import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.SummarizeMode;

public abstract class SummarizeRawProcessor extends RawProcessor {

	protected SummarizeMode summarizeMode = SummarizeMode.AVERAGE;

	public SummarizeMode getSummarizeMode() {
		return summarizeMode;
	}

	public SummarizeRawProcessor setSummarizeMode(SummarizeMode summarizeMode) {
		this.summarizeMode = summarizeMode;
		return this;
	}

	@Override
	public Double[] cookEvents(List<Execution> execs) {

		List<Double[]> values = new ArrayList<Double[]>();

		for (Execution exec : execs) {
			values.add(cookEvents(exec));
		}

		if (values.size() == 0) {
			throw new RuntimeException("There is no cooked values");
		} else if (values.size() == 1) {
			return values.get(0);
		} else {
			Double[] cookedEvents = ArraysUtil.createDoubleArray(values.get(0).length);
			ListDoubleArrayEncapsulator list = new ListDoubleArrayEncapsulator(values);
			for (int i = 0; i < cookedEvents.length; i++) {
				list.setIndex(i);
				cookedEvents[0] = summarizeMode.summarizeValues(list);
			}
			return cookedEvents;
		}
	}

	public abstract Double[] cookEvents(Execution exec);

}
