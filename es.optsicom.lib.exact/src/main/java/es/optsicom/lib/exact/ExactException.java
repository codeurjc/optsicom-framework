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
package es.optsicom.lib.exact;

public class ExactException extends RuntimeException {

	private static final long serialVersionUID = 5230218936134126444L;

	public ExactException() {
	}

	public ExactException(String message) {
		super(message);
	}

	public ExactException(Throwable cause) {
		super(cause);
	}

	public ExactException(String message, Throwable cause) {
		super(message, cause);
	}

}
