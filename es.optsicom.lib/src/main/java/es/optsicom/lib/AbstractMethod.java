package es.optsicom.lib;

import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class AbstractMethod<S extends Solution<I>, I extends Instance> implements Method<S,I> {

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
