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
 * We use the outcomes of our experiments to calculate the average percent
 * deviation (pDev) of the solutions obtained by each procedure when compared to
 * the best solutions during the given experiments. Obtenido del paper de SS
 * para el MDP.
 * 
 * Devuelve el resultado normalizado a 1.
 * 
 * @author Mica
 * 
 */
public class DevStatisticCalc extends RelativizerStatisticCalc {

	public DevStatisticCalc() {
		this(BestMode.MAX_IS_BEST);
	}

	public DevStatisticCalc(BestMode bestMode) {
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
			result[i] = (Math.abs((best - values[i]) / best));
		}

		return result;
	}

	@Override
	public String getName() {
		return "Dev. (" + bestMode + ")";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.PERCENT;
	}
}
