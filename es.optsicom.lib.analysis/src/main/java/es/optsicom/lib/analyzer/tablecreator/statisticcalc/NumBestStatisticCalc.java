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
 * Calculates the number of bests. Valid for both maximization and minimization
 * problems.
 * 
 * @author patxi
 * 
 */
public class NumBestStatisticCalc extends RelativizerStatisticCalc {

	public static final double EPSILON = 0.000001d;

	public NumBestStatisticCalc(BestMode bestMode) {
		super(SummarizeMode.SUM, bestMode, BestMode.MAX_IS_BEST);
	}

	public NumBestStatisticCalc() {
		this(BestMode.MAX_IS_BEST);
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
			if (isSameValue(values[i], best)) {
				result[i] = 1d;
			}
		}

		return result;
	}

	public static boolean isSameValue(double value, double best) {
		double dev = Math.abs((value - best) / Math.max(value, best));
		return dev <= EPSILON;
	}

	@Override
	public String getName() {
		return "#Best (" + bestMode + ")";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.INTEGER;
	}

}
