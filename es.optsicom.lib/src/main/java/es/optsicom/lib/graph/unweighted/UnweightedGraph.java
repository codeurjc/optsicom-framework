package es.optsicom.lib.graph.unweighted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import es.optsicom.lib.graph.Arc;
import es.optsicom.lib.graph.Graph;
import es.optsicom.lib.graph.Node;

/**
 * @author Patxi Gortázar
 * 
 */
public class UnweightedGraph implements Graph {

	public class UnweightedNode implements Node {

		private int index;
		private List<Node> adjacentNodes;
		private Node[] arrayOfAdjacentNodes;

		public UnweightedNode(int index) {
			this.index = index;
		}

		public String getIdentifier() {
			return Integer.toString(index);
		}

		public int getIndex() {
			return index;
		}

		public float getWeightTo(Node node) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Node otherNode) {
			return this.getIndex() - otherNode.getIndex();
		}

		@Override
		public String toString() {
			return Integer.toString(index);
		}

		/**
		 * The collection returned shouldn't be modified.
		 */
		@Override
		public List<Node> getAdjacents() {
			return this.adjacentNodes;
		}
		
		public Node[] getAdjacentsAsArray() {
			return arrayOfAdjacentNodes;
		}

		@Override
		public float getWeight() {
			throw new UnsupportedOperationException();
		}

		public void setAdjacents(List<UnweightedNode> adjacents) {
			this.adjacentNodes = new ArrayList<Node>(adjacents);
			this.arrayOfAdjacentNodes = adjacents.toArray(new Node[0]);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UnweightedNode other = (UnweightedNode) obj;
			if (index != other.index)
				return false;
			return true;
		}
		
	}
	
	public class UnweightedEdge implements Arc {
		UnweightedNode node1;
		UnweightedNode node2;
		
		public UnweightedEdge(UnweightedNode node1, UnweightedNode node2) {
			this.node1 = node1;
			this.node2 = node2;
		}

		@Override
		public int getNodeIndex1() {
			return node1.getIndex();
		}

		@Override
		public int getNodeIndex2() {
			return node2.getIndex();
		}

		@Override
		public Node getNode1() {
			return node1;
		}

		@Override
		public Node getNode2() {
			return node2;
		}

		@Override
		public float getWeight() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Arc o) {
			return 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
			result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UnweightedEdge other = (UnweightedEdge) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			
			if((node1.equals(other.node1) && node2.equals(other.node2)) 
					|| (node1.equals(other.node2) && node2.equals(other.node1))) {
				return true;
			}
			return false;
		}

		private UnweightedGraph getOuterType() {
			return UnweightedGraph.this;
		}
		
		
	}
	
	private boolean[][] matrix;
	private List<Node> nodes = new ArrayList<Node>();
	private List<Arc> edges = new ArrayList<Arc>();
	private Map<Integer, UnweightedNode> nodesMap = new HashMap<Integer, UnweightedNode>();
	private Map<UnweightedNode, List<UnweightedNode>> adjacentsMap = new HashMap<UnweightedNode, List<UnweightedNode>>();
	private String description = "";

	public UnweightedGraph(int numNodes, int NumEdges, Map<Integer, List<Integer>> adjacentsMap2) {
		
		initialize(numNodes, adjacentsMap2);
		
	}
	
	public UnweightedGraph(boolean[][] adjacencyMatrix, Map<Integer, List<Integer>> adjacentsMap2) {
		
		this.matrix = adjacencyMatrix;
		
		initialize(adjacencyMatrix.length, adjacentsMap2);
	}

	private void initialize(int numNodes, Map<Integer, List<Integer>> adjacentsMap2) {
		for (int i = 0; i < numNodes; i++) {
			UnweightedNode node = new UnweightedNode(i);
			nodesMap.put(i, node);
			nodes.add(node);
		}
		
		for (Entry<Integer, List<Integer>> entry : adjacentsMap2.entrySet()) {
			UnweightedNode node = nodesMap.get(entry.getKey());
			List<UnweightedNode> adjacents = new ArrayList<UnweightedNode>();
			for (int j : entry.getValue()) {
				UnweightedNode adjacent = nodesMap.get(j);
				adjacents.add(adjacent);
				if(!adjacentsMap.containsKey(node) && !adjacentsMap.containsKey(adjacent)) {
					UnweightedEdge edge = new UnweightedEdge(node, adjacent);
					edges.add(edge);
				}
			}
			adjacentsMap.put(node, adjacents);
			node.setAdjacents(adjacents);
		}
	}

	public boolean[][] getAdjacencyMatrix() {
		return this.matrix;
	}
	
	public List<Arc> getArcs() {
		return edges;
	}

	public List<UnweightedNode> getAdjacents(UnweightedNode node) {
		return adjacentsMap.get(node);
	}

	public Node getNode(String identifier) {
		throw new UnsupportedOperationException();
	}

	public Node getNode(int index) {
		if(!nodesMap.containsKey(index)) {
			throw new Error("Invalid node index: " + index);
		}
		return nodesMap.get(index);
	}

	public List<Node> getNodes() {

		return nodes;
	}

	public float getWeight(int i, int j) {
		return 1;
	}

	@Override
	public int getNumNodes() {
		return nodes.size();
	}

	@Override
	public float getWeight(Node v, Node w) {
		return 0;
	}

	@Override
	public String getAdditionalInfo() {
		return description;
	}
	
	public void setAdditionalInfo(String description) {
		this.description = description;
	}

}
