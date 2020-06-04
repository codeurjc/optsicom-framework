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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class PointListSaver {

	private static PointListSaver instance = new PointListSaver();

	public static PointListSaver getInstance() {
		return instance;
	}

	public void saveGraph(List<Point> puntos, File file) throws IOException {
		saveGraph(puntos, new FileOutputStream(file));
	}

	public void saveGraph(List<Point> puntos, OutputStream os) {

		PrintWriter w = new PrintWriter(new OutputStreamWriter(os));
		w.println(puntos.size());
		w.println(puntos.get(0).getDimensions().length);

		int counter = 0;
		for (Point punto : puntos) {
			w.print(counter);
			for (float dimension : punto.getDimensions()) {
				w.print(" " + dimension);
			}
			w.println();
			counter++;
		}

		w.close();
	}
}
