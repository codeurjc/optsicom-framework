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
package es.optsicom.lib.approx.constructive.test;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.constructive.Constructive;

public class IntervalConsTest {
	// public static void main(String[] args) {
	// Constructive ic = new DummyIsoIntervalConstructive(null, 4);
	//
	// //Test by Time
	// System.out.println("Test by Time");
	// ic.initSolutionCreationByTime(5000);
	//
	// long finishTime = System.currentTimeMillis() + 5000;
	// while(System.currentTimeMillis() < finishTime){
	//
	// ic.createSolution();
	// try {
	// Thread.sleep(100);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// //Test by Num
	// System.out.println("Test by Num");
	// ic.initSolutionCreationByNum(50);
	//
	// for(int i=0; i<50; i++){
	// ic.createSolution();
	// }
	//
	// }

	public static void main(String[] args) {
		Constructive<Solution<Instance>, Instance> ic = new DummyNonIsoInterval<Solution<Instance>, Instance>(
				new float[] { 0.4f, 0.4f, 0.1f, 0.1f });

		// Test by Time
		System.out.println("Test by Time");
		ic.initSolutionCreationByTime(10000);

		long finishTime = System.currentTimeMillis() + 10000;
		while (System.currentTimeMillis() < finishTime) {

			ic.createSolution();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// Test by Num
		System.out.println("Test by Num");
		ic.initSolutionCreationByNum(50);

		for (int i = 0; i < 50; i++) {
			ic.createSolution();
		}

	}
}
