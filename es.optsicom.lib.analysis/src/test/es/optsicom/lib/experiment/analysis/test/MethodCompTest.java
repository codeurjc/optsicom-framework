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
package es.optsicom.lib.experiment.analysis.test;

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.statisticcalc.DevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.MDevStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.NumBestStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.RankStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.RelativizerStatisticCalc;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.ScoreStatisticCalc;
import es.optsicom.lib.util.BestMode;

public class MethodCompTest {

	public static void main(String[] args) {

		//double[] values = { 1, 1, 2, 2, 4, 4 };

		double[] values = { -0.03, -0.02, -0.02, 0.01, 0.03, 0.03 };
		//El resultado del rank debe ser: {1, 2.5, 2.5, 4, 5.5, 5.5}

		System.out.println("Valores: " + Arrays.toString(values));

		List<RelativizerStatisticCalc> comparators = Arrays.<RelativizerStatisticCalc> asList(new MDevStatisticCalc(),
		        new DevStatisticCalc(), new RankStatisticCalc(), new RankStatisticCalc(BestMode.MIN_IS_BEST),
		        new NumBestStatisticCalc(), new ScoreStatisticCalc(), new ScoreStatisticCalc(BestMode.MIN_IS_BEST));

		for (RelativizerStatisticCalc comp : comparators) {
			double[] result = comp.relativize(values);
			System.out.println("Result " + comp + ": " + Arrays.toString(result));
		}

	}

}
