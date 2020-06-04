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
import java.util.Comparator;

public class BiggestInvertSortLimitedList<T> extends SortedLimitedList<T> {
	
	@SuppressWarnings("rawtypes")
	private static final Comparator inverseComparator = Collections.reverseOrder();

	public BiggestInvertSortLimitedList(int numElems) {
		super(numElems);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(T elem) {

		int indexOf = this.list.indexOf(elem);

		if (indexOf == -1) {

			int index = Collections.binarySearch(list, elem, (Comparator<T>) inverseComparator);

			if (index < 0) {

				int position = -index - 1;

				if (list.size() < maxSize || position != maxSize) {
					list.add(position, elem);
				}

			} else {
				if (list.size() < maxSize || index != maxSize) {
					// if(elem.equals(list.get(index))){
					// System.out.println("Equals!!");
					// }
					list.add(index, elem);
				}
			}

			if (list.size() > maxSize) {
				list.remove(list.size() - 1);
			}
		}

		// } else {

		// MDPSolution solution = (MDPSolution) elem;
		// MDPSolution otherSolution = (MDPSolution) this.list.get(indexOf);
		// if (solution == otherSolution) {
		// //System.out.println("ERROR: Encontrado el mismo en el refSet");
		// } else {
		//
		// float olderError = solution.getWeight()
		// - otherSolution.getWeight();
		//
		// solution.recalculateNodesContribution();
		// otherSolution.recalculateNodesContribution();
		//
		// float error = solution.getWeight() - otherSolution.getWeight();
		//
		// // if(olderError != 0){
		// // System.out.println("Older Error: "+olderError+" New Error:
		// "+error);
		// // }
		//
		// if (error != 0) {
		//
		// List<Weighted<Node>> nodesCont = solution
		// .getNodesContribution();
		// List<Weighted<Node>> otherNodesCont = otherSolution
		// .getNodesContribution();
		//
		// for (Node node : solution) {
		//
		// Weighted<Node> node1 = null;
		// for (Weighted<Node> wn : nodesCont) {
		// if (wn.getElement() == node) {
		// node1 = wn;
		// }
		// }
		//
		// Weighted<Node> node2 = null;
		// for (Weighted<Node> wn : otherNodesCont) {
		// if (wn.getElement() == node) {
		// node2 = wn;
		// }
		// }
		//
		// if (!node1.equals(node2)) {
		//
		// int index =node1.getElement().getIndex();
		// System.out.println("Node: "
		// + index
		// + " Error Weight: "
		// + (node1.getWeight() - node2.getWeight()));
		//
		// solution.recalculateNodesContribution(index);
		// otherSolution.recalculateNodesContribution(index);
		//
		// float newWeightSol = 0;
		// for (Node n : solution) {
		// newWeightSol += node1.getElement().getWeightTo(
		// n);
		// }
		//
		// float newWeightOSol = 0;
		// for (Node n : otherSolution) {
		// newWeightOSol += node2.getElement()
		// .getWeightTo(n);
		// }
		//
		// System.out.println("Error: "
		// + (newWeightSol - newWeightSol));
		// }
		// }
		//
		// }
		// }
		// }
	}

	public T getBiggest() {
		return list.get(0);
	}

	@Override
	public void setMaxSize(int maxSize) {
		// Debug.debugln(" Max Size : " + maxSize);
		if (maxSize < list.size()) {
			list.subList(maxSize, list.size()).clear();
		}
		this.maxSize = maxSize;
	}

	public static void main(String[] args) {
		BiggestInvertSortLimitedList<Float> refSet = new BiggestInvertSortLimitedList<Float>(5);
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
