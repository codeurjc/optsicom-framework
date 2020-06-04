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
package es.optsicom.lib.analyzer.report.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import es.optsicom.lib.expresults.model.DBProperties;

public class FilteredRowTitles {

	private List<DBProperties> originalRowTitles;
	private DBProperties commonProperties;
	private List<DBProperties> filteredRowTitles;

	public FilteredRowTitles(List<DBProperties> rowTitles) {
		this.originalRowTitles = rowTitles;
		processOriginalRowTitles();
	}

	private void processOriginalRowTitles() {

		Set<Entry<String, String>> commonPropoertiesEntries = new HashSet<Map.Entry<String, String>>(
				originalRowTitles.get(0).getMap().entrySet());

		for (int i = 1; i < originalRowTitles.size(); i++) {
			commonPropoertiesEntries.retainAll(originalRowTitles.get(i).getMap().entrySet());
		}

		commonProperties = new DBProperties(new HashMap<String, String>());
		for (Entry<String, String> entry : commonPropoertiesEntries) {
			commonProperties.put(entry.getKey(), entry.getValue());
		}

		filteredRowTitles = new ArrayList<DBProperties>();
		for (DBProperties properties : originalRowTitles) {

			DBProperties filteredProps = new DBProperties(properties);

			for (String propName : commonProperties.keySet()) {
				filteredProps.getMap().remove(propName);
			}

			filteredRowTitles.add(filteredProps);
		}
	}

	public List<DBProperties> getFilteredRowTitles() {
		return filteredRowTitles;
	}

	public DBProperties getCommonProperties() {
		return commonProperties;
	}

}
