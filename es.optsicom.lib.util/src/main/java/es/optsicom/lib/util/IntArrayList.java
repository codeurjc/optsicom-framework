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

public class IntArrayList {

	private int[] data;
	private int numElems;

	public IntArrayList(int maxElems) {
		this.data = new int[maxElems];
	}

	public void add(int value) {
		this.data[numElems] = value;
		numElems++;
	}

	public int removeLast() {
		numElems--;
		return data[numElems];
	}

	public int size() {
		return numElems;
	}

	public int[] getData() {
		return data;
	}
}
