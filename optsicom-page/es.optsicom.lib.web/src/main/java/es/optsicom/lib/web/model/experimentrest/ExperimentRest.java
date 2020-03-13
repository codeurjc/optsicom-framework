package es.optsicom.lib.web.model.experimentrest;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.expresults.model.Experiment;

public class ExperimentRest {

	private Experiment experiment;
	private List<MethodName> methodNames;

	public ExperimentRest() {
		this.experiment = new Experiment();
		this.methodNames = new ArrayList<>();
	}

	public ExperimentRest(Experiment experiment, List<MethodName> methodNames) {
		this.experiment = experiment;
		this.methodNames = methodNames;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public List<MethodName> getMethodNames() {
		return methodNames;
	}

	public void setMethodNames(List<MethodName> methodNames) {
		this.methodNames = methodNames;
	}
	
}
