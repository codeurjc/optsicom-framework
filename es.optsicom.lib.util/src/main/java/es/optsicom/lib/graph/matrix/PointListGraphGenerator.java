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

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.util.RandomManager;

public class PointListGraphGenerator {

	public List<Point> generateGloverPoints(int numPoints) {
		return generateGloverPoints(numPoints, -1);
	}

	public List<Point> generateGloverPoints(int numPoints, int numDimensions) {
		List<Point> puntos = new ArrayList<Point>(numPoints);
		if (numDimensions == -1) {
			numDimensions = RandomManager.nextInt(20) + 2;
		}
		for (int i = 0; i < numPoints; i++) {
			float[] dimensiones = new float[numDimensions];
			for (int j = 0; j < numDimensions; j++) {
				dimensiones[j] = (float) RandomManager.nextDouble() * 100;
			}
			puntos.add(new Point(dimensiones));
		}
		return puntos;
	}
}
