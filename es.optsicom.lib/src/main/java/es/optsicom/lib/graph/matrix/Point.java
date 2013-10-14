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

public class Point {

	private float[] dimensions;

	public Point(float[] dimensiones) {
		dimensions = dimensiones;
	}

	public float distanceTo(Point point) {
		float[] dimensionesPunto = point.dimensions;
		float distancia = 0;
		for (int i = 0; i < dimensions.length; i++) {
			distancia += Math.pow(dimensionesPunto[i] - dimensions[i], 2);
		}
		return (float) Math.sqrt(distancia);
	}

	public float[] getDimensions() {
		return dimensions;
	}

	public void setDimensions(float[] dimensions) {
		this.dimensions = dimensions;
	}
}