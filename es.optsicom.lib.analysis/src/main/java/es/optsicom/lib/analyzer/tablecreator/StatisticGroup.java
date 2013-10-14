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
package es.optsicom.lib.analyzer.tablecreator;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.pr.RawProcessor;

public class StatisticGroup {

	private RawProcessor rawProcessor;
	private Statistic[][] statistics;
	private List<Statistic> statisticList;

	private StatisticGroup(RawProcessor rawProcessor, Statistic[][] statistics) {
		this.rawProcessor = rawProcessor;
		this.statistics = statistics;
		
		this.statisticList = new ArrayList<Statistic>();
		
		for(int i=0; i<statistics.length; i++){
			for(int j=0; j<statistics[0].length; j++){
				Statistic statistic = statistics[i][j];
				statistic.configureStatisticGroup(this, rawProcessor, i);
				statisticList.add(statistic);
			}
		}
	}

	public RawProcessor getRawProcessor() {
		return rawProcessor;
	}

	public Statistic[][] getStatistics() {
		return statistics;
	}

	public List<Statistic> getStatisticList() {
		return statisticList;
	}
	
	public int getNumStatistics() {
		return statisticList.size();
	}

	public static StatisticGroup createSimpleStatisticGroup(
			RawProcessor rawProcessor, Statistic... statistics) {
		return new StatisticGroup(rawProcessor,	new Statistic[][] { statistics });
	}

	public static StatisticGroup createMultipleStatisticGroup(
			RawProcessor rawProcessor, Statistic[]... statistics) {
		return new StatisticGroup(rawProcessor, statistics);
	}
}
