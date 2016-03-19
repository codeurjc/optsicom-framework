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

import java.util.Iterator;
import java.util.List;

public class RotateListView<T> implements Iterable<T> {

	private class IteratorRotate implements Iterator<T> {

		private int initialCounter;
		private boolean hasNext = true;

		public IteratorRotate() {
			initialCounter = startCounter;
		}

		public boolean hasNext() {
			return hasNext;
		}

		public T next() {
			T elem = list.get(startCounter);
			startCounter = (startCounter + 1) % list.size();
			hasNext = (startCounter != initialCounter);
			return elem;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private List<? extends T> list;
	int startCounter;

	public RotateListView(List<? extends T> list) {
		this(list, RandomManager.nextInt(list.size()));
	}

	public RotateListView(List<? extends T> list, int startIndex) {
		this.list = list;
		this.startCounter = startIndex;
	}

	public Iterator<T> iterator() {
		return new IteratorRotate();
	}

	public static <T> RotateListView<T> create(List<T> list) {
		return new RotateListView<T>(list);
	}

}
