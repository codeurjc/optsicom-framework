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

import java.util.Arrays;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class FeasStatisticCalc extends RelativizerStatisticCalc {

	private double feasibleLimit = 99999999;
	
	public FeasStatisticCalc(BestMode mode) {
		super(SummarizeMode.SUM, mode, BestMode.MAX_IS_BEST);
	}

	public FeasStatisticCalc() {
		this(BestMode.MAX_IS_BEST);
	}

	@Override
	public Double[] relativize(Double[] values, Number bestValue) {

		//System.out.print("Values: "+Arrays.toString(values));
		
		Double[] result = ArraysUtil.createDoubleArray(values.length);

		for (int i = 0; i < result.length; i++) {

			if(values[i] == null || Math.abs(values[i]) > feasibleLimit){
				result[i] = 0d;
			} else {
				result[i] = 1d;
			}
		}
		
		//System.out.println(" Result: "+Arrays.toString(result));

		return result;
	}

	@Override
	public String getName() {
		return "Feasible (" + bestMode + ")";
	}

	@Override
	public NumberType getNumberType() {
		return NumberType.INTEGER;
	}
}
