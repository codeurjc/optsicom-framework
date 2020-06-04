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

public class MoveRandomPositionsTest {

	public static void main(String[] args) {

		int size = 10;

		int[] array = new int[size];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}

		System.out.println(Arrays.toString(array));
		array = ArraysUtil.moveRandomPositions(array);
		System.out.println(Arrays.toString(array));

	}

}
