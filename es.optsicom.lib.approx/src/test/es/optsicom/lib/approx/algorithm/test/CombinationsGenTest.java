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
package es.optsicom.lib.approx.algorithm.test;

import java.util.List;

import es.optsicom.lib.approx.algorithm.ss.SSCombinationsGen;

public class CombinationsGenTest {

	public static void main(String[] args) {

		SSCombinationsGen combinations = new SSCombinationsGen(4, 10);

		System.out.println("Total Groups");
		for (List<Integer> group : combinations.getAllGroups()) {
			System.out.println(group);
		}

		System.out.println("Groups Containig Index");
		for (int i = 0; i < 10; i++) {
			System.out.println(i + ":");
			for (List<Integer> group : combinations.getGroupsContainingIndex(i)) {
				System.out.println("      " + group);
			}
		}

		System.out.println("0, 3, 5");
		for (List<Integer> group : combinations.getGroupsContainingIndexes(0, 3, 5)) {
			System.out.println(group);
		}

	}

}
