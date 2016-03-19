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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RandomIterator<T> implements Iterator<T> {

	private List<? extends T> list;

	public RandomIterator(Collection<? extends T> list) {
		this.list = new ArrayList<T>(list);
	}

	public boolean hasNext() {
		return !list.isEmpty();
	}

	public T next() {
		return list.remove(RandomManager.nextInt(list.size()));
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
