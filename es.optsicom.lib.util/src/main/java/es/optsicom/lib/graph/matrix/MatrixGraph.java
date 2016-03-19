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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.optsicom.lib.graph.Arc;
import es.optsicom.lib.graph.Graph;
import es.optsicom.lib.graph.Node;

public class MatrixGraph implements Graph {

	private final float weights[][];
	private Graph totalGraph = this;
	private final List<Node> nodes;
	private List<Arc> arcs;
	private String additionalInfo;
	private List<String> skippedLines;
	private float[] nodeWeights;

	class MatrixNode implements Node {

		private final String identifier;
		private final int index;

		public MatrixNode(String identifier, int number) {
			this.identifier = identifier;
			index = number;
		}

		public String getIdentifier() {
			return identifier;
		}

		public float getWeightTo(Node node) {
			if (this == node) {
				return 0;
			} else {
				MatrixNode mNode = (MatrixNode) node;
				return weights[Math.max(index, mNode.index) - 1][Math.min(index, mNode.index)];
			}
		}

		@Override
		public String toString() {
			if (nodeWeights != null) {
				return "[" + identifier + "|" + getWeight() + "]";
			} else {
				return identifier;
			}
		}

		public int getIndex() {
			return index;
		}

		public int compareTo(Node n2) {
			return index - n2.getIndex();
		}

		@Override
		public int hashCode() {
			return index;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof MatrixNode) {
				MatrixNode mg = (MatrixNode) o;
				return index == mg.index;
			} else {
				return false;
			}
		}

		public List<Node> getAdjacents() {
			return Collections.emptyList();
		}

		public float getWeight() {
			return nodeWeights[index];
		}
	}

	public MatrixGraph(float[][] weights) {
		this(weights, weights.length + 1);
	}

	public MatrixGraph(float[][] weights, int totalNodes) {
		this.weights = weights;
		nodes = new ArrayList<Node>();
		for (int i = 0; i < totalNodes; i++) {
			nodes.add(new MatrixNode(Integer.toString(i), i));
		}
		this.nodeWeights = new float[nodes.size()];
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public Node getNode(String identifier) {
		for (Node node : nodes) {
			if (node.getIdentifier().equals(identifier)) {
				return node;
			}
		}
		return null;
	}

	public Graph getTotalGraph() {
		return totalGraph;
	}

	protected void setTotalGraph(Graph totalGraph) {
		this.totalGraph = totalGraph;
	}

	public Node getNode(int index) {
		return getNodes().get(index);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Number of Nodes: " + getNumNodes() + "\n");
		int numFila = 1;
		for (float[] fila : weights) {
			sb.append("[" + numFila + "]").append(": ");
			int col = 0;
			for (float valor : fila) {
				sb.append(valor).append("[" + col + "], ");
				col++;
			}
			sb.append("\n");
			numFila++;
		}
		return sb.toString();
	}

	public float getWeight(int i, int j) {
		if (i == j) {
			return 0;
		} else {
			return weights[Math.max(i, j) - 1][Math.min(i, j)];
		}
	}

	public int getNumNodes() {
		return weights.length + 1;
	}

	public String toPartialWeightTable() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < getNumNodes(); i++) {
			sb.append("\t").append(i + 1);
		}
		sb.append("\n");
		for (int i = 0; i < getNumNodes() - 1; i++) {
			sb.append(i + 1);
			for (int j = 0; j < getNumNodes() - 1; j++) {
				float[] fila = weights[j];
				sb.append("\t");
				if (fila.length > i) {
					sb.append(String.format("%.2f", fila[i]));
				}
			}
			sb.append("\n");
		}
		return sb.toString();

	}

	public List<Float> createWeightsList() {
		List<Float> weights = new ArrayList<Float>();
		for (float[] fila : this.weights) {
			for (float valor : fila) {
				weights.add(valor);
			}
		}
		return weights;
	}

	public List<Arc> getArcs() {
		if (arcs == null) {
			arcs = new ArrayList<Arc>();
			for (int i = 0; i < weights.length; i++) {
				float[] fila = weights[i];
				for (int j = 0; j < fila.length; j++) {
					float valor = fila[j];
					arcs.add(new ArcImpl(i + 1, j, valor));
				}
			}
		}
		return arcs;
	}

	public List<List<Float>> createNodeWeightList() {
		List<List<Float>> nodeWeight = new ArrayList<List<Float>>();
		for (int i = 0; i < getNumNodes(); i++) {
			List<Float> weights = new ArrayList<Float>();
			nodeWeight.add(weights);
			for (int j = 0; j < getNumNodes(); j++) {
				if (i != j) {
					weights.add(getWeight(i, j));
				}
			}
		}
		return nodeWeight;
	}

	public String toTotalWeightTable() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getNumNodes(); i++) {
			sb.append("\t").append(i + 1);
		}
		sb.append("\n");
		for (int i = 0; i < getNumNodes(); i++) {
			sb.append(i + 1);
			for (int j = 0; j < getNumNodes(); j++) {
				sb.append("\t");
				if (i != j) {
					sb.append(String.format("%.2f", getWeight(i, j)));
				} else {
					sb.append("-");
				}
			}
			sb.append("\n");
		}
		return sb.toString();

	}

	public List<List<Arc>> createNodeEdgesList() {
		List<List<Arc>> nodesEdges = new ArrayList<List<Arc>>();
		for (int i = 0; i < getNumNodes(); i++) {
			List<Arc> edges = new ArrayList<Arc>();
			nodesEdges.add(edges);
			for (int j = 0; j < getNumNodes(); j++) {
				if (i != j) {
					int menor = Math.min(i, j);
					int mayor = Math.max(i, j);
					edges.add(new ArcImpl(menor, mayor, getWeight(i, j)));
				}
			}
		}
		return nodesEdges;
	}

	public List<Arc> createArcsList() {
		List<Arc> edges = new ArrayList<Arc>();
		for (int i = 0; i < getNumNodes(); i++) {
			for (int j = 0; j < getNumNodes(); j++) {
				if (i != j) {
					int menor = Math.min(i, j);
					int mayor = Math.max(i, j);
					edges.add(new ArcImpl(menor, mayor, getWeight(i, j)));
				}
			}
		}
		return edges;
	}

	public float getWeight(Node v, Node w) {
		return getWeight(v.getIndex(), w.getIndex());
	}

	public void setSkippedLines(List<String> skippedLines) {
		this.skippedLines = skippedLines;
	}

	public List<String> getSkippedLines() {
		return skippedLines;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	@Override
	public void setNodeWeights(float[] nodeWeights) {
		this.nodeWeights = nodeWeights;
	}
}
