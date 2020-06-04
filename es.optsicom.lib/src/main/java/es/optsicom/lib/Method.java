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

import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.Properties;

public interface Method<S extends Solution<I>, I extends Instance> extends Descriptive {

	@Id
	public String getName();

	public Properties getProperties();

	public MethodDescription createMethodDescription();

	public void setInstance(I instance);

	public I getInstance();

	public void removeInstance();

	public ExecutionResult execute(long timeLimit);

	public ExecutionResult execute();

}
