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
 * The second result is the score achieved by each strategy, considering all
 * instances. For a given instance, the score of a strategy Y is defined as the
 * number of strategies that found better average solutions than Y. In other
 * words, if Y found the best solution, its score is 0; if it found the second
 * best, it is 1; and so on, until 6. In case of ties, all strategies involved
 * receive the same score, equal to the number of strategies strictly better
 * than all of them. The score of a strategy is the sum of the scores obtained
 * for all instances.
 * 
 * Obtenido del [Ribeiro2002].
 * 
 * Devuelve el resultado normalizado a 1.
 * 
 * @author Mica
 * 
 */
public class ScoreStatisticCalc extends RelativizerStatisticCalc {

	public ScoreStatisticCalc(BestMode mode) {
		super(SummarizeMode.SUM, mode, BestMode.MIN_IS_BEST);
	}

	public ScoreStatisticCalc() {
		this(BestMode.MAX_IS_BEST);
	}

	@Override
	public Double[] relativize(Double[] values, Number bestValue) {

		Double[] result = ArraysUtil.createDoubleArray(values.length);
		Double[] copy = values.clone();

		int[] ranking = ArraysUtil.sort(copy, bestMode);

		for (int i = 0; i < ranking.length; i++) {

			int equals = 1;
			for (int j = i; j < (ranking.length - 1) && NumBestStatisticCalc.isSameValue(copy[j], copy[j + 1]); j++) {
				equals++;
			}

			for (int j = 0; j < equals; j++) {
				int indexValue = j + i;
				result[ranking[indexValue]] = (double) i;
			}
			i += (equals - 1);
		}

		return result;
	}

	@Override
	public String getName() {
		return "Score (" + bestMode + ")";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.INTEGER;
	}
}
