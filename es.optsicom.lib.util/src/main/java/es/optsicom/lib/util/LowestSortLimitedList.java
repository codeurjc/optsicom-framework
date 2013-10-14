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

import java.util.Collections;

/**
 * @author Patxi Gortázar
 * 
 */
public class LowestSortLimitedList<T extends Comparable<? super T>> extends SortedLimitedList<T> {

	public LowestSortLimitedList(int numElems) {
		super(numElems);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.optsicom.util.SortedLimitedList#add(java.lang.Object)
	 */
	@Override
	public void add(T elem) {
		int indexOf = this.list.indexOf(elem);

		if (indexOf == -1) {

			int index = Collections.binarySearch(list, elem);

			if (index < 0) {

				int position = -index - 1;

				if (list.size() < maxSize || position != maxSize) {
					list.add(position, elem);
				}

			} else {
				if (list.size() < maxSize || index != maxSize) {
					list.add(index, elem);
				}
			}

			if (list.size() > maxSize) {
				list.remove(list.size() - 1);
			}
		}
	}

	public T getLowest() {
		return list.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.optsicom.util.SortedLimitedList#setMaxSize(int)
	 */
	@Override
	public void setMaxSize(int maxSize) {
		if (maxSize < list.size()) {
			list.subList(maxSize, list.size()).clear();
		}
		this.maxSize = maxSize;
	}

	public static void main(String[] args) {
		LowestSortLimitedList<Float> refSet = new LowestSortLimitedList<Float>(5);
		refSet.add(3.3f);
		refSet.add(5f);
		refSet.add(3.8f);
		refSet.add(3.9f);
		refSet.add(3.91f);
		refSet.add(3.92f);
		refSet.add(3.93f);
		refSet.add(3.94f);
		refSet.add(3.5f);
		refSet.add(3.6f);
		refSet.add(3.7f);

		System.out.println(refSet.getList());
	}

}
