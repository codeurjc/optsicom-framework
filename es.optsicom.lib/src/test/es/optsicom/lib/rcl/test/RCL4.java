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

public class RCL4 {

	public static int selectElem(double[] values, double closeToBest) {

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

		return indexes[RandomManager.getRandom().nextInt(numIndexes)];
	}

}
