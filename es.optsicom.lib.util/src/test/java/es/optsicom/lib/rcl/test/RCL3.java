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

import es.optsicom.lib.util.ArraysUtil;

public class RCL3 {

	public static int selectElem(double[] values, double closeToBest) {

		int[] changes = ArraysUtil.sort(values);

		int index = RCL2.selectElem(values, closeToBest);

		return changes[index];
	}

}
