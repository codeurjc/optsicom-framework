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

import java.util.Comparator;
import java.util.List;

/**
 * @author mica
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface Node extends Comparable<Node> {

	public static Comparator<Node> WEIGHT_COMPARATOR = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return Float.compare(o1.getWeight(), o2.getWeight());
		}
	};

	public static Comparator<Node> WEIGHT_REVERSE_COMPARATOR = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return Float.compare(o2.getWeight(), o1.getWeight());
		}
	};

	String getIdentifier();

	float getWeightTo(Node node);

	float getWeight();

	int getIndex();

	List<Node> getAdjacents();

}
