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
package es.optsicom.lib.approx.constructive;

import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.Properties;

public interface Constructive<S extends Solution<I>, I extends Instance> extends Descriptive {

	public void initSolutionCreation();

	public void initSolutionCreationByNum(int numSolutions);

	public void initSolutionCreationByTime(long millis);

	public S createSolution();

	public boolean isDeterminist();

	public List<S> createSolutions(int numSolutions);

	public List<S> createSolutionsInTime(long millis);

	public I getInstance();

	public void setInstance(I instance);

	public void removeInstance();

	public Properties getProperties();

}