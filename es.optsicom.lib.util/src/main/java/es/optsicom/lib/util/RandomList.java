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
import java.util.Collection;
import java.util.Iterator;

public class RandomList<T> implements Iterable<T> {

	Collection<? extends T> list;
	
	public RandomList(Collection<? extends T> list){
		this.list = list;
	}
	
	public Iterator<T> iterator() {
		return new RandomIterator<T>(list);
	}
	
	/**
	 * <p>Returns a wrapper for collections that can be used to traverse a collection with in random order.</p>
	 * 
	 * @param <T> 
	 * @param collection The collection that will be traverser in random order.  
	 * @return A {@link RandomList} object that can be used to traverse the original collection in random order.
	 */
	public static <T> RandomList<T> create(Collection<T> collection) {
		return new RandomList<T>(collection);
	}
	
}
