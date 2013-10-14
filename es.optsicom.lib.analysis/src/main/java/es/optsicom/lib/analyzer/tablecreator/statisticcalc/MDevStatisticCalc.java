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
 * For each instance, we compute the overall average solution value obtained by
 * the procedures or configurations to one instance. Then, for each method, we
 * determine the relative percentage deviation for that instance: how much the
 * average solution value obtained by that method is above (or below) the
 * overall average in percentage terms. By taking the average of these
 * deviations over all instances, we obtain the average relative percentage
 * deviation (%dev) for each method [Resende2004].
 * 
 * Each distance in the reduced set was run five times, with five different
 * random seeds. The first result is the average relative error obtained with
 * the corresponding strategy over all instances tested. For each run, the
 * relative error observed with some strategy is computed with respect to the
 * best average solution value among those found for this instance with the
 * seven different strategies compared [Ribeiro2002]
 * 
 * Devuelve el resultado normalizado a 1.
 * 
 * Debido a sus caracter�sticas, ese calculador no puede usarse cuando la media
 * de los valores es 0.
 * 
 * @author Mica
 * 
 */
public class MDevStatisticCalc extends RelativizerStatisticCalc {

	public MDevStatisticCalc() {
		this(BestMode.MAX_IS_BEST);
	}

	public MDevStatisticCalc(BestMode mode) {
		super(SummarizeMode.AVERAGE, mode);
	}

	@Override
	public Double[] relativize(Double[] values, Number bestValue) {

		if (bestMode == BestMode.MIN_IS_BEST) {
			throw new Error("Not supported mode " + bestMode);
		}

		Double[] result = ArraysUtil.createDoubleArray(values.length);

		double mean = ArraysUtil.average(values);
		for (int i = 0; i < values.length; i++) {
			result[i] = ((mean - values[i]) / mean);
		}

		return result;
	}

	@Override
	public String getName() {
		return "pMDev";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.PERCENT;
	}
}
