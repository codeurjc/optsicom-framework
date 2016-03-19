package es.optsicom.lib.util;

import java.util.Arrays;
import java.util.List;

public class ObjectListSorterTest {

	public static void main(String[] args) {

		List<Integer> ints = Arrays.asList(3, 4, 1, 2);
		List<String> values = Arrays.asList("BB", "AA", "ZZ", "CC");

		CollectionsUtil.sortListsByListA(values, ints);

		System.out.println(values);
		System.out.println(ints);

	}

}
