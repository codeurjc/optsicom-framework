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

import java.util.Collection;

import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class DistanceCalc<S extends Solution<I>, I extends Instance> implements Descriptive {

	public abstract double calculateDistance(S sol, S otherSol);

	public double calculateDistance(S sol, Collection<S> solutions) {

		int distance = 0;
		for (S refSol : solutions) {
			distance += calculateDistance(sol, refSol);
		}
		return distance;
	}

	public double calculateDistance(S sol, S[] solutions) {

		int distance = 0;
		for (S refSol : solutions) {
			distance += calculateDistance(sol, refSol);
		}
		return distance;
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}
}
