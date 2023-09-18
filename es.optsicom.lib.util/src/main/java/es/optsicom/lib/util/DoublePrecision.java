package es.optsicom.lib.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoublePrecision {

	private final static double EPSILON = 0.000001;

	public static boolean equals(double a, double b) {
		return equals(a, b, EPSILON);
	}

	public static boolean greaterThan(double a, double b) {
		return greaterThan(a, b, EPSILON);
	}

	public static boolean lessThan(double a, double b) {
		return lessThan(a, b, EPSILON);
	}

	public static boolean greaterThanOrEquals(double a, double b) {
		return greaterThanOrEquals(a, b, EPSILON);
	}

	public static boolean lessThanOrEquals(double a, double b) {
		return lessThanOrEquals(a, b, EPSILON);
	}

	public static boolean equals(double a, double b, double epsilon) {
		return a == b ? true : Math.abs(a - b) < epsilon;
	}

	public static boolean greaterThan(double a, double b, double epsilon) {
		return a - b > epsilon;
	}

	public static boolean lessThan(double a, double b, double epsilon) {
		return b - a > epsilon;
	}

	public static boolean greaterThanOrEquals(double a, double b, double epsilon) {
		return greaterThan(a, b, epsilon) || equals(a, b, epsilon);
	}

	public static boolean lessThanOrEquals(double a, double b, double epsilon) {
		return lessThan(a, b, epsilon) || equals(a, b, epsilon);
	}

	public static double round(double d, int places) {
		BigDecimal bigDecimal = new BigDecimal(d);
		bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);

		return bigDecimal.doubleValue();
	}
}
