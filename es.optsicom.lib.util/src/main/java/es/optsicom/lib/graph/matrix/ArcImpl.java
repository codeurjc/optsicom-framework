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
package es.optsicom.lib.graph.matrix;

import es.optsicom.lib.graph.Arc;
import es.optsicom.lib.graph.Node;

public class ArcImpl implements Arc {

	private int node1;
	private int node2;
	private float weight;

	public ArcImpl(int node1, int node2, float weight) {
		this.node1 = node1;
		this.node2 = node2;
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.optsicom.graph.matrix.Arc#getNode1()
	 */
	public int getNodeIndex1() {
		return node1;
	}

	public void setNode1(int node1) {
		this.node1 = node1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.optsicom.graph.matrix.Arc#getNode2()
	 */
	public int getNodeIndex2() {
		return node2;
	}

	public void setNode2(int node2) {
		this.node2 = node2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.optsicom.graph.matrix.Arc#getWeight()
	 */
	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.optsicom.graph.matrix.Arc#compareTo(es.optsicom.graph.matrix.ArcImpl)
	 */
	public int compareTo(Arc o) {
		if (weight > o.getWeight()) {
			return 1;
		} else if (weight < o.getWeight()) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "d(" + (node1 + 1) + "," + (node2 + 1) + ")";
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + node1;
		result = PRIME * result + node2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ArcImpl other = (ArcImpl) obj;
		if (node1 != other.node1) {
			return false;
		}
		if (node2 != other.node2) {
			return false;
		}
		return true;
	}

	public Node getNode1() {
		return null;
	}

	public Node getNode2() {
		return null;
	}

}
