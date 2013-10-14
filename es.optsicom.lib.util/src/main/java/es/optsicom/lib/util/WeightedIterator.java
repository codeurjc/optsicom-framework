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

public class WeightedIterator<E> implements Iterator<E> {

	Iterator<Weighed<E>> it;
	
	public WeightedIterator(Iterator<Weighed<E>> it) {
		this.it = it;
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public E next() {
		return it.next().getElement();
	}

	public void remove() {
		it.remove();
	}
}
