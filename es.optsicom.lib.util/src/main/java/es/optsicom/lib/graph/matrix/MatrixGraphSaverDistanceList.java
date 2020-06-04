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
import java.util.Locale;

public class MatrixGraphSaverDistanceList {

	private static MatrixGraphSaverDistanceList instance = new MatrixGraphSaverDistanceList();

	public static MatrixGraphSaverDistanceList getInstance() {
		return instance;
	}

	private MatrixGraphSaverDistanceList() {
	}

	public void saveGraph(MatrixGraph graph, File file) throws IOException {
		saveGraph(graph, new FileOutputStream(file), -1, "");
	}

	public void saveGraph(MatrixGraph g, File file, int decimals) throws FileNotFoundException {
		saveGraph(g, new FileOutputStream(file), decimals, "");
	}

	public void saveGraph(MatrixGraph g, File file, int decimals, String additionalInfo) throws FileNotFoundException {
		saveGraph(g, new FileOutputStream(file), decimals, additionalInfo);
	}

	public void saveGraph(MatrixGraph graph, OutputStream os, int decimals, String additionalInfo) {

		int n = graph.getNumNodes();

		PrintWriter w = new PrintWriter(new OutputStreamWriter(os));
		w.println(n + " " + additionalInfo);

		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				float distance = graph.getWeight(i, j);

				String value;
				if (decimals == -1) {
					value = Float.toString(distance);
				} else {
					value = String.format(Locale.ENGLISH, "%." + decimals + "f", distance);
				}

				w.println(i + " " + j + " " + value);
			}
		}

		w.close();
	}

}
