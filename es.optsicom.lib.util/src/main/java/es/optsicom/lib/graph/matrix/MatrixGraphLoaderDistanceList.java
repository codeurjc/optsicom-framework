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
import java.util.StringTokenizer;

public class MatrixGraphLoaderDistanceList extends MatrixGraphLoader {

	private static MatrixGraphLoaderDistanceList instance = new MatrixGraphLoaderDistanceList();

	public static MatrixGraphLoaderDistanceList getInstance() {
		return instance;
	}

	@Override
	public MatrixGraph loadGraph(InputStream is, int totalNodes) throws IOException, FormatException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		try {

			String linea = br.readLine();
			if (linea == null) {
				throw new FormatException("The file is empty");
			}

			String additionalInfo = null;
			if(linea.contains(" ")){
				int indexOf = linea.indexOf(' ');
				additionalInfo = linea.substring(indexOf,linea.length());
				linea = linea.substring(0,indexOf);
			}
			
			int numberNodes = Integer.parseInt(linea);

			float weights[][] = new float[numberNodes - 1][];
			for (int i = 0; i < numberNodes - 1; i++) {
				weights[i] = new float[i + 1];
			}

			linea = br.readLine();
			while (linea != null) {

				StringTokenizer st = new StringTokenizer(linea);
				int colNumber = Integer.parseInt(st.nextToken());
				int rowNumber = Integer.parseInt(st.nextToken());
				float weight = Float.parseFloat(st.nextToken());

				weights[rowNumber - 1][colNumber] = weight;

				linea = br.readLine();
			}

			MatrixGraph mg;
			if (totalNodes == -1) {
				mg = new MatrixGraph(weights);
			} else {
				mg = new MatrixGraph(weights, totalNodes);
			}
			
			mg.setAdditionalInfo(additionalInfo);
			
			return mg;

		} catch (NumberFormatException e) {
			throw new FormatException("Format error",e);
		}
	}

}
