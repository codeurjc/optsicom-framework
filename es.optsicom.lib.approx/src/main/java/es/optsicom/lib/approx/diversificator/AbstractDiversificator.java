package es.optsicom.lib.approx.diversificator;

import es.optsicom.lib.Solution;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

public abstract class AbstractDiversificator<S extends Solution<?>> implements Diversificator<S> {
	
	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
