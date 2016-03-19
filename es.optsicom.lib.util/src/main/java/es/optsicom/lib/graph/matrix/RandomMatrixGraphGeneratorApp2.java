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
import java.io.IOException;

public class RandomMatrixGraphGeneratorApp2 {

	public static void main(String[] args) throws IOException {

		int numberNodes = 30;
		int numberProblems = 5;

		File dir = new File("MaxDivProb2\\Simple" + numberNodes);
		dir.mkdirs();

		RandomMatrixGraphGenerator creator = new RandomMatrixGraphGenerator();
		MatrixGraphSaverDistanceList saver = MatrixGraphSaverDistanceList.getInstance();

		for (int i = 0; i < numberProblems; i++) {
			System.out.println("Creating graph " + i);
			MatrixGraph mg = creator.createGraphFloat(numberNodes, 0, 100);
			saver.saveGraph(mg, new File(dir, "Simple " + numberNodes + " " + (i + 1) + ".txt"));
		}

	}
}
