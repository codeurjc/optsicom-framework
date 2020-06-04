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
package es.optsicom.lib.util;

import java.util.Arrays;
import java.util.Random;

import es.optsicom.lib.util.RandomManager;

public class RandomizedSelectorTest2 {

	private static final int NUM_CREATIONS = 10000000;

	public static void main(String[] args) {

		double[] values = { 5, 3, 1, 0.3, 10, 22, 45, 1, 3, 4 };

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < NUM_CREATIONS; i++) {

			//			RandomizedSelector<Object> rs = new RandomizedSelector<Object>();
			//
			//			for (double element : values) {
			//				rs.add(new Object(), (float) element);
			//			}
			//
			//			rs.selectElement();

			selectRandomly(values);
		}

		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Duration: " + duration);

	}

	private static int selectRandomly(double[] values) {

		double[] acummulativeValues = new double[values.length];
		double acummulativeWeight = 0;

		for (int i = 0; i < values.length; i++) {
			acummulativeWeight += values[i];
			acummulativeValues[i] = acummulativeWeight;
		}

		Random r = RandomManager.getRandom();

		double randomWeight = r.nextDouble() * acummulativeWeight;
		int index = Arrays.binarySearch(values, randomWeight);

		if (index > 0) {
			return index;
		} else {
			int insertionPoint = -index - 1;
			if (insertionPoint == values.length) {
				return values.length - 1;
			} else {
				return insertionPoint;
			}
		}
	}
}
