package es.optsicom.lib.multiobjective;

import es.optsicom.lib.Instance;
import es.optsicom.lib.SolutionFactory;
import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class MOAbstractMethod<S extends MultiObjectiveSolution<I>, I extends Instance> implements MultiObjectiveMethod<S, I> {

	protected SolutionFactory<S, I> factory;
	
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	@Id
	public String getName(){
		return this.getClass().getSimpleName();
	}

	public void setFactory(SolutionFactory<S, I> factory) {
		this.factory = factory;
	}
	
	@Id
	public SolutionFactory<S, I> getFactory() {
		return factory;
	}

}
