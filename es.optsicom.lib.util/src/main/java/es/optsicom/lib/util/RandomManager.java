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

import java.util.Random;

public class RandomManager {

	private static Random random = new Random();

	// private static List<Number> randomNumbers = new ArrayList<Number>();
	// private static int numRandoms = 0;

	public static int nextInt(int number) {
		int nextInt = random.nextInt(number);
		// System.out.println("RI:"+nextInt);
		// randomNumbers.add(nextInt);
		return nextInt;
	}

	/**
	 * Next random int between begin and end, both inclusive
	 * 
	 * @param begin
	 * @param end
	 * @return int
	 */
	public static int nextInt(int begin, int end) {
		if (end - begin == 0) {
			return end;
		}
		int nextInt = nextInt(end - begin + 1);
		return nextInt + begin;
	}

	public static double nextDouble() {
		double nextDouble = random.nextDouble();
		// System.out.println("RD:"+nextDouble);
		// randomNumbers.add(nextDouble);
		return nextDouble;
	}

	/**
	 * Sample of a normal distribution with the mean and deviation specified
	 * 
	 * @param mean
	 * @param stdev
	 * @return double
	 */
	public static double nextGaussian(double mean, double stdev) {
		double nextGaussian = random.nextGaussian() * stdev + mean;
		return nextGaussian;
	}

	/**
	 * Sample of a truncated normal distribution within the 95% interval of the mean
	 * and deviation specified
	 * 
	 * @param mean
	 * @param stdev
	 * @return double
	 */
	public static double nextGaussianTruncated(double mean, double stdev) {
		double lowerLimit = mean - (2 * stdev);
		double upperLimit = mean + (2 * stdev);

		double nextGaussian = nextGaussian(mean, stdev);

		while (DoublePrecision.lessThan(nextGaussian, lowerLimit)
		        || DoublePrecision.greaterThan(nextGaussian, upperLimit)) {

			nextGaussian = nextGaussian(mean, stdev);
		}

		return nextGaussian;
	}

	/**
	 * Sample of a truncated normal distribution within the interval of the mean and
	 * deviation specified
	 * 
	 * @param mean
	 * @param stdev
	 * @param lowerLimit
	 * @param upperLimit
	 * @return double
	 */
	public static double nextGaussianTruncated(double mean, double stdev, double lowerLimit, double upperLimit) {
		double nextGaussian = nextGaussian(mean, stdev);

		while (DoublePrecision.lessThan(nextGaussian, lowerLimit)
		        || DoublePrecision.greaterThan(nextGaussian, upperLimit)) {

			nextGaussian = nextGaussian(mean, stdev);
		}

		return nextGaussian;
	}

	public static float nextFloat() {
		float nextFloat = random.nextFloat();
		return nextFloat;
	}

	public static void setSeed(long seed) {
		System.out.println("Using seed: " + seed);
		random = new Random(seed);
		// if(randomNumbers.size() != 0){
		// try {
		// Writer out = new BufferedWriter(new FileWriter(new
		// File("randoms/random"+numRandoms+".txt")));
		// long numRan = 0;
		// for(Number number : randomNumbers){
		// out.append(numRan+"\t"+number+"\n");
		// numRan++;
		// }
		// randomNumbers.clear();
		// out.close();
		// System.out.println("Saved!!!");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// numRandoms++;
		// }
	}

	public static boolean nextBoolean() {
		return RandomManager.nextInt(2) == 1 ? true : false;
	}

	public static Random getRandom() {
		return random;
	}
}
