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

public class RandomMatrixGraphGeneratorApp3 {

	public static void main(String[] args) throws IOException {

		int[] ns = { 25, 50, 100, 125, 150, 200, 500 };
		int numberProblems = 10;

		File dir = new File("../MaxDivProblem/instancefiles3");
		dir.mkdirs();

		RandomMatrixGraphGenerator creator = new RandomMatrixGraphGenerator();
		MatrixGraphSaverDistanceList saver = MatrixGraphSaverDistanceList.getInstance();

		for (int n : ns) {
			for (int i = 1; i <= numberProblems; i++) {
				System.out.println("Creating graph " + i);
				MatrixGraph mg = creator.createGraphInt(n, 2, 20);
				saver.saveGraph(mg, new File(dir, "Silva (2-20) " + n + " " + (i) + ".txt"));
			}
		}

	}
}
