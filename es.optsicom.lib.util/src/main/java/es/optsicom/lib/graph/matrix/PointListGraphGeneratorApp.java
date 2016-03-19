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
import java.util.List;

public class PointListGraphGeneratorApp {

	public static void main(String[] args) throws IOException {

		File dir = new File("../MavDivProblem/instancefiles2");
		dir.mkdirs();

		int[] ns = { /* 25, 50, 100, 125, 150, */200, 500 };
		int numberProblems = 10;
		String problemsName = "Glover Exp2";

		PointListGraphGenerator creator = new PointListGraphGenerator();
		PointListSaver saver = PointListSaver.getInstance();

		for (int n : ns) {
			for (int i = 1; i <= numberProblems; i++) {

				System.out.println("Creating graph " + i);
				List<Point> points = creator.generateGloverPoints(n);
				saver.saveGraph(points, new File(dir, problemsName + " (n " + n + ") " + i + ".txt"));
			}
		}

	}

}
