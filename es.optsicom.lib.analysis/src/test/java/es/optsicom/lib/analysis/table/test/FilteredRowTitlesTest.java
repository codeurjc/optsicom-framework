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
package es.optsicom.lib.analysis.table.test;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.table.FilteredRowTitles;
import es.optsicom.lib.expresults.model.DBProperties;

public class FilteredRowTitlesTest {

	public static void main(String[] args) {

		List<DBProperties> titles = new ArrayList<DBProperties>();

		DBProperties map = new DBProperties("test");
		map.put("prop1", "value1");
		map.put("prop2", "value2");
		map.put("prop3", "value3_1");
		map.put("propM1", "value4");
		titles.add(map);

		DBProperties map2 = new DBProperties("test");
		map2.put("prop1", "value1");
		map2.put("prop2", "value2");
		map2.put("prop3", "value3");
		map2.put("propM2", "value4");
		titles.add(map2);

		DBProperties map3 = new DBProperties("test");
		map3.put("prop1", "value1");
		map3.put("prop2", "value2");
		map3.put("prop3", "value3");
		map3.put("propM3", "value4");
		titles.add(map3);

		FilteredRowTitles frt = new FilteredRowTitles(titles);

		System.out.println("CommonProperties:");
		System.out.println(frt.getCommonProperties());

		System.out.println("FilteredRowTitles:");
		System.out.println(frt.getFilteredRowTitles());

	}

}
