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
package es.optsicom.lib.graph.matrix;

import java.io.IOException;

public class FormatException extends IOException {

	private static final long serialVersionUID = 3883039187054745552L;

	public FormatException(String message) {
		super(message);
	}

	public FormatException(String message, Throwable cause) {
		super(message);
		this.initCause(cause);
	}

	public FormatException(Throwable cause) {
		super();
		initCause(cause);
	}

	public FormatException() {
		super();
	}

}
