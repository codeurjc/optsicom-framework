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

import es.optsicom.lib.util.description.Properties;

/**
 * @author Patxi Gortázar
 * 
 */
public class NotElementsFilter extends ElementFilter {

	private ElementFilter elementFilter;

	public NotElementsFilter(ElementFilter elementFilter) {
		this.elementFilter = elementFilter;
	}

	public boolean isAllowed(Properties properties) {
		return !elementFilter.isAllowed(properties);
	}

}
