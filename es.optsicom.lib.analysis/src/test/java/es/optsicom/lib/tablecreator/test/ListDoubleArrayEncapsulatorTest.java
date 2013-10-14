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

		List<Double[]> values = Arrays.asList(new Double[] { 5.0, 2.0, 3.0 }, new Double[] { 6.0, 7.0, 8.0 }, new Double[] { 1.0, 2.0,
		        3.0 }, new Double[] { 9.0, 10.0, 11.0 });

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
