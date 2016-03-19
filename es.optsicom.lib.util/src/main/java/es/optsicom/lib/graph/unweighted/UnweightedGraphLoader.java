package es.optsicom.lib.graph.unweighted;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import es.optsicom.lib.graph.matrix.FormatException;

/**
 * @author Patxi Gortázar
 * 
 */
public class UnweightedGraphLoader {

	private static UnweightedGraphLoader INSTANCE = new UnweightedGraphLoader();

	public static UnweightedGraphLoader getInstance() {
		return INSTANCE;
	}

	public UnweightedGraph loadGraph(FileInputStream fio) throws FormatException {
		return loadGraph(fio, false);
	}

	/**
	 * @param fileInputStream
	 * @return
	 * @throws FormatException
	 */
	public UnweightedGraph loadGraph(FileInputStream fileInputStream, boolean firstLineMatters) throws FormatException {
		BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

		boolean[][] adjacencyMatrix;
		Map<Integer, List<Integer>> adjacentsMap;
		String line;
		try {
			if (!firstLineMatters) {
				br.readLine();
			}
			line = br.readLine();

			StringTokenizer st = new StringTokenizer(line);
			int numNodes = Integer.parseInt(st.nextToken());
			adjacencyMatrix = new boolean[numNodes][numNodes];

			adjacentsMap = new HashMap<Integer, List<Integer>>();

			while ((line = br.readLine()) != null) {
				if (!"".equals(line)) {

					st = new StringTokenizer(line);

					int nodeA = Integer.parseInt(st.nextToken());
					int nodeB = Integer.parseInt(st.nextToken());

					adjacencyMatrix[nodeA - 1][nodeB - 1] = true;
					adjacencyMatrix[nodeB - 1][nodeA - 1] = true;

					if (!adjacentsMap.containsKey(nodeA - 1)) {
						adjacentsMap.put(nodeA - 1, new ArrayList<Integer>());
					}
					List<Integer> adjacents = adjacentsMap.get(nodeA - 1);
					adjacents.add(nodeB - 1);

					if (!adjacentsMap.containsKey(nodeB - 1)) {
						adjacentsMap.put(nodeB - 1, new ArrayList<Integer>());
					}
					adjacents = adjacentsMap.get(nodeB - 1);
					adjacents.add(nodeA - 1);

				}
			}
		} catch (IOException e) {
			throw new FormatException();
		}

		return new UnweightedGraph(adjacencyMatrix, adjacentsMap);
	}

}
