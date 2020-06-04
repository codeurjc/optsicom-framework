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

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.util.description.Properties;

public class AndElementsFilter extends ElementFilter {

	List<ElementFilter> elementFilters;

	public AndElementsFilter(List<ElementFilter> elementFilters) {
		this.elementFilters = elementFilters;
	}

	public AndElementsFilter(ElementFilter... elementFilters) {
		this(Arrays.asList(elementFilters));
	}

	@Override
	public boolean isAllowed(Properties properties) {
		for (ElementFilter elementFilter : elementFilters) {
			if (!elementFilter.isAllowed(properties)) {
				return false;
			}
		}
		return true;
	}

}
