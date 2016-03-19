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
 * For each instance, the methods were sorted according to their relative
 * percentage deviations; the best received one point, the second two points,
 * and so on, until the sixth best method, with six points. When there was a
 * tie, points were divided equally between the methods involved. For example,
 * if the deviations were -0.03, -0.02, -0.02, 0.01, 0.03, and 0.03, the
 * corresponding methods would receive 1, 2.5, 2.5, 4, 5.5, and 5.5 points,
 * respectively. The number of points received by a method according to this
 * process is its rank for that particular instance. Its normalized rank was
 * obtained by linearly mapping the range of ranks (1 to 6, in this case) to the
 * interval [-1, 1]. In the example above, the normalized ranks would be -1,
 * -0.4, -0.4, 0.2, 0.8, and 0.8. The normalized ranks must add up to zero (by
 * definition). If a method is always better than all others, its normalized
 * rank will be -1; if always worse, it will be 1. What columns 3 and 5 of Table
 * 1 show are the average normalized ranks of each method, taken over the set of
 * 10 instances. Column 3 refers to solution quality, and column 5 to running
 * times [Resende2004].
 * 
 * Devuelve el resultado normalizado a 1.
 * 
 * @author Mica
 * 
 */
public class RankStatisticCalc extends RelativizerStatisticCalc {

	public RankStatisticCalc() {
		this(BestMode.MAX_IS_BEST);
	}

	public RankStatisticCalc(BestMode bestMode) {
		super(SummarizeMode.AVERAGE, bestMode, BestMode.MIN_IS_BEST);
	}

	@Override
	public Double[] relativize(Double[] values, Number bestValue) {

		Double[] result = ArraysUtil.createDoubleArray(values.length);
		Double[] copy = values.clone();

		int[] ranking = ArraysUtil.sort(copy);

		if (bestMode == BestMode.MAX_IS_BEST) {
			ArraysUtil.reverse(ranking);
			ArraysUtil.reverse(copy);
		}

		for (int i = 0; i < ranking.length; i++) {

			int equals = 1;
			for (int j = i; j < (ranking.length - 1) && copy[j] == copy[j + 1]; j++) {
				equals++;
			}

			double sumaSerie = (1 + equals) * equals / 2;
			double points = ((equals * i) + sumaSerie) / equals;
			for (int j = 0; j < equals; j++) {
				int indexValue = j + i;
				result[ranking[indexValue]] = points;
			}
			i += (equals - 1);
		}

		ArraysUtil.mapTo(result, 1, values.length, -1, +1);

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + bestMode + ")";
	}

	@Override
	public String getName() {
		return "Rank";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.DECIMAL;
	}
}
