package es.optsicom.lib.util;

public class Normalizer {
	
	public enum Normalization {
		ZERO_ONE,
		MINUS_ONE_ONE
	}
	
	public static double[] normalize(double[] values, Normalization kind) {
		if (kind == Normalization.ZERO_ONE) {
			return normalize(values);
		} else {
			return normalizeNegatives(values);
		}
	}
	
	private static double[] normalize(double[] values) {
		
		double max = ArraysUtil.max(values);
		double[] normalized = new double[values.length];
		for(int i = 0; i < values.length; i++) {
			normalized[i] = values[i] / max;
		}
		
		return normalized;
	}

	private static double[] normalizeNegatives(double[] values) {
		
		double max = ArraysUtil.max(values);
		double[] normalized = new double[values.length];
		for(int i = 0; i < values.length; i++) {
			normalized[i] = (values[i] / max) * 2 -1;
		}
		
		return normalized;
	}
}
