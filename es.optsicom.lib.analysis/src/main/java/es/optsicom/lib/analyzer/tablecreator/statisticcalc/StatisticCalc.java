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

// Las instancias de esta clase se encargan de procesar los valores por cada instancia y 
// generar nuevos valores que estén adaptados para calcular la media, la suma, etc. Por 
// ejemplo, clases hijas son DevStatisticCalc, NumBestStatisticCalc, etc.. 
public abstract class StatisticCalc {

	protected SummarizeMode summarizeMode;
	protected BestMode resultsBestMode = null;

	public StatisticCalc(SummarizeMode summarizeMode) {
		this.summarizeMode = summarizeMode;
	}
	
	public StatisticCalc(SummarizeMode summarizeMode, BestMode resultBestMode) {
		this.summarizeMode = summarizeMode;
		this.resultsBestMode = resultBestMode;
	}

	public Double[] relativize(Double[] valuesByMethod) {
		return relativize(valuesByMethod, null);
	}

	/**
	 * Returns the values relativized beetween them. For example, this is
	 * specially useful when comparing objValues obtained with different methods
	 * 
	 * @param methodData
	 * @return
	 */
	public Double[] relativize(Double[] valuesByMethod, Number bestValue) {
		return valuesByMethod;
	}

	/**
	 * Returns the double summarizing the values for a method in all instances
	 * of the analysis.
	 * 
	 * @param allInstanceValues
	 * @return
	 */
	public Double summarize(Double[] allInstanceValues) {
		return summarizeMode.summarizeValues(allInstanceValues);
	}

	public abstract String getName();

	public abstract NumberType getNumberType();
	
	/**
	 * This information about the statistic allows an automatic analysis
	 * of results
	 * @return
	 */
	public BestMode getResultsBestMode(){
		return resultsBestMode;
	}
	
	public void setResultsBestMode(BestMode resultsBestMode) {
		this.resultsBestMode = resultsBestMode;
	}
}
