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
package es.optsicom.lib;

import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.IWeighed;

public abstract class Solution<I extends Instance> implements IWeighed {
	
	protected SolutionFactory factory;
	
	public abstract double getWeight();

	public abstract Solution<I> createCopy();

	public abstract I getInstance();

	/**
	 * Returns the info that the system save in experiments. This info is later
	 * passed with original Instance to regenerate this solution if necessary.
	 * With this strategy, the system saves a lot of disk space. This strategy
	 * allows a more flexible evolution of Solution classes because the saved
	 * info is a canonical info. The returned value must be inmutable or a copy
	 * of original info.
	 * 
	 * @return
	 */
	public abstract Object getInfoToSave();

	/**
	 * Clients should implement this method. This method will be called when Optsicom
	 * needs to know whether this solution is better than another one. This
	 * method should compare solutions based on their quality. It must return
	 * true if this is a better solution than <code>aSolution</code>. False
	 * otherwise.
	 * 
	 * @return true if this solution is better that <code>aSolution</code>,
	 *         false otherwise.
	 */
	public abstract boolean isBetterThan(Solution<I> aSolution);

	/**
	 * This method provides a comparison based on the weight.
	 * 
	 * @return 1 if this solutions has higher weight that <code>o</code>, -1
	 *         if this solution has lower weight than <code>o</code>, and 0
	 *         if their weights are equal. That is, solutions are compared using
	 *         their weights values.
	 * 
	 */
	public int compareTo(IWeighed o) {
		return (this.getWeight() > o.getWeight()) ? 1 : (this.getWeight() < o.getWeight()) ? -1 : 0;
	}
	
	public SolutionFactory getFactory() {
		return factory;
	}
	
	public abstract void asSolution(Solution<I> solution);

	public String toStringConsoleExperiment(){
		return " Weight:"+this.getWeight()+" "+ArraysUtil.toStringObj(getInfoToSave());
	}
	
}
