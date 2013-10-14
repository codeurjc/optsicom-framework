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
package es.optsicom.lib.analyzer.tablecreator.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import es.optsicom.lib.util.description.Properties;

/**
 * This class allows all elements that have specified properties and values. The properties are specified as name+"="+value.
 * A element that have specified properties and values is allowed. 
 */

public class PropertiesFilter extends ElementFilter {

	private List<String> mandatoryProps;

	public PropertiesFilter(List<String> mandatoryProps) {
		this.mandatoryProps = mandatoryProps;
	}

	public PropertiesFilter(String... mandatoryProps) {
		this(Arrays.asList(mandatoryProps));
	}

	@Override
	public boolean isAllowed(Properties properties) {
		return getPropList(properties).containsAll(mandatoryProps);
	}

	private List<String> getPropList(Properties properties) {

		List<String> list = new ArrayList<String>();
		for (String key : new TreeSet<String>(properties.getMap().keySet())) {
			list.add(key + "=" + properties.get(key));
		}
		return list;
	}

	public List<String> getMandatoryProps() {
		return mandatoryProps;
	}

}
