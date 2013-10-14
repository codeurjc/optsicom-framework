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

/** 
 * Las implementaciones de este interfaz representan una cota diseñada para la
 * exploración del árbol como hemos puesto en el paper del exacto, en contraposición
 * a la exploración que propone Pisinger. Con esta cota, si se selecciona el nodo
 * 4 quiere decir que los nodos anteriores ya quedan fijados, a no-seleccionados
 * o a seleccionados. 
 * @author mica
 *
 * @param <I>
 */
public interface BoundCalcBinaryTreeBBPaper<I extends Instance> extends Descriptive {

	public void setInstance(I instance);

	public void addNodeToSolution(int node);

	public void removeNodeFromSolution(int node);

	public double getUpperBound();

	public boolean prone(double bestSolutionWeight);

}
