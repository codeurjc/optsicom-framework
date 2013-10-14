package es.optsicom.lib.analyzer.tablecreator.statisticcalc;

import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.InstanceDescription;

public interface RelativeValueProvider {

	public abstract Number getValue(InstanceDescription instance, ExperimentManager experimentResults);
	
}
