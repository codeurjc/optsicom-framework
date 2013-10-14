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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionsUtil {

	public static double average(List<? extends Number> values) {
		double sum = 0;
		for (Number value : values) {
			sum += value.doubleValue();
		}
		return sum / values.size();
	}

	public static double max(List<? extends Number> values) {
		double max = Double.NEGATIVE_INFINITY;
		for (Number value : values) {
			double doubleValue = value.doubleValue();
			if (doubleValue > max) {
				max = doubleValue;
			}
		}
		return max;
	}

	public static <E extends Comparable<E>> int maxIndex(List<E> values) {
		int index = 0;
		E best = values.get(0);
		if (values.size() > 1) {
			for (int i = 1; i < values.size(); i++) {				
				E value = values.get(i);
				if(best.compareTo(value) < 0){
					best = value;
					index = i;
				}
			}
		}
		return index;
	}
	
	public static <E extends Comparable<E>> E maxObject(List<E> values) {
		E best = values.get(0);
		if (values.size() > 1) {
			for (int i = 1; i < values.size(); i++) {				
				E value = values.get(i);
				if(best.compareTo(value) < 0){
					best = value;
				}
			}
		}
		return best;
	}
	
	public static <E extends Comparable<E>> E maxObject(List<E> values, Comparator<E> comparator) {
		E best = values.get(0);
		if (values.size() > 1) {
			for (int i = 1; i < values.size(); i++) {				
				E value = values.get(i);
				if(comparator.compare(best, value) < 0){
					best = value;
				}
			}
		}
		return best;
	}
	
	public static double min(List<? extends Number> values) {
		double min = Double.POSITIVE_INFINITY;
		for (Number value : values) {
			double doubleValue = value.doubleValue();
			if (doubleValue < min) {
				min = doubleValue;
			}
		}
		return min;
	}

	public static double sum(List<? extends Number> values) {
		double sum = 0;
		for (Number value : values) {
			sum += value.doubleValue();
		}
		return sum;
	}

	/**
	 * Create a list of natural numbers in the interval [from, to]
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<Integer> createNaturalsList(int from, int to) {
		List<Integer> naturals = new ArrayList<Integer>();
		int numNaturals = to - from + 1;
		for (int i = 0; i < numNaturals; i++) {
			naturals.add(from + i);
		}
		return naturals;
	}

	public static List<Integer> generateRandomNaturals(int numNaturals,
			int from, int to) {
		List<Integer> naturals = new ArrayList<Integer>();
		int currentNumNaturals = 0;
		for (Integer natural : RandomList.create(createNaturalsList(from, to))) {
			naturals.add(natural);
			currentNumNaturals++;
			if (currentNumNaturals == numNaturals) {
				break;
			}
		}
		return naturals;
	}

	public static <E> List<E> selectRandonmly(List<E> elements, int numElements) {
		List<E> output = new ArrayList<E>(elements);
		Collections.shuffle(output);
		return output.subList(0, numElements);
	}

	/**
	 * Ordena de menor a mayor y devuelve en el array de enteros el índice que
	 * tenía el primer valor, en la primera posición. El índice que tenía el
	 * segundo valor, en la segunda posición. etc...
	 * 
	 * @param inArray
	 * @return
	 */
	public static <T extends Comparable<? super T>> int[] sort(List<T> list) {
		return ObjectListSorter.sort(list, null);
	}

	/**
	 * Ordena de menor a mayor y devuelve en el array de enteros el índice que
	 * tenía el primer valor, en la primera posición. El índice que tenía el
	 * segundo valor, en la segunda posición. etc...
	 * 
	 * @param inArray
	 * @return
	 */
	public static <T extends Comparable<? super T>> int[] sort(List<T> list,
			Comparator<T> comparator) {
		return ObjectListSorter.sort(list, comparator);
	}

	public static <E> void swapByMap(List<E> list, int[] map) {
		List<E> aux = new ArrayList<E>(list);
		for (int i = 0; i < map.length; i++) {
			list.set(i, aux.get(map[i]));
		}
	}

	public static <T extends Comparable<? super T>, E> void sortListsByListA(
			List<T> listA, Comparator<T> comparator, List<E> listB) {
		int[] map = sort(listA, comparator);
		swapByMap(listB, map);
	}

	public static <T extends Comparable<? super T>, E> void sortListsByListA(
			List<T> listA, List<E> listB) {
		int[] map = sort(listA);
		swapByMap(listB, map);
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
	public static <T extends Comparable<? super T>> int[] mapToSort(List<T> list) {
		return sort(new ArrayList<T>(list));
	}

}
