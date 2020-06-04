package es.optsicom.lib.approx.experiment;

import es.optsicom.lib.approx.algorithm.ConstructiveImprovement;
import es.optsicom.lib.approx.constructive.Constructive;

public class ConstructiveExpConf extends ApproxExpConf {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addConstructive(Constructive constructive) {
		addMethod(new ConstructiveImprovement(constructive));
	}

}
