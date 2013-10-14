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

public class RandomMatrixGraphGeneratorApp {

	public static void main(String[] args) throws IOException {

		File dir = new File("../MavDivProblem/instancefiles2");
		dir.mkdirs();

		int[] ns = { /*25, 50, 100, 125, 150,*/200, 500 };
		int numberProblems = 10;

		RandomMatrixGraphGenerator creator = new RandomMatrixGraphGenerator();
		MatrixGraphSaverDistanceList saver = MatrixGraphSaverDistanceList.getInstance();

		for (int n : ns) {
			for (int i = 1; i <= numberProblems; i++) {

				System.out.println("Creating graph " + i);
				MatrixGraph mg = creator.createGraphFloat(n, 10, 100);
				saver.saveGraph(mg, new File(dir, "SilvaReal (n " + n + ") " + i + ".txt"));
			}
		}

	}
}
