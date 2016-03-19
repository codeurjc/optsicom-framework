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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import es.optsicom.lib.graph.Graph;

public class MatrixGraphSaverSilvaFormat {

	private static MatrixGraphSaverSilvaFormat instance = new MatrixGraphSaverSilvaFormat();

	public static MatrixGraphSaverSilvaFormat getInstance() {
		return instance;
	}

	private MatrixGraphSaverSilvaFormat() {}

	public void saveGraph(Graph graph, File file) throws IOException {
		saveGraph(graph, new FileOutputStream(file), -1);
	}
	
	public void saveGraph(Graph graph, OutputStream os, int decimals) {

		int n = graph.getNumNodes();

		PrintWriter w = new PrintWriter(new OutputStreamWriter(os));
		w.println(n);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				float distance = graph.getWeight(i, j);

				String value;
				if (decimals == -1) {
					value = Float.toString(distance);
				} else {
					value = String.format("%." + decimals + "f", distance);
				}

				w.print(value+" ");
			}
			
			w.println();
		}

		w.close();
	}

	public void saveGraph(Graph g, File file, int decimals) throws FileNotFoundException {
		saveGraph(g, new FileOutputStream(file), decimals);
	}

}
