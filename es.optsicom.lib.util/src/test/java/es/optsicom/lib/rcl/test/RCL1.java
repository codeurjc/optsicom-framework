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

import es.optsicom.lib.util.RandomManager;

public class RCL1 {

	public static int selectElem(double[] sortedValues, double closeToBest) {

		int size = sortedValues.length;
		double maxValue = sortedValues[size - 1];
		double thresold = closeToBest * maxValue;

		int i;
		// Esto se podría hacer con una búsqueda binaria
		for (i = size - 1; i > 0; i--) {
			if (sortedValues[i] < thresold) {
				break;
			}
		}

		// The last test is not included in RCL
		i++;
		// System.out.println("i:" + i);

		return RandomManager.nextInt(size - i) + i;
	}

}
