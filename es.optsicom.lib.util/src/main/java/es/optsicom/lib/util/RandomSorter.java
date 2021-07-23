package es.optsicom.lib.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RandomSorter {

	public static <E> List<E> sort(List<E> items, Comparator<E> comparator, float alpha) {
		List<E> iterableList = new ArrayList<>(items);
		List<E> sortedList = new ArrayList<>();
		
		// Sort list using comparator
		Collections.sort(iterableList, comparator);
		
		// Create sorted list using alpha
		while (iterableList.size() > 0) {
			int index = selectValue(iterableList.size(), alpha);

			E element = iterableList.remove(index);
			sortedList.add(element);
		}

		return sortedList;
	}

	public static <E> List<E> sort(List<E> items, float alpha) {
		List<E> iterableList = new ArrayList<>(items);
		List<E> sortedList = new ArrayList<>();

		// Create sorted list using alpha
		while (iterableList.size() > 0) {
			int index = selectValue(iterableList.size(), alpha);

			E element = iterableList.remove(index);
			sortedList.add(element);
		}

		return sortedList;
	}

	private static int selectValue(int size, float alpha) {
		int min = 0;
		int max = (int) Math.ceil((size - 1) * alpha);

		return RandomManager.nextInt(min, max);
	}

}
