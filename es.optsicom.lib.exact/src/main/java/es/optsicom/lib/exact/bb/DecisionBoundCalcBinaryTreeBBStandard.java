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

public interface DecisionBoundCalcBinaryTreeBBStandard<I extends Instance> {

	public void setInstance(I instance);

	public int getNumFixedNodes();

	public int getNumSelectedNodes();

	public boolean[] getSelectedNodes();

	public void fixNextNode(boolean b);

	public void freeLastFixedNode();

	public boolean prone();

}
