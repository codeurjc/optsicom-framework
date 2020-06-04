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

import es.optsicom.lib.util.RandomManager;

public class RandomMatrixGraphGenerator {

	public MatrixGraph createGraphFloat(int numberNodes, float minValue, float maxValue) {

		float weights[][] = new float[numberNodes - 1][];
		for (int i = 0; i < numberNodes - 1; i++) {
			weights[i] = new float[i + 1];
		}

		for (int i = 0; i < numberNodes; i++) {
			for (int j = i + 1; j < numberNodes; j++) {
				weights[j - 1][i] = createWeight(minValue, maxValue);
			}
		}

		return new MatrixGraph(weights);

	}

	private float createWeight(float minValue, float maxValue) {
		return (float) (RandomManager.nextDouble() * (maxValue - minValue) + minValue);
	}

	public MatrixGraph createGraphInt(int numberNodes, int minValue, int maxValue) {
		float weights[][] = new float[numberNodes - 1][];
		for (int i = 0; i < numberNodes - 1; i++) {
			weights[i] = new float[i + 1];
		}

		for (int i = 0; i < numberNodes; i++) {
			for (int j = i + 1; j < numberNodes; j++) {
				weights[j - 1][i] = RandomManager.nextInt(maxValue + 1 - minValue) + minValue;
			}
		}
		return new MatrixGraph(weights);
	}

}
