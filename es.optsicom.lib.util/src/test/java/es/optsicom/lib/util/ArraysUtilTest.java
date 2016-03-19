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

public class ArraysUtilTest {

	public static void main(String[] args) {

		int[] values = { 1, 2, 3, 4, 5, 6, 7 };

		System.out.println(Arrays.toString(values));
		ArraysUtil.reverse(values);
		System.out.println(Arrays.toString(values));

	}

}
