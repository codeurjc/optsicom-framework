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

import java.util.Arrays;

import es.optsicom.lib.util.RandomManager;

/**
 * Opción recomendada cuando los valores están ordenados
 * @author mica
 *
 */
public class RCL2 {

	public static int selectElem(double[] sortedValues, double closeToBest) {

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

		return RandomManager.getRandom().nextInt(size - i) + i;
	}

}
