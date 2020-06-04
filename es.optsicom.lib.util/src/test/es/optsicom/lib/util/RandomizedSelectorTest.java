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

import es.optsicom.lib.util.RandomizedSelector;

public class RandomizedSelectorTest {

	private static final int NUM_CREATIONS = 100000;

	public static void main(String[] args) {

		double[] values = { 5, 3, 1, 0.3, 10, 22, 45, 1, 3, 4 };
		int[] counts = new int[values.length];

		for (int i = 0; i < NUM_CREATIONS; i++) {
			counts[RandomizedSelector.selectRandomly(values)]++;
		}

		double total = 0;
		for (double value : values) {
			total += value;
		}

		for (double value : values) {
			double percentValue = (((float) value) / total) * 100;
			System.out.format("%.2f ", percentValue);
		}

		System.out.println();

		for (double value : counts) {
			double percentValue = (((float) value) / NUM_CREATIONS) * 100;
			System.out.format("%.2f ", percentValue);
		}

	}

}
