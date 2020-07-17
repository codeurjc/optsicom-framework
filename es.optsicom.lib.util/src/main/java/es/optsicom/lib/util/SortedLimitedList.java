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
import java.util.List;

public abstract class SortedLimitedList<T> {

	protected List<T> list;
	protected int maxSize;

	public SortedLimitedList(int numElems) {
		super();
		this.list = new ArrayList<T>(numElems + 1);
		this.maxSize = numElems;
		// Debug.debugln(" Max Size : " + maxSize);
	}

	public abstract void add(T elem);

	public void addAll(Iterable<T> iterable) {
		for (T t : iterable) {
			this.add(t);
		}
	}

	public List<T> getList() {
		return list;
	}
	
	public boolean contains(T elem) {
		return list.contains(elem);
	}

	public abstract void setMaxSize(int maxSize);

	public int getMaxSize() {
		return this.maxSize;
	}

	public void clear(int from, int to) {
		list.subList(from, to).clear();
	}

	public int size() {
		return list.size();
	}

}
