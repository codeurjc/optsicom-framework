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
package es.optsicom.lib.approx.combinator;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class GroupCombinator<S extends Solution<I>, I extends Instance> implements Combinator<S, I> {

	public enum SelectionType {
		GREEDY, RANDOM_PONDERATED
	}

	int numElements = 2;

	public GroupCombinator(int i) {
		numElements = i;
	}

	public void setNumElements(int numElements) {
		this.numElements = numElements;
	}

	public int getNumElements() {
		return numElements;
	}

	public List<S> combineSolutions(List<S> solutions) {
		List<List<S>> groups = createGroups(numElements, solutions);
		return combineGroups(groups);
	}

	public List<S> combineGroups(List<List<S>> groups) {
		List<S> combined = new ArrayList<S>();
		for (List<S> group : groups) {
			combined.add(combineGroup(group));
		}
		return combined;
	}

	protected abstract S internalCombineGroup(List<S> group);

	public S combineGroup(List<S> group) {
		S newSolution = internalCombineGroup(group);
		return newSolution;
	}

	private List<List<S>> createGroups(int numElements, List<S> solutions) {

		List<List<Integer>> groupsInt = createGroupIterationInt(numElements, solutions.size());

		List<List<S>> groups = new ArrayList<List<S>>();

		for (List<Integer> groupInt : groupsInt) {
			List<S> group = new ArrayList<S>();
			groups.add(group);
			for (Integer i : groupInt) {
				group.add(solutions.get(i));
			}
		}

		return groups;
	}

	private List<List<Integer>> createGroupIterationInt(int numElements, int size) {
		return createGroupIterationInt(numElements, 0, size);
	}

	private List<List<Integer>> createGroupIterationInt(int numElements, int init, int size) {

		List<List<Integer>> groups = new ArrayList<List<Integer>>();
		if (numElements == 0) {
			groups.add(new ArrayList<Integer>());
		} else {
			for (int i = init; i < size; i++) {
				List<List<Integer>> tempGroups = createGroupIterationInt(numElements - 1, i + 1, size);
				for (List<Integer> g : tempGroups) {
					g.add(0, i);
				}
				groups.addAll(tempGroups);
			}
		}
		return groups;
	}

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}
}
