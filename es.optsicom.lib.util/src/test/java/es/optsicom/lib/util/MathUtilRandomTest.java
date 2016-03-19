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

public class MathUtilRandomTest {

	public static void main(String[] args) {

		double[] props = new double[] { 0.3, 0.3, 0.3, 0.1 };

		double[] generated = new double[props.length];

		int numGenerations = 10000000;

		for (int i = 0; i < numGenerations; i++) {
			int index = MathUtil.selectIndex(props);
			generated[index] += 1.0 / numGenerations;
		}

		System.out.println("Props: " + Arrays.toString(props));
		System.out.println("Generated: " + Arrays.toString(generated));

	}

}
