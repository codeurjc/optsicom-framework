package es.optsicom.lib.util;

import java.util.Arrays;
import java.util.List;

public class CombinationGeneratorTest2 {

	public static void main(String[] args) {
		List<String> values = Arrays.asList("e1", "e2", "e3", "e4", "e5", "e6");
		
		List<List<String>> combinations = CombinationGenerator.createCombinations(values);
		
		for(List<String> comb : combinations){
			System.out.println(comb);
		}
	}
}
