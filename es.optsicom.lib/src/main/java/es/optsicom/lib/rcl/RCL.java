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
package es.optsicom.lib.rcl;

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.util.IWeighed;
import es.optsicom.lib.util.RandomManager;

public class RCL {

	public static int selectOrderedValues(double[] sortedValues, double closeToBest) {

		int size = sortedValues.length;
		double maxValue = sortedValues[size - 1];
		double thresold = closeToBest * maxValue;

		int i = Arrays.binarySearch(sortedValues, thresold);
		if (i >= 0) {

			//If two equals values are in the array, it selects the smaller
			while (i > 0) {
				if (sortedValues[i] == sortedValues[i - 1]) {
					i--;
				}
			}

		} else {
			i = -i - 1;
		}

		//System.out.println("i:" + i);

		return RandomManager.nextInt(size - i) + i;
	}

	public static int selectNotOrderedValues(double[] values, double closeToBest) {

		int size = values.length;

		double maxValue = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < size; i++) {
			if (values[i] > maxValue) {
				maxValue = values[i];
			}
		}

		double thresold = closeToBest * maxValue;

		int[] indexes = new int[size];
		int numIndexes = 0;
		for (int i = 0; i < size; i++) {
			if (values[i] >= thresold) {
				indexes[numIndexes] = i;
				numIndexes++;
			}
		}

		return indexes[RandomManager.nextInt(numIndexes)];
	}

	public static int selectNotOrderedValues(List<? extends IWeighed> values, double closeToBest) {

		int size = values.size();

		double maxValue = Double.NEGATIVE_INFINITY;
		for (IWeighed value : values) {
			if (value.getWeight() > maxValue) {
				maxValue = value.getWeight();
			}
		}

		double thresold = closeToBest * maxValue;

		int[] indexes = new int[size];
		int numIndexes = 0;
		int i = 0;
		for (IWeighed value : values) {
			if (value.getWeight() >= thresold) {
				indexes[numIndexes] = i;
				numIndexes++;
			}
			i++;
		}

		return indexes[RandomManager.nextInt(numIndexes)];
	}

}
