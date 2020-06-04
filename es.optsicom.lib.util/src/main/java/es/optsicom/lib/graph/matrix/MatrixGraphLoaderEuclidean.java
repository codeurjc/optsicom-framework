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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MatrixGraphLoaderEuclidean extends MatrixGraphLoader {

	private static MatrixGraphLoaderEuclidean instance = new MatrixGraphLoaderEuclidean();

	public static MatrixGraphLoaderEuclidean getInstance() {
		return instance;
	}

	@Override
	public MatrixGraph loadGraph(InputStream is, int totalNodes) throws IOException, FormatException {

		try {

			List<Point> puntos = loadPointList(is);

			float[][] weights = createMatrixWeight(puntos);

			if (totalNodes == -1) {
				return new MatrixGraph(weights);
			} else {
				return new MatrixGraph(weights, totalNodes);
			}

		} catch (NumberFormatException e) {
			throw new FormatException(e);
		}
	}

	private List<Point> loadPointList(InputStream is) throws IOException, FormatException {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String linea = br.readLine();
		if (linea == null) {
			throw new FormatException("The file is empty");
		}

		int indexOfSpace = linea.indexOf(' ');
		int numberNodes;
		if (indexOfSpace == -1) {
			numberNodes = Integer.parseInt(linea);
		} else {
			numberNodes = Integer.parseInt(linea.substring(0, indexOfSpace));
		}

		linea = br.readLine();
		int numDimensiones = Integer.parseInt(linea);

		List<Point> puntos = new ArrayList<Point>();

		linea = br.readLine();

		while (linea != null && !linea.trim().equals("")) {
			StringTokenizer st = new StringTokenizer(linea);
			st.nextToken();

			float[] dimensiones = new float[numDimensiones];
			for (int i = 0; i < numDimensiones; i++) {
				String value = st.nextToken();
				float valueFloat = Float.parseFloat(value);
				dimensiones[i] = valueFloat;
			}

			puntos.add(new Point(dimensiones));

			linea = br.readLine();
		}

		if (puntos.size() != numberNodes) {
			throw new FormatException("Incorrect number of lines");
		}
		return puntos;
	}

	public float[][] createMatrixWeight(List<Point> points) {
		float weights[][] = new float[points.size() - 1][];
		for (int i = 0; i < points.size() - 1; i++) {
			weights[i] = new float[i + 1];
		}

		for (int j = 0; j < points.size(); j++) {
			for (int i = 0; i < j; i++) {
				Point pointJ = points.get(j);
				Point pointI = points.get(i);
				weights[j - 1][i] = pointJ.distanceTo(pointI);
			}
		}
		return weights;
	}

}
