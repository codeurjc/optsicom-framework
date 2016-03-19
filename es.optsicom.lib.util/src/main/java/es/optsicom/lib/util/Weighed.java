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

import java.util.Comparator;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Weighed<E> implements IWeighed {

	private static final Weighed dummyNode = new Weighed(null, 0) {
		@Override
		public Weighed min(Weighed wn) {
			return wn;
		};

		@Override
		public Weighed max(Weighed wn) {
			return wn;
		};
	};

	private static final Comparator<Weighed> INVERSE_COMPARATOR = new Comparator<Weighed>() {
		public int compare(Weighed a, Weighed b) {
			return (a.value > b.value) ? -1 : (a.value < b.value) ? 1 : 0;
		}
	};

	private E element;
	private double value;

	public Weighed(E element, double value) {
		this.element = element;
		this.value = value;
	}

	public Weighed(Weighed<E> wn) {
		this.element = wn.element;
		this.value = wn.value;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Weighed) {
			Weighed w = (Weighed) o;
			return element.equals(w.element) && value == w.value;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return element.hashCode() + 37 * (int) value;
	}

	@Override
	public String toString() {
		return ((element != null) ? element.toString() : "null") + " (" + value + ")";
	}

	public int compareTo(IWeighed w) {
		return (this.getWeight() > w.getWeight()) ? 1 : (this.getWeight() < w.getWeight()) ? -1 : 0;
	}

	public static <E> Weighed<E> clone(Weighed<E> w) {
		return new Weighed<E>(w.element, w.value);
	}

	public double getWeight() {
		return value;
	}

	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

	public void setWeight(double value) {
		this.value = value;
	}

	public Weighed<E> min(Weighed<E> wn) {
		if (this.value < wn.value) {
			return this;
		} else {
			return wn;
		}
	}

	public Weighed<E> max(Weighed<E> wn) {
		if (this.value > wn.value) {
			return this;
		} else {
			return wn;
		}
	}

	public static <E> Weighed<E> getDummyWeighted() {
		return dummyNode;
	}

	public static <T> boolean isDummyNode(Weighed<T> weighted) {
		return weighted == dummyNode;
	}

	public static Comparator<Weighed> getInverseComparatorInternal() {
		return INVERSE_COMPARATOR;
	}

	public void addValue(double deltaValue) {
		this.value += deltaValue;
	}

	public static <E> Weighed<E> create(E element, double weight) {
		return new Weighed<E>(element, weight);
	}

}