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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import es.optsicom.lib.graph.Graph;

public abstract class MatrixGraphLoader {

	public Graph loadGraph(File file) throws IOException, FormatException {
		return loadGraph(new FileInputStream(file));
	}

	public Graph loadGraph(File file, int totalNodes) throws IOException, FormatException {
		FileInputStream fis = new FileInputStream(file);
		Graph g = loadGraph(fis, totalNodes);
		fis.close();
		return g;
	}

	public Graph loadGraph(InputStream is) throws IOException, FormatException {
		return loadGraph(is, -1);
	}

	public abstract Graph loadGraph(InputStream is, int totalNodes) throws IOException, FormatException;

}
