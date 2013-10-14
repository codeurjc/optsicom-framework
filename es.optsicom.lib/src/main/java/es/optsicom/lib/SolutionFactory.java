package es.optsicom.lib;

import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

/**
 * <p>A solution factory that can be used to accommodate different types of solutions within the same problem.
 * Users can extend this class and provide an implementation of {@link #createSolution(Instance)} and
 * {@link #createSolution(Instance, Object)}.</p> 
 * 
 *  <p>Instances of this class can be passed on to optimization methods via the {@link AbstractMethod#setFactory(SolutionFactory)} method.</p>
 *  
 *  <p>Users are encouraged to associate the factory instance to the solution via the {@link Solution#factory} attribute.</p>
 * 
 * @author Patxi Gortázar
 *
 * @param <S> Solution type that the concrete factory creates
 * @param <I> Instance type for the problem being considered
 */
public abstract class SolutionFactory<S extends Solution<I>, I extends Instance> implements Descriptive {

	/**
	 * Creates an empty solution. The definition of <em>empty</em> depends on the problem. 
	 * 
	 * @param instance The instance
	 * @return a new solution
	 */
	public abstract S createSolution(I instance);
	
	/**
	 * Creates a solution initialized with the initializationData passed as an argument. 
	 * The concrete meaning of this initialization data depends on the solution.
	 * 
	 * @param instance The instance
	 * @param initializationData Data to initialize the instance with
	 * @return a new solution initialized with the data passed as an argument
	 */
	public abstract S createSolution(I instance, Object initializationData);
	
	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}
}
