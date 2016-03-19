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
package es.optsicom.lib.analyzer.tablecreator.statisticcalc;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

/**
 * This statistic calculates deviation between solutions. If there is a
 * infeasible solution, it won't be considered in the average dev for any method
 * 
 * @author Mica
 * 
 */
public class FeasAllDevStatisticCalc extends RelativizerStatisticCalc {

	private double feasibleLimit = 99999999;

	public FeasAllDevStatisticCalc() {
		this(BestMode.MIN_IS_BEST);
	}

	public FeasAllDevStatisticCalc(BestMode bestMode) {
		super(SummarizeMode.AVERAGE, bestMode, BestMode.MIN_IS_BEST);
	}

	@Override
	public Double[] relativize(Double[] values, Number bestValue) {

		Double[] result = ArraysUtil.createDoubleArray(values.length);

		double best;
		if (bestValue == null) {
			best = ArraysUtil.getBest(bestMode, values);
		} else {
			best = bestValue.doubleValue();
		}

		for (int i = 0; i < values.length; i++) {
			if (Math.abs(values[i]) > feasibleLimit) {

				for (int j = 0; j < values.length; j++) {
					result[j] = null;
				}
				return result;

			} else {
				result[i] = (Math.abs((best - values[i]) / best));
			}
		}

		return result;
	}

	@Override
	public String getName() {
		return "FeasDev. (" + bestMode + ")";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.PERCENT;
	}
}
