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

public class AsymmetricRandomMatrixGraphGenerator {

	public AsymmetricMatrixGraph createGraphFloat(int numberNodes, float minValue, float maxValue) {

		float weights[][] = new float[numberNodes][numberNodes];

		for (int i = 0; i < numberNodes; i++) {
			for (int j = 0; j < numberNodes; j++) {
				weights[j][i] = createFloatWeight(minValue, maxValue);
			}
		}

		return new AsymmetricMatrixGraph(weights);

	}

	private float createFloatWeight(float minValue, float maxValue) {
		return (float) (RandomManager.nextDouble() * (maxValue - minValue) + minValue);
	}

	public AsymmetricMatrixGraph createGraphInt(int numberNodes, int minValue, int maxValue) {

		float weights[][] = new float[numberNodes][numberNodes];

		for (int i = 0; i < numberNodes; i++) {
			for (int j = 0; j < numberNodes; j++) {
				weights[j][i] = createIntWeight(minValue, maxValue);
			}
		}

		return new AsymmetricMatrixGraph(weights);

	}

	private float createIntWeight(int minValue, int maxValue) {
		return RandomManager.nextInt(maxValue + 1 - minValue) + minValue;
	}

}
