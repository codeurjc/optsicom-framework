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
package es.optsicom.lib.exact.bb;

import es.optsicom.lib.Instance;
import es.optsicom.lib.util.description.Descriptive;

public interface BoundCalcBinaryTreeBBStandard<I extends Instance> extends Descriptive {

	double getUpperBound();

	boolean isRandomFixedAllowed();

	void fixNode(int i, boolean selected);

	void fixNextNode(boolean selected);

	void freeLastFixedNode();

	int getNumFixedNodes();

	int getNumSelectedNodes();

	boolean[] getSelectedNodes();

	boolean[] getFixedNodes();

	void setInstance(I instance);

	//Método usado para optimizar el cálculo de la cota
	boolean prone(double bestSolutionWeight);

}
