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
package es.optsicom.lib.rcl.test;

import java.util.Random;

import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.RandomManager;

public class SorterTest {

	private static final int NUM_VALUES = 500;
	private static final int NUM_TESTS = 1000;
	private static final int NUM_TESTS_INSTANCE = 100;

	public static void main(String[] args) {

		RandomManager.setSeed(2000);

		

		long time = 0;

		for (int i = 0; i < NUM_TESTS; i++) {

			double[] values = new double[NUM_VALUES];
			for (int j = 0; j < values.length; j++) {
				values[j] = RandomManager.nextDouble();
			}

			long startTime = System.currentTimeMillis();
			for (int k = 0; k < NUM_TESTS_INSTANCE; k++) {
				double[] clonedValues = values.clone();
				ArraysUtil.sort(clonedValues);
			}
			time += System.currentTimeMillis() - startTime;

		}

		System.out.println("Duration: " + time);

	}

}
