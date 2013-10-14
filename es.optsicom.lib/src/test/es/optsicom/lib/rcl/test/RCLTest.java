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

import java.util.Arrays;
import java.util.Random;

import es.optsicom.lib.util.RandomManager;

/**
 * Este test ha servido para comprobar experimentalmente cual puede ser la mejor estrategia con la RCL
 * Dependiendo de si el array de valores está ya ordenado o no, es más interesante una estrategia u otra.
 * Si el array de valores está ya ordenado, es mejor usar una búsqueda binaria, en cambio si el array no
 * está ordenado, es mejor NO ordenarle para seleccionar con RCL. 
 * @author mica
 *
 */
public class RCLTest {

	private static final int NUM_VALUES = 500;
	private static final int NUM_TESTS = 1000;
	private static final int NUM_TESTS_INSTANCE = 1000;

	public static void main(String[] args) {

		RandomManager.setSeed(2000);

		Random r = RandomManager.getRandom();

		long time = 0;

		for (int i = 0; i < NUM_TESTS; i++) {

			double[] values = new double[NUM_VALUES];
			for (int j = 0; j < values.length; j++) {
				values[j] = r.nextDouble();
			}

			Arrays.sort(values);

			long startTime = System.currentTimeMillis();
			for (int k = 0; k < NUM_TESTS_INSTANCE; k++) {

				int index = RCL2.selectElem(values, 0.8);
				//System.out.println(index);

			}
			time += System.currentTimeMillis() - startTime;

		}

		System.out.println("Duration: " + time);

	}

}
