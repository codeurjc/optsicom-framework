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
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public class NonRelativizerStatisticCalc extends StatisticCalc {

	private NumberType numberType;

	public NonRelativizerStatisticCalc(SummarizeMode summarizeMode, NumberType numberType) {
		super(summarizeMode);
		this.numberType = numberType;
	}

	public NonRelativizerStatisticCalc(SummarizeMode summarizeMode, NumberType numberType, BestMode resultBestMode) {
		super(summarizeMode, resultBestMode);
		this.numberType = numberType;
	}

	@Override
	public String getName() {
		return summarizeMode.name();
	}

	@Override
	public NumberType getNumberType() {
		return numberType;
	}

}
