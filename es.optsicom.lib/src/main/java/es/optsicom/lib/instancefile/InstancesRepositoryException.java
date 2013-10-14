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
package es.optsicom.lib.instancefile;

public class InstancesRepositoryException extends RuntimeException {

	public InstancesRepositoryException() {
	}

	public InstancesRepositoryException(String message) {
		super(message);
	}

	public InstancesRepositoryException(Throwable cause) {
		super(cause);
	}

	public InstancesRepositoryException(String message, Throwable cause) {
		super(message);
		this.initCause(cause);
	}

}
