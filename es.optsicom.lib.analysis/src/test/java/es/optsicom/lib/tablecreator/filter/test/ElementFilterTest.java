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
package es.optsicom.lib.tablecreator.filter.test;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.filter.ExplicitElementsFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.PropertiesFilter;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.util.description.Properties;

public class ElementFilterTest {
	public static void main(String[] args) {

		List<DBProperties> elements = new ArrayList<DBProperties>();

		DBProperties map = new DBProperties("test");
		map.put("prop1", "value1");
		map.put("prop2", "value2");
		map.put("prop3", "value3_1");
		map.put("propM1", "value4");
		elements.add(map);

		DBProperties map2 = new DBProperties("test");
		map2.put("prop1", "value1");
		map2.put("prop2", "value2");
		map2.put("prop3", "value3");
		map2.put("propM2", "value4");
		elements.add(map2);

		DBProperties map3 = new DBProperties("test");
		map3.put("prop1", "value1");
		map3.put("prop2", "value2");
		map3.put("prop3", "value3");
		map3.put("propM3", "value4");
		elements.add(map3);

		System.out.println("Explicit Filter");
		ExplicitElementsFilter elf = new ExplicitElementsFilter(
		        "{prop1=value1,prop2=value2,prop3=value3_1,propM1=value4}");

		for (DBProperties props : elements) {
			System.out.println("Props:");
			System.out.println(props);
			System.out.println("Allowed: " + elf.isAllowed(props));
		}

		System.out.println("Props Filter");
		PropertiesFilter pf = new PropertiesFilter("prop1=value1", "prop2=value2");

		for (DBProperties props : elements) {
			System.out.println("Props:");
			System.out.println(props);
			System.out.println("Allowed: " + pf.isAllowed(props));
		}

	}
}
