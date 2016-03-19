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
package es.optsicom.lib.graph;

public interface Arc extends Comparable<Arc> {

	public abstract int getNodeIndex1();

	public abstract int getNodeIndex2();

	public abstract Node getNode1();

	public abstract Node getNode2();

	public abstract float getWeight();

	public abstract int compareTo(Arc o);

}