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
package es.optsicom.lib;

import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.util.description.Descriptive;

public interface Instance extends Descriptive {

	public String getId();

	public InstanceFile getInstanceFile();

	public Problem getProblem();

	public InstanceDescription createInstanceDescription();

}
