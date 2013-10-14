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
package es.optsicom.lib.tablecreator.test;

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.pr.ListDoubleArrayEncapsulator;

public class ListDoubleArrayEncapsulatorTest {

	public static void main(String[] args) {

		List<double[]> values = Arrays.asList(new double[] { 5, 2, 3 }, new double[] { 6, 7, 8 }, new double[] { 1, 2,
		        3 }, new double[] { 9, 10, 11 });

		ListDoubleArrayEncapsulator enc = new ListDoubleArrayEncapsulator(values);

		for (int i = 0; i < 3; i++) {

			System.out.println("Index: " + i);
			enc.setIndex(i);

			for (Number n : enc) {
				System.out.println(n);
			}

		}

	}

}
