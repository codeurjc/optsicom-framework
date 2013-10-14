package es.optsicom.lib.graph.unweighted;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import es.optsicom.lib.graph.unweighted.UnweightedGraph.UnweightedNode;


public class UnweightedGraphSaver {

	private static UnweightedGraphSaver INSTANCE = new UnweightedGraphSaver();
	
	public static UnweightedGraphSaver getInstance() {
		return INSTANCE;
	}
	
	public void saveGraph(UnweightedGraph graph, FileOutputStream fos) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		// Write description
		bw.write(graph.getAdditionalInfo());
		bw.newLine();
		
		// Write graph info (number of nodes, edges... what is available)
		bw.write(graph.getNumNodes() + " " + graph.getNumNodes() + " " + graph.getArcs().size());
		bw.newLine();
		
		// Write data
		for(int i = 0; i < graph.getNumNodes(); i++) {
			List<UnweightedNode> adjacents = graph.getAdjacents((UnweightedNode) graph.getNode(i));
			for(UnweightedNode adjacent : adjacents) {
				if(adjacent.getIndex() > i) {
					// Index are one-based
					bw.write((i + 1) + " " + (adjacent.getIndex() + 1));
					bw.newLine();
				}
			}
		}
		
		bw.close();
		
	}
	
}
