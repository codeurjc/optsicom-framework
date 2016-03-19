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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/* Formato que incluye en la primera l�nea el n�mero de nodos de 
 * la matriz dos veces, separado por espacio.
 * En las dem�s l�neas aparece la matriz de distancias entre los 
 * nodos. La matriz deber�a ser sim�trica, as� que s�lo leemos 
 * el "triangualo".
 */
public class MatrixGraphLoaderSquare extends MatrixGraphLoader {

	private static MatrixGraphLoaderSquare instance = new MatrixGraphLoaderSquare();

	public static MatrixGraphLoaderSquare getInstance() {
		return instance;
	}

	@Override
	public MatrixGraph loadGraph(InputStream is, int totalNodes) throws IOException, FormatException {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String linea = br.readLine();
		if (linea == null) {
			throw new FormatException("El fichero est� vac�o");
		}

		StringTokenizer st = new StringTokenizer(linea);
		int numberNodes;
		try {
			numberNodes = Integer.parseInt(st.nextToken());
		} catch (NumberFormatException e) {
			throw new FormatException("Cabecera incorrecta", e);
		}

		float weights[][] = new float[numberNodes - 1][];
		for (int i = 0; i < numberNodes - 1; i++) {
			weights[i] = new float[i + 1];
		}

		br.readLine();
		linea = br.readLine();
		int lineNumber = 1;
		while (linea != null) {

			st = new StringTokenizer(linea);

			int valoresLeer = lineNumber;
			for (int i = 0; i < valoresLeer; i++) {
				String valor;
				try {
					valor = st.nextToken();
				} catch (NoSuchElementException e) {
					throw new FormatException("Error al leer la l�nea", e);
				}
				try {
					weights[lineNumber - 1][i] = Float.parseFloat(valor);
				} catch (NumberFormatException e) {
					throw new FormatException("El valor " + valor + " no es un float", e);
				}
			}

			linea = br.readLine();
			lineNumber++;
		}

		if (lineNumber < numberNodes) {
			throw new FormatException("Fichero Incompleto");
		}

		if (totalNodes == -1) {
			return new MatrixGraph(weights);
		} else {
			return new MatrixGraph(weights, totalNodes);
		}
	}

}
