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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomizedSelector<T> {

	public enum Proportionality {
		DIRECTLY, INVERSELY
	}

	private List<Double> weights;
	private double totalWeight = 0;
	private double virtualTotalWeight = 0;
	private List<Weighed<T>> pairs;

	private Proportionality proportionality;

	public RandomizedSelector(Proportionality prop) {
		this.proportionality = prop;
		this.pairs = new ArrayList<Weighed<T>>();
	}

	public RandomizedSelector() {
		this(Proportionality.DIRECTLY);
	}

	public RandomizedSelector(List<Weighed<T>> pairs, Proportionality prop) {
		this.proportionality = prop;
		this.pairs = pairs;
	}

	public void add(T element, float weight) {
		weights = null;
		pairs.add(new Weighed<T>(element, weight));
		totalWeight += weight;
	}

	public T selectElement() {
		return selectPair().getElement();
	}

	private void calculateWeights() {
		this.weights = new ArrayList<Double>();
		double acummulativeWeight = 0;
		switch (proportionality) {
		case DIRECTLY: {
			for (Weighed<T> pair : pairs) {
				acummulativeWeight += pair.getWeight();
				weights.add(acummulativeWeight);
			}
			break;
		}
		case INVERSELY: {
			for (Weighed<T> pair : pairs) {
				acummulativeWeight += (totalWeight - pair.getWeight());
				weights.add(acummulativeWeight);
			}
			break;
		}
		}
		virtualTotalWeight = acummulativeWeight;
	}

	public Weighed<T> selectPair() {
		if (weights == null) {
			calculateWeights();
		}

		double randomWeight = RandomManager.nextDouble() * virtualTotalWeight;
		int index = Collections.binarySearch(weights, randomWeight);

		if (index > 0) {
			return this.pairs.get(index);
		} else {
			int insertionPoint = -index - 1;
			if (insertionPoint == weights.size()) {
				return pairs.get(pairs.size() - 1);
			} else {
				return pairs.get(insertionPoint);
			}
		}
	}

	public static int selectRandomly(double[] values) {

		double[] acummulativeValues = new double[values.length];
		double acummulativeWeight = 0;

		for (int i = 0; i < values.length; i++) {
			acummulativeWeight += values[i];
			acummulativeValues[i] = acummulativeWeight;
		}

		double randomWeight = RandomManager.nextDouble() * acummulativeWeight;
		int index = Arrays.binarySearch(acummulativeValues, randomWeight);

		if (index > 0) {
			return index;
		} else {
			int insertionPoint = -index - 1;
			if (insertionPoint == values.length) {
				return values.length - 1;
			} else {
				return insertionPoint;
			}
		}
	}

	public static int selectRandomly(IWeighed[] values) {

		double[] acummulativeValues = new double[values.length];
		double acummulativeWeight = 0;

		for (int i = 0; i < values.length; i++) {
			acummulativeWeight += values[i].getWeight();
			acummulativeValues[i] = acummulativeWeight;
		}

		double randomWeight = RandomManager.nextDouble() * acummulativeWeight;
		int index = Arrays.binarySearch(acummulativeValues, randomWeight);

		if (index > 0) {
			return index;
		} else {
			int insertionPoint = -index - 1;
			if (insertionPoint == values.length) {
				return values.length - 1;
			} else {
				return insertionPoint;
			}
		}
	}

	public static <S extends IWeighed> int selectRandomly(Collection<S> values) {

		double[] acummulativeValues = new double[values.size()];
		double acummulativeWeight = 0;

		Iterator<S> it = values.iterator();
		for (int i = 0; i < values.size(); i++) {
			acummulativeWeight += it.next().getWeight();
			acummulativeValues[i] = acummulativeWeight;
		}

		double randomWeight = RandomManager.nextDouble() * acummulativeWeight;
		int index = Arrays.binarySearch(acummulativeValues, randomWeight);

		if (index > 0) {
			return index;
		} else {
			int insertionPoint = -index - 1;
			if (insertionPoint == values.size()) {
				return values.size() - 1;
			} else {
				return insertionPoint;
			}
		}
	}

	public static <S extends IWeighed> S selectRandomlyObject(S[] values) {
		return values[selectRandomly(values)];
	}

	public static <S extends IWeighed> S selectRandomlyObject(List<S> values) {
		return values.get(selectRandomly(values));
	}

}
