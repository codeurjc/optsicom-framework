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

import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public abstract class RelativizerStatisticCalc extends StatisticCalc {

	protected BestMode bestMode = BestMode.MAX_IS_BEST;
	protected RelativeValueProvider relativeValueProvider = null;
	
	protected RelativizerStatisticCalc(SummarizeMode summarizeMode, BestMode mode) {
		super(summarizeMode);
		this.bestMode = mode;
	}
	
	protected RelativizerStatisticCalc(SummarizeMode summarizeMode, BestMode mode, BestMode resultBestMode) {
		super(summarizeMode, resultBestMode);
		this.bestMode = mode;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public BestMode getBestMode() {
		return bestMode;
	}

	public void setBestMode(BestMode mode) {
		this.bestMode = mode;
	}

	public RelativeValueProvider getRelativeValueProvider() {
		return relativeValueProvider;
	}

	public void setRelativeValueProvider(RelativeValueProvider relativeValueProvider) {
		this.relativeValueProvider = relativeValueProvider;
	}

}
