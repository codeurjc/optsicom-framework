package es.optsicom.lib.util;

import java.util.Arrays;

public class ArraysUtilFindTest {

	public static void main(String[] args) {
		int[] values = { 1, 2, 2, 3, 4, 2, 5 };
		int[] indexes = ArraysUtil.find(values, 2, 0, values.length);
		System.out.println(Arrays.toString(indexes));

		indexes = ArraysUtil.find(values, 2, 1, values.length);
		System.out.println(Arrays.toString(indexes));

		indexes = ArraysUtil.find(values, 2, 2, values.length);
		System.out.println(Arrays.toString(indexes));

		indexes = ArraysUtil.find(values, 2, 3, values.length);
		System.out.println(Arrays.toString(indexes));

		indexes = ArraysUtil.find(values, 2, 1, 2);
		System.out.println(Arrays.toString(indexes));
	}

}
