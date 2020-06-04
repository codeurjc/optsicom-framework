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
package es.optsicom.lib.approx.algorithm.ss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase genera grupos de elementos. Se utiliza en metaheurísticas
 * poblacionales en las que es necesaria la combinación de soluciones.
 * Básicamente se ha usado en SS. Genera los grupos con la siguiente lógica: -
 * Si el tamaño de los grupos es 2, genera todas las combinaciones de 2
 * elementos - Si el tamaño de los grupos es 3, genera todas las combinaciones
 * de 2 elementos + las combinaciones de 3 elementos formadas al añadir el
 * elemento con menor índice a las combinaciones de 2 elementos sin repetir
 * ningún grupo. Esta creación de grupos tiene sentido porque los índices se
 * usan para indexar soluciones del refSet y aquellas con menor índice son las
 * "mejores", de esa forma se da prioridad a las combinaciones de las mejores
 * soluciones evitando la explosión combinatoria. - Si el tamaño de los grupos
 * es 4, se generan todas las combinaciones de 2 elementos, las combinaciones de
 * 3 elementos siguiendo la lógica anterior y las combinaciones de 4 elementos
 * también siguiendo la lógica anterior. - Si el tamaño es mayor de 4, se
 * generan todas las soluciones anteriores más los grupos que contienen los 5,
 * 6,.., n elementos con el índice más bajo.
 * 
 * @author mica
 *
 */
public class SSCombinationsGen {

	private final List<List<Integer>> totalGroups;
	private final Map<Integer, List<List<Integer>>> groupsContainingIndex = new HashMap<Integer, List<List<Integer>>>();

	public SSCombinationsGen(int numElemsToCombine) {
		this(numElemsToCombine, numElemsToCombine);
	}

	public SSCombinationsGen(int combinationsMaxSize, int numElemsToCombine) {

		// this.combinationsMaxSize = combinationsMaxSize;

		if (combinationsMaxSize > numElemsToCombine) {
			throw new IllegalArgumentException("combinationMaxSize cannot be greater than refSetSize");
		}

		totalGroups = generateCombinations(combinationsMaxSize, numElemsToCombine);

		for (int i = 0; i < numElemsToCombine; i++) {
			groupsContainingIndex.put(i, new ArrayList<List<Integer>>());
		}

		for (List<Integer> group : totalGroups) {
			for (Integer i : group) {
				groupsContainingIndex.get(i).add(group);
			}
		}
	}

	public List<List<Integer>> getAllGroups() {
		return totalGroups;
	}

	private List<List<Integer>> generateCombinations(int combinationsMaxSize, int refSetSize) {

		List<List<Integer>> totalGroups = new ArrayList<List<Integer>>();

		List<List<Integer>> groups2 = createGroupIterationInt(2, refSetSize);
		totalGroups.addAll(groups2);

		if (combinationsMaxSize > 2) {

			List<List<Integer>> groups3 = createExpandedGroup(refSetSize, groups2);
			totalGroups.addAll(groups3);

			if (combinationsMaxSize > 3) {
				List<List<Integer>> groups4 = createExpandedGroup(refSetSize, groups3);
				totalGroups.addAll(groups4);

				if (combinationsMaxSize > 4) {

					for (int i = 5; i <= combinationsMaxSize; i++) {
						totalGroups.add(createGroup(i));
					}
				}
			}
		}

		return totalGroups;
	}

	private List<Integer> createGroup(int numElems) {
		List<Integer> group = new ArrayList<Integer>();
		for (int i = 0; i < numElems; i++) {
			group.add(i);
		}
		return group;
	}

	private List<List<Integer>> createExpandedGroup(int refSetSize, List<List<Integer>> originalGroups) {
		List<List<Integer>> expandedGroups = new ArrayList<List<Integer>>();
		for (List<Integer> group : originalGroups) {
			expandedGroups.add(new ArrayList<Integer>(group));
		}

		for (Iterator<List<Integer>> iter = expandedGroups.iterator(); iter.hasNext();) {
			List<Integer> expandedGroup = iter.next();

			boolean groupSet = false;

			for (int i = 0; i < refSetSize; i++) {
				if (expandedGroup.contains(i)) {
					continue;
				}

				expandedGroup.add(i);
				if (existsPreviousGroup(expandedGroups, expandedGroup)) {
					expandedGroup.remove(expandedGroup.size() - 1);
				} else {
					groupSet = true;
					break;
				}
			}

			if (!groupSet) {
				iter.remove();
			}
		}
		return expandedGroups;
	}

	private boolean existsPreviousGroup(List<List<Integer>> groups, List<Integer> newGroup) {
		for (List<Integer> group : groups) {
			if (group == newGroup) {
				return false;
			}
			List<Integer> oGroup = new ArrayList<Integer>(group);
			List<Integer> oNewGroup = new ArrayList<Integer>(newGroup);

			Collections.sort(oGroup);
			Collections.sort(oNewGroup);

			if (oGroup.equals(oNewGroup)) {
				return true;
			}
		}
		throw new Error();
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

	public Collection<List<Integer>> getGroupsContainingIndex(int i) {
		return this.groupsContainingIndex.get(i);
	}

	public Collection<List<Integer>> getGroupsContainingIndexes(Integer... indexes) {
		return getGroupsContainingIndexes(Arrays.asList(indexes));
	}

	public Collection<List<Integer>> getGroupsContainingIndexes(List<Integer> indexes) {
		Set<List<Integer>> groups = new HashSet<List<Integer>>();
		for (Integer i : indexes) {
			groups.addAll(this.groupsContainingIndex.get(i));
		}
		return groups;
	}

	public Collection<List<Integer>> getGroupsContainingIndexes(List<Integer> indexes, int refSetSize) {
		Set<List<Integer>> groups = new HashSet<List<Integer>>();
		for (Integer i : indexes) {
			for (List<Integer> group : this.groupsContainingIndex.get(i)) {
				int maxGroupValue = 0;
				for (int gIndex : group) {
					maxGroupValue = Math.max(gIndex, maxGroupValue);
				}
				if (maxGroupValue < refSetSize) {
					groups.add(group);
				}
			}
		}
		return groups;
	}

	public <T> Collection<List<T>> getGroupsContainingIndexes(List<Integer> indexes, List<T> refSet) {
		Set<List<T>> returnGroups = new HashSet<List<T>>();
		for (Integer i : indexes) {
			for (List<Integer> group : this.groupsContainingIndex.get(i)) {
				int maxGroupValue = 0;
				for (int gIndex : group) {
					maxGroupValue = Math.max(gIndex, maxGroupValue);
				}
				if (maxGroupValue < refSet.size()) {
					List<T> returnGroup = new ArrayList<T>();
					for (int index : group) {
						returnGroup.add(refSet.get(index));
					}
					returnGroups.add(returnGroup);
				}
			}
		}
		return returnGroups;
	}

	public <T> List<List<T>> getGroups(List<T> mdpGraphs) {

		List<List<T>> groups = new ArrayList<List<T>>();

		global: for (List<Integer> groupInt : totalGroups) {
			List<T> group = new ArrayList<T>();
			for (Integer i : groupInt) {
				if (i > mdpGraphs.size() - 1) {
					continue global;
				} else {
					group.add(mdpGraphs.get(i));
				}
			}
			groups.add(group);
		}

		return groups;
	}

}
