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
import java.util.List;

public class MathUtil {

	public static double min(double e1, double e2, double e3) {
		double min = e1;
		if (e2 < min) {
			min = e2;
		}
		if (e3 < min) {
			min = e3;
		}
		return min;
	}

	/**
	 * Selects an index proportional to the probabilities values.
	 * 
	 * @param probabilities
	 * @return
	 */
	public static int selectIndex(double[] probabilities) {

		double[] weights = new double[probabilities.length];
		double acummulativeWeight = 0;

		int i = 0;
		for (double probability : probabilities) {
			acummulativeWeight += probability;
			weights[i] = acummulativeWeight;
			i++;
		}

		double randomWeight = RandomManager.nextDouble() * acummulativeWeight;
		int index = Arrays.binarySearch(weights, randomWeight);

		if (index > 0) {
			return index;
		} else {
			int insertionPoint = -index - 1;
			return insertionPoint;
		}
	}

	/**
	 * Selects an index inversely proportional to probabilities.
	 * 
	 * @param probabilities
	 * @return
	 */
	public static int selectIndexInversely(double[] probabilities) {
		double[] freqInv = new double[probabilities.length];
		for (int i = 0; i < freqInv.length; i++) {
			freqInv[i] = ArraysUtil.min(probabilities) - (probabilities[i] - ArraysUtil.max(probabilities));
		}

		return selectIndex(freqInv);
	}

	public static double nextDouble(double lowerBound, double upperBound) {
		return lowerBound + RandomManager.nextDouble() * (upperBound - lowerBound);
	}

	public static double meanValue(List<Double> list) {

		double sum = 0;
		for (Double value : list) {
			sum += value;
		}

		return sum / list.size();
	}

	public static boolean efectiveEquals(double valueA, double valueB) {
		return efectiveEquals(valueA, valueB, 0.00001);
	}

	public static boolean efectiveEquals(double valueA, double valueB, double epsilon) {
		return Math.abs(valueA - valueB) < epsilon;
	}

	public static boolean efectiveEqualsDouble(Double valueA, Double valueB, double epsilon) {
		if (valueA == null && valueB == null) {
			return true;
		} else if (valueA == null || valueB == null) {
			return false;
		} else {
			return Math.abs(valueA - valueB) < epsilon;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Comparable> T max(T... objects) {
		return ArraysUtil.max(objects);
	}

}
