package es.optsicom.lib.util;

//--------------------------------------
// Systematically generate combinations.
//--------------------------------------

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {

	private int[] a;
	private int n;
	private int r;
	private long numLeft;
	private long total;

	// ------------
	// Constructor
	// ------------

	public CombinationGenerator(int n, int r) {
		if (r > n) {
			throw new IllegalArgumentException();
		}
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		this.n = n;
		this.r = r;
		a = new int[r];
		BigInteger nFact = getFactorial(n);
		BigInteger rFact = getFactorial(r);
		BigInteger nminusrFact = getFactorial(n - r);
		total = nFact.divide(rFact.multiply(nminusrFact)).longValue();

		// Mica: I made this hack to allow that n == r
		// if(n == r){
		// total = 2;
		// }

		reset();
	}

	// ------
	// Reset
	// ------

	public void reset() {
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		numLeft = total;
	}

	// ------------------------------------------------
	// Return number of combinations not yet generated
	// ------------------------------------------------

	public long getNumLeft() {
		return numLeft;
	}

	// -----------------------------
	// Are there more combinations?
	// -----------------------------

	public boolean hasMore() {
		return numLeft >= 1;
	}

	// ------------------------------------
	// Return total number of combinations
	// ------------------------------------

	public long getTotal() {
		return total;
	}

	// ------------------
	// Compute factorial
	// ------------------

	private static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	// --------------------------------------------------------
	// Generate next combination (algorithm from Rosen p. 286)
	// --------------------------------------------------------

	public int[] getNext() {

		if (numLeft == total) {
			numLeft = numLeft - 1;
			return a.clone();
		}

		int i = r - 1;
		while (a[i] == n - r + i) {
			i--;
		}
		a[i] = a[i] + 1;
		for (int j = i + 1; j < r; j++) {
			a[j] = a[i] + j - i;
		}

		numLeft = numLeft - 1;
		return a.clone();

	}

	public static int[][] createCombinations(int n, int r) {

		CombinationGenerator x = new CombinationGenerator(n, r);
		int[][] result = new int[(int) x.getTotal()][];

		int numCombination = 0;
		while (x.hasMore()) {
			result[numCombination] = x.getNext();
			numCombination++;
		}

		return result;
	}

	public static <E> List<List<E>> createCombinations(List<E> elements, int r) {

		List<List<E>> result = new ArrayList<List<E>>();
		CombinationGenerator x = new CombinationGenerator(elements.size(), r);

		while (x.hasMore()) {
			int[] indices = x.getNext();
			List<E> combination = new ArrayList<E>();
			for (int i = 0; i < indices.length; i++) {
				combination.add(elements.get(indices[i]));
			}
			result.add(combination);
		}

		return result;
	}

	public static int[][] createCombination(int n) {
		List<int[]> result = new ArrayList<int[]>();

		for (int i = 1; i <= n; i++) {
			int[][] comb = (createCombinations(n, i));
			for (int j = 0; j < comb.length; j++) {
				result.add(comb[j]);
			}
		}

		return result.toArray(new int[0][0]);
	}

	public static <E> List<List<E>> createCombinations(List<E> elements) {
		List<List<E>> result = new ArrayList<List<E>>();

		for (int i = 1; i <= elements.size(); i++) {
			result.addAll(createCombinations(elements, i));
		}

		return result;
	}
}
