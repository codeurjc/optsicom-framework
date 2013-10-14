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
package es.optsicom.lib.approx.constructive;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.SolutionFactory;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

/**
 * An abstract implementation of {@link Constructive} interface that provides support for instance management, and implements
 * {@link Constructive#createSolutions(int)} and {@link Constructive#createSolutionsInTime(long)}.
 * Subclassers must implement the {@link Constructive#createSolution()} method, the init...() methods and the 
 * {@link Constructive#isDeterminist()} method.
 * @author patxi
 *
 * @param <S> Solution type
 * @param <I> Instance type
 */
public abstract class AbstractConstructive<S extends Solution<I>, I extends Instance> implements Constructive<S, I> {

	protected I instance;
	protected SolutionFactory<S, I> factory; 

	//	public AbstractConstructive(I instance) {
	//		this.instance = instance;
	//	}

	public AbstractConstructive() {
	}

	public I getInstance() {
		return instance;
	}

	public void setInstance(I instance) {
		this.instance = instance;
	}
	
	public void removeInstance() {
		this.instance = null;
	}
	
	public void setFactory(SolutionFactory<S, I> factory) {
		this.factory = factory;
	}
	
	@Id
	public SolutionFactory<S, I> getFactory() {
		return factory;
	}

	public List<S> createSolutions(int numSolutions) {
		List<S> solutions = new ArrayList<S>();
		for (int i = 0; i < numSolutions; i++) {
			solutions.add(createSolution());
		}
		return solutions;
	}

	public List<S> createSolutionsInTime(long millis) {
		List<S> solutions = new ArrayList<S>();
		long finishTime = System.currentTimeMillis() + millis;

		long now;
		do {
			solutions.add(createSolution());
			now = System.currentTimeMillis();
		} while (now < finishTime);

		return solutions;
	}

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}
}
