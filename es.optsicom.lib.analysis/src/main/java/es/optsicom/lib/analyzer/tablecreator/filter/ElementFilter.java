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
 * This class is used to filter elements. The filter mechanism can be made by explicit enumerate valid elements or
 * can be made with different kind of rules. For example, the elements with a property with a specific value or that 
 * pass an expression.
 * @author mica
 *
 */

public abstract class ElementFilter {

	public abstract boolean isAllowed(Properties properties);

}
