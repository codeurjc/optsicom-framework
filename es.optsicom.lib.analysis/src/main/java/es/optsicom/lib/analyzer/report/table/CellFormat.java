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

import java.util.Locale;

public abstract class CellFormat {

	public String format(Object value) {
		return format(value, Locale.getDefault());
	}

	public abstract String format(Object value, Locale locale);

}
