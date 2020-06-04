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

public class MatrixGraphBuilder {

	private final float weights[][];
	private final int n;

	public MatrixGraphBuilder(int n) {
		this.n = n;
		weights = new float[n - 1][];
		for (int i = 0; i < n - 1; i++) {
			weights[i] = new float[i + 1];
		}
	}

	public float getWeight(int i, int j) {
		if (i == j) {
			return 0;
		} else {
			return weights[Math.max(i, j) - 1][Math.min(i, j)];
		}
	}

	public void setWeight(int i, int j, float weight) {
		if (i == j) {
			return;
		} else {
			weights[Math.max(i, j) - 1][Math.min(i, j)] = weight;
		}
	}

	public MatrixGraph createMatrixGraph() {
		return new MatrixGraph(weights, n);
	}

}
