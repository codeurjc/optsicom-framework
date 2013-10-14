package es.optsicom.lib.graph.unweighted;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import es.optsicom.lib.graph.Graph;

/**
 * <p>Loads an unweighted graph from a file in the format defined by Chaco, described
 * in the manual of Jostle software (a software tool for the Graph Partitioning Problem):</p>
 * 
 * <p><a href="http://staffweb.cms.gre.ac.uk/~c.walshaw/jostle/jostle-exe.pdf">http://staffweb.cms.gre.ac.uk/~c.walshaw/jostle/jostle-exe.pdf</a></p>
 * 
 * @author Francisco Gortázar <patxi.gortazar@gmail.com>
 *
 */
public class ChacoGraphLoader {

	public Graph loadGraph(File file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(file));

		String line = br.readLine();
		StringTokenizer st = new StringTokenizer(line);
		int numNodes = Integer.parseInt(st.nextToken());
		int numEdges = Integer.parseInt(st.nextToken());

		Map<Integer, List<Integer>> adjacencyMap = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < numNodes; i++) {
			line = br.readLine();
			st = new StringTokenizer(line);
			while (st.hasMoreTokens()) {
				int adjacentIndex = Integer.parseInt(st.nextToken());
				adjacentIndex--; // We use 0-based indexes
				putCreatingIfNecessary(adjacencyMap, i, adjacentIndex);
			}
		}
		
		br.close();

		UnweightedGraph graph = new UnweightedGraph(numNodes, numEdges, adjacencyMap);

		return graph;

	}

	private void putCreatingIfNecessary(Map<Integer, List<Integer>> adjacencyMap, int i, int adjacentIndex) {
		if (adjacencyMap.containsKey(i)) {
			adjacencyMap.get(i).add(adjacentIndex);
		} else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(adjacentIndex);
			adjacencyMap.put(i, list);
		}

//		if (adjacencyMap.containsKey(adjacentIndex)) {
//			adjacencyMap.get(adjacentIndex).add(i);
//		} else {
//			List<Integer> list = new ArrayList<Integer>();
//			list.add(i);
//			adjacencyMap.put(adjacentIndex, list);
//		}
	}

}
