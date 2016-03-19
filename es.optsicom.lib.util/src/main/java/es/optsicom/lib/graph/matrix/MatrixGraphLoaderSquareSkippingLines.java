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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import es.optsicom.lib.graph.Graph;

/**
 * Loader that loads an symmetric square matrix skipping the first lines (to
 * store additional info depending on problem).
 */
public class MatrixGraphLoaderSquareSkippingLines extends MatrixGraphLoader {

	private int numLinesToSkip;

	public MatrixGraphLoaderSquareSkippingLines(int numLinesToSkip) {
		this.numLinesToSkip = numLinesToSkip;
	}

	@Override
	public MatrixGraph loadGraph(InputStream is) throws IOException, FormatException {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		List<String> skippedLines = new ArrayList<>();

		for (int i = 0; i < numLinesToSkip; i++) {
			String line = br.readLine();
			if (line != null) {
				skippedLines.add(line);
			} else {
				throw new FormatException("The file has less lines than numLinesToSkip (" + numLinesToSkip + ")");
			}
		}

		String firstMatrixLine = br.readLine();
		StringTokenizer st = new StringTokenizer(firstMatrixLine);
		int numNodes = st.countTokens();

		float[][] weights = createSymmetricMatrix(numNodes);

		String line = br.readLine();

		int numLine = 1;
		while (line != null) {
			readLine(weights, line, numLine);
			line = br.readLine();
			numLine++;
		}

		if (numLine < numNodes - 1) {
			throw new FormatException("The file should have at least " + numNodes + " lines with matrix data");
		}

		MatrixGraph matrixGraph = new MatrixGraph(weights);

		matrixGraph.setSkippedLines(skippedLines);

		return matrixGraph;
	}

	private void readLine(float[][] weights, String line, int numLine) throws FormatException {

		StringTokenizer st = new StringTokenizer(line);

		int numValuesToLoad = numLine;
		for (int i = 0; i < numValuesToLoad; i++) {

			String value;
			try {
				value = st.nextToken();
			} catch (NoSuchElementException e) {
				throw new FormatException(
						"Exception reading line " + line + ". It should have at least " + numValuesToLoad + " elements",
						e);
			}
			try {
				weights[numLine - 1][i] = Float.parseFloat(value);
			} catch (NumberFormatException e) {
				throw new FormatException("Token " + value + " should be a number", e);
			}
		}
	}

	private float[][] createSymmetricMatrix(int numNodes) {
		float weights[][] = new float[numNodes - 1][];
		for (int i = 0; i < numNodes - 1; i++) {
			weights[i] = new float[i + 1];
		}
		return weights;
	}

	@Override
	public Graph loadGraph(InputStream is, int totalNodes) throws IOException, FormatException {
		return loadGraph(is);
	}

}
