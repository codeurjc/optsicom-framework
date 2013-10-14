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
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public class BoundCalcPaperToStandardAdapter<I extends Instance> implements BoundCalcBinaryTreeBBPaper<I> {

	private BoundCalcBinaryTreeBBStandard<I> boundCalcStandard;

	//	private int numNodes;
	//	private int[] selectedNodes;

	public BoundCalcPaperToStandardAdapter(BoundCalcBinaryTreeBBStandard<I> boundCalcStandard) {
		this.boundCalcStandard = boundCalcStandard;
	}

	@Override
	public double getUpperBound() {
		return boundCalcStandard.getUpperBound();
	}

	@Override
	public void setInstance(I instance) {
		this.boundCalcStandard.setInstance(instance);
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	@Id
	public BoundCalcBinaryTreeBBStandard<I> getBoundCalcStandard() {
		return boundCalcStandard;
	}

	@Override
	public void addNodeToSolution(int node) {

		int numFixedNodes = this.boundCalcStandard.getNumFixedNodes();

		for (int i = numFixedNodes; i < node; i++) {
			this.boundCalcStandard.fixNextNode(false);
		}

		this.boundCalcStandard.fixNextNode(true);
	}

	@Override
	public void removeNodeFromSolution(int node) {
		//Hay dos estrategias, o bien se liberan todos los nodos hasta el último seleccionado o
		//bien se es más inteligente y se dejan fijas a no seleccionado porque luego se va a
		//seleccionar otro nodo posterior. 

		int numFixedNodes = this.boundCalcStandard.getNumFixedNodes();

		for (int i = numFixedNodes - 1; i >= node; i--) {
			this.boundCalcStandard.freeLastFixedNode();
		}

	}

	@Override
	public boolean prone(double bestSolutionWeight) {
		return this.boundCalcStandard.prone(bestSolutionWeight);
	}

}
