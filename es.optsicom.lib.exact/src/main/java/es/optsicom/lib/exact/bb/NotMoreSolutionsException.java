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
package es.optsicom.lib.exact.bb;

public class NotMoreSolutionsException extends Exception {

	private static final long serialVersionUID = -6510409826034697985L;

	public NotMoreSolutionsException() {
		super();
	}

	public NotMoreSolutionsException(String msg) {
		super(msg);
	}

}
