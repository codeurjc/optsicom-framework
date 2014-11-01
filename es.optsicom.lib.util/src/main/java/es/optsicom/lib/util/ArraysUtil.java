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

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class ArraysUtil {

	public static double max(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	public static Double max(Double[] values) {
		Double max = null;
		for (Double value : values) {
			if (value != null) {
				if (max == null || value > max) {
					max = value;
				}
			}
		}
		return max;
	}

	public static <T extends Comparable> T max(T[] values) {
		T max = null;
		for (T value : values) {
			if (value != null) {
				if (max == null || max.compareTo(value) < 0) {
					max = value;
				}
			}
		}
		return max;
	}

	public static double maxWithoutInf(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double value : values) {
			if (value != Double.POSITIVE_INFINITY
					&& value != Double.NEGATIVE_INFINITY) {
				if (value > max) {
					max = value;
				}
			}
		}
		return max;
	}

	public static double minWithoutInf(double[] values) {
		double min = Double.POSITIVE_INFINITY;
		for (double value : values) {
			if (value != Double.POSITIVE_INFINITY
					&& value != Double.NEGATIVE_INFINITY) {
				if (value < min) {
					min = value;
				}
			}
		}
		return min;
	}

	public static int max(int[] values) {
		int max = Integer.MIN_VALUE;
		for (int value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	public static double min(double[] values) {
		double min = Double.POSITIVE_INFINITY;
		for (double value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	public static double minAbs(double[] values) {
		double min = Double.POSITIVE_INFINITY;
		double realMin = Double.POSITIVE_INFINITY;
		for (double value : values) {
			if (Math.abs(value) < min) {
				min = Math.abs(value);
				realMin = value;
			}
		}
		return realMin;
	}

	public static Double min(Double[] values) {
		Double min = null;
		for (Double value : values) {
			if (value != null) {
				if (min == null || value < min) {
					min = value;
				}
			}
		}
		return min;
	}

	public static int min(int[] values) {
		int min = Integer.MAX_VALUE;
		for (int value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	/**
	 * Returns the position of the minimum value of the array. If there are more
	 * than one values that conforms to be the minimum then the first one is
	 * returned.
	 * 
	 * @param values
	 *            The array
	 * @return the first of the values that conforms to be the minimum
	 */
	public static int indexOfMin(int[] values) {
		int min = Integer.MAX_VALUE;
		int position = -1;
		for (int index = 0; index < values.length; index++) {
			if (values[index] < min) {
				position = index;
				min = values[index];
			}
		}
		return position;
	}

	/**
	 * Returns the position of the maximun value of the array. If there are more
	 * than one values that conforms to be the maximun then the first one is
	 * returned.
	 * 
	 * @param values
	 *            The array
	 * @return the first of the values that conforms to be the maximun
	 */
	public static int indexOfMax(int[] values) {
		int max = Integer.MIN_VALUE;
		int position = -1;
		for (int index = 0; index < values.length; index++) {
			if (values[index] > max) {
				position = index;
				max = values[index];
			}
		}
		return position;
	}

	public static double sum(double[] values) {
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		return sum;
	}

	public static Double sum(Double[] values) {
		double sum = 0;
		for (Double value : values) {
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}

	public static int sum(int[] values) {
		int sum = 0;
		for (int value : values) {
			sum += value;
		}
		return sum;
	}

	public static int[] minmax(int[] values) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int value : values) {
			if (value < min) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
		}
		return new int[] { min, max };
	}

	public static double[] minmax(double[] values) {
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		for (double value : values) {
			if (value < min) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
		}
		return new double[] { min, max };
	}

	public static double average(double[] values) {
		double acc = 0;
		for (double value : values) {
			acc += value;
		}
		return acc / values.length;
	}

	public static Double average(Double[] values) {
		double acc = 0;
		int notNull = 0;
		for (Double value : values) {
			if (value != null) {
				acc += value;
				notNull++;
			}
		}
		return acc / notNull;
	}

	public static int[] sort(double[] inArray, int begin, int end) {
		return Sorter.sort(inArray, begin, end);
	}

	/**
	 * Ordena de menor a mayor y devuelve en el array de enteros el índice que
	 * tenía el primer valor, en la primera posición. El índice que tenía el
	 * segundo valor, en la segunda posición. etc...
	 * 
	 * @param inArray
	 * @return
	 */
	public static int[] sort(double[] inArray) {
		return Sorter.sort(inArray);
	}

	public static void reverse(int[] values) {
		for (int i = 0; i < values.length / 2; i++) {
			int index = values.length - i - 1;
			int aux = values[i];
			values[i] = values[index];
			values[index] = aux;
		}
	}

	public static void reverse(double[] values) {
		for (int i = 0; i < values.length / 2; i++) {
			int index = values.length - i - 1;
			double aux = values[i];
			values[i] = values[index];
			values[index] = aux;
		}
	}

	public static void reverse(Double[] values) {
		for (int i = 0; i < values.length / 2; i++) {
			int index = values.length - i - 1;
			Double aux = values[i];
			values[i] = values[index];
			values[index] = aux;
		}
	}

	public static void mapTo(double[] values, double destMin, double destMax) {
		double[] minmax = minmax(values);
		double min = minmax[0];
		double max = minmax[1];
		mapTo(values, destMin, destMax, min, max);
	}

	public static void mapTo(double[] values, double srcMin, double srcMax,
			double destMin, double destMax) {
		double targetRange = destMax - destMin;
		double sourceRange = srcMax - srcMin;
		double ratio = targetRange / sourceRange;
		for (int i = 0; i < values.length; i++) {
			values[i] = ((values[i] - srcMin) * ratio) + destMin;
		}
	}

	public static void mapTo(Double[] values, double srcMin, double srcMax,
			double destMin, double destMax) {
		Double targetRange = destMax - destMin;
		Double sourceRange = srcMax - srcMin;
		Double ratio = targetRange / sourceRange;
		for (int i = 0; i < values.length; i++) {
			values[i] = ((values[i] - srcMin) * ratio) + destMin;
		}
	}

	public static void add(double[] array, double[] sumValues) {
		if (array.length != sumValues.length) {
			throw new InvalidParameterException(
					"The arrays must have the same length.");
		}
		for (int i = 0; i < array.length; i++) {
			array[i] += sumValues[i];
		}

	}

	public static int[] moveRandomPositions(int[] array) {

		int lenght = array.length;
		int[] result = new int[lenght];

		int positions = RandomManager.nextInt(lenght);

		for (int i = 0; i < lenght; i++) {
			result[(i + positions) % lenght] = array[i];
		}
		return result;
	}

	public static String toStringObj(Object obj) {

		String infoToSaveStr;
		if (obj == null) {
			infoToSaveStr = "null";
		} else if (obj.getClass().isArray()) {
			if (obj instanceof int[]) {
				infoToSaveStr = Arrays.toString((int[]) obj);
			} else if (obj instanceof double[]) {
				infoToSaveStr = Arrays.toString((double[]) obj);
			} else if (obj instanceof float[]) {
				infoToSaveStr = Arrays.toString((float[]) obj);
			} else if (obj instanceof short[]) {
				infoToSaveStr = Arrays.toString((short[]) obj);
			} else if (obj instanceof byte[]) {
				infoToSaveStr = Arrays.toString((byte[]) obj);
			} else if (obj instanceof boolean[]) {
				infoToSaveStr = Arrays.toString((boolean[]) obj);
			} else if (obj instanceof long[]) {
				infoToSaveStr = Arrays.toString((long[]) obj);
			} else if (obj instanceof char[]) {
				infoToSaveStr = Arrays.toString((char[]) obj);
			} else {
				infoToSaveStr = Arrays.deepToString((Object[]) obj);
			}
		} else {
			infoToSaveStr = obj.toString();
		}
		return infoToSaveStr;

	}

	public static double getBest(BestMode mode, double[] values) {
		if (mode == BestMode.MAX_IS_BEST) {
			return ArraysUtil.max(values);
		} else {
			return ArraysUtil.min(values);
		}
	}

	public static Double getBest(BestMode mode, Double[] values) {
		if (mode == BestMode.MAX_IS_BEST) {
			return ArraysUtil.max(values);
		} else {
			return ArraysUtil.min(values);
		}
	}

	public static int[] sort(double[] values, BestMode mode) {
		int[] newPositions = ArraysUtil.sort(values);

		if (mode == BestMode.MAX_IS_BEST) {
			ArraysUtil.reverse(newPositions);
			ArraysUtil.reverse(values);
		}

		return newPositions;
	}

	public static int[] sort(Double[] values, BestMode mode) {
		int[] newPositions = ArraysUtil.sort(values);

		if (mode == BestMode.MAX_IS_BEST) {
			ArraysUtil.reverse(newPositions);
			ArraysUtil.reverse(values);
		}

		return newPositions;
	}

	public static <T extends Comparable<? super T>> int[] sort(T[] array) {
		return ObjectSorter.sort(array);
	}

	public static int[] toIntArray(boolean[] selectedValues) {

		int numValues = 0;
		for (boolean value : selectedValues) {
			if (value) {
				numValues++;
			}
		}

		int[] intValues = new int[numValues];
		int numValue = 0;
		for (int i = 0; i < selectedValues.length; i++) {
			if (selectedValues[i]) {
				intValues[numValue] = i;
				numValue++;
			}
		}
		return intValues;
	}

	public static boolean[] toBooleanArray(int[] intArray, int n) {

		boolean[] array = new boolean[n];
		for (int element : intArray) {
			array[element] = true;
		}
		return array;
	}

	/**
	 * Returns the index of the first apparition of the target value into the
	 * given array. Returns -1 if the value is not found in the array.
	 * 
	 * This is a convenience method when the array is not sorted. If the array
	 * is sorted, the {@link Arrays#binarySearch(int[], int)} method is the
	 * preferred way to find values within an array.
	 * 
	 * @param values
	 *            The array to search
	 * @param target
	 *            The value the method searchs for within the array
	 * @return The index of the value within the array. If there are several
	 *         elements with the given value, the index of the first one is
	 *         returned. If the value cannot be found within the array, -1 is
	 *         returned.
	 */
	public static int firstIndexOf(int[] values, int target) {
		for (int index = 0; index < values.length; index++) {
			if (values[index] == target) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Swaps the positions index1 and index2 of inArray.
	 * 
	 * @param inArray
	 * @param index1
	 * @param index2
	 */
	public static void swap(int[] inArray, int index1, int index2) {
		int temp = inArray[index1];
		inArray[index1] = inArray[index2];
		inArray[index2] = temp;
	}

	/**
	 * Ordena de menor a mayor y devuelve en el array de enteros el índice que
	 * tenía el primer valor, en la primera posición. El índice que tenía el
	 * segundo valor, en la segunda posición. etc...
	 * 
	 * @param inArray
	 * @return
	 */
	public static int[] sort(int[] inArray) {
		return IntSorter.sort(inArray);
	}

	/**
	 * Ordena de menor a mayor y devuelve en el array de enteros el índice que
	 * tenía el primer valor, en la primera posición. El índice que tenía el
	 * segundo valor, en la segunda posición. etc...
	 * 
	 * @param inArray
	 * @return
	 */
	public static int[] sort(float[] inArray) {
		return FloatSorter.sort(inArray);
	}

	public static int[] toIntArrayFalse(boolean[] selectedValues) {
		int numValues = 0;
		for (boolean value : selectedValues) {
			if (!value) {
				numValues++;
			}
		}

		int[] intValues = new int[numValues];
		int numValue = 0;
		for (int i = 0; i < selectedValues.length; i++) {
			if (!selectedValues[i]) {
				intValues[numValue] = i;
				numValue++;
			}
		}
		return intValues;

	}

	/**
	 * Creates an array containing numElements naturals staring with 0 and
	 * ending with numElements-1
	 * 
	 * @param numElements
	 * @return
	 */
	public static int[] createNaturals(int numElements) {
		return createNaturals(0, numElements - 1);
	}

	public static int[] createNaturals(int from, int to) {
		int numNumbers = to - from + 1;
		int[] naturals = new int[numNumbers];
		for (int i = 0; i < numNumbers; i++) {
			naturals[i] = i + from;
		}
		return naturals;
	}

	public static boolean[] createFilled(boolean value, int numElems) {
		boolean[] array = new boolean[numElems];
		Arrays.fill(array, value);
		return array;
	}
	
	public static double[] createFilled(double value, int numElems) {
		double[] array = new double[numElems];
		Arrays.fill(array, value);
		return array;
	}

	public static int[] createFilled(int value, int numElems) {
		int[] array = new int[numElems];
		Arrays.fill(array, value);
		return array;
	}

	public static int[] concat(int value, int[] array) {
		int[] newArray = new int[array.length + 1];
		newArray[0] = value;
		System.arraycopy(array, 0, newArray, 1, array.length);
		return newArray;
	}

	public static double[] concat(double value, double[] array) {
		double[] newArray = new double[array.length + 1];
		newArray[0] = value;
		System.arraycopy(array, 0, newArray, 1, array.length);
		return newArray;
	}

	public static boolean contains(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}

	public static int[] createRandomNaturals(int from, int to) {
		int[] naturals = createNaturals(from, to);
		suffle(naturals);
		return naturals;
	}

	public static void suffle(int[] naturals) {
		for (int i = 0; i < naturals.length; i++) {
			int randomPosition = RandomManager.nextInt(naturals.length);
			int temp = naturals[i];
			naturals[i] = naturals[randomPosition];
			naturals[randomPosition] = temp;
		}
	}

	/**
	 * Sorts the array from lower to higher and apply the changes on the second
	 * parameter array
	 */
	public static void sort(double[] arrayToSort, int[] associatedArray) {

		int[] sortedPossitions = sort(arrayToSort);

		int[] clonedAssociatedArray = associatedArray.clone();
		for (int i = 0; i < arrayToSort.length; i++) {
			associatedArray[i] = clonedAssociatedArray[sortedPossitions[i]];
		}

	}

	public static String[] concat(String[] array, String... values) {

		String[] result = Arrays.copyOf(array, array.length + values.length);

		for (int i = array.length; i < result.length; i++) {
			result[i] = values[i - array.length];
		}

		return result;
	}

	/**
	 * Returns a map with the sorted positions of the values but without change
	 * the array. In the first position returns the index in the array of lowest
	 * value, in the second position returns the index of second lowest value,
	 * etc.
	 * 
	 * @param relativizedValues
	 * @return
	 */
	public static int[] mapToShort(double[] array) {
		return sort(array.clone());
	}

	/**
	 * Returns the indexes of the specified array for which the value is equal
	 * to value. The search is performed in the range [from, to).
	 * 
	 * @param array
	 * @param value
	 * @param from
	 * @param to
	 * @return the indexes of the specified array for which the value is equal
	 *         to value.
	 */
	public static int[] find(int[] array, int value, int from, int to) {
		int[] temp = new int[array.length];
		int index = 0;
		for (int i = from; i < to; i++) {
			if (array[i] == value) {
				temp[index] = i;
				index++;
			}
		}
		int[] result = new int[index];
		System.arraycopy(temp, 0, result, 0, index);
		return result;
	}

	/**
	 * Returns the indexes of the specified array for which the value is
	 * different to value. The search is performed in the range [from, to).
	 * 
	 * @param array
	 * @param value
	 * @param from
	 * @param to
	 * @return the indexes of the specified array for which the value is equal
	 *         to value.
	 */
	public static int[] findNotEqual(int[] array, int value, int from, int to) {
		int[] temp = new int[array.length];
		int index = 0;
		for (int i = from; i < to; i++) {
			if (array[i] != value) {
				temp[index] = i;
				index++;
			}
		}
		int[] result = new int[index];
		System.arraycopy(temp, 0, result, 0, index);
		return result;
	}

	public static int[] find(int[] array, int value) {
		return find(array, value, 0, array.length);
	}

	public static int[] copyShuffled(int[] elements) {
		int[] newArray = elements.clone();
		suffle(elements);
		return newArray;
	}

	public static Double[] createDoubleArray(int length) {
		Double[] result = new Double[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = 0d;
		}
		return result;
	}

	public static int[] toIntArray(List<Integer> values) {
		int[] result = new int[values.size()];
		for (int i = 0; i < values.size(); i++) {
			result[i] = values.get(i);
		}
		return result;
	}

	public static float[] toFloatArray(int[] values) {
		float[] result = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i];
		}
		return result;
	}

}
