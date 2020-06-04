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
package es.optsicom.lib.analyzer.tablecreator.pr;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListDoubleArrayEncapsulator implements List<Number> {

	public class ListDoubleArrayEncapsulatorListIt implements ListIterator<Number> {

		private int itIndex;

		public ListDoubleArrayEncapsulatorListIt(int index) {
			this.itIndex = index;
		}

		public void add(Number e) {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return itIndex < values.size();
		}

		public boolean hasPrevious() {
			return itIndex > 0;
		}

		public Number next() {
			Number n = get(itIndex);
			itIndex++;
			return n;
		}

		public int nextIndex() {
			return itIndex + 1;
		}

		public Number previous() {
			Number n = get(itIndex);
			itIndex--;
			return n;
		}

		public int previousIndex() {
			return itIndex - 1;
		}

		public void remove() {
			throw new UnsupportedOperationException();

		}

		public void set(Number e) {
			throw new UnsupportedOperationException();

		}

	}

	private List<Double[]> values;
	private int index = 0;

	public ListDoubleArrayEncapsulator(List<Double[]> values) {
		this.values = values;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int size() {
		return values.size();
	}

	public Number get(int index) {
		return values.get(index)[this.index];
	}

	public Iterator<Number> iterator() {
		return listIterator();
	}

	public ListIterator<Number> listIterator() {
		return listIterator(0);
	}

	public ListIterator<Number> listIterator(int index) {
		return new ListDoubleArrayEncapsulatorListIt(index);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean add(Number e) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, Number element) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends Number> c) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends Number> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public Number remove(int index) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public Number set(int index, Number element) {
		throw new UnsupportedOperationException();
	}

	public List<Number> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

}
