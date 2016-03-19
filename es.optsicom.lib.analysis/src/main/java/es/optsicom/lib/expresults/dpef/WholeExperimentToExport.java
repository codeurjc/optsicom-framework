package es.optsicom.lib.expresults.dpef;

import java.util.List;
import java.util.Map;

import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;

public class WholeExperimentToExport {

	private Experiment experiment;
	private List<Execution> executions;
	private Map<MethodDescription, String> expMethodNames;

	public WholeExperimentToExport() {
		// Jackson needed constructor
	}

	public WholeExperimentToExport(Experiment experiment, List<Execution> executions,
			Map<MethodDescription, String> expMethodNames) {
		super();
		this.experiment = experiment;
		this.executions = executions;
		this.expMethodNames = expMethodNames;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public List<Execution> getExecutions() {
		return executions;
	}

	public void setExecutions(List<Execution> executions) {
		this.executions = executions;
	}

	public Map<MethodDescription, String> getExpMethodNames() {
		return expMethodNames;
	}

	public void setExpMethodNames(Map<MethodDescription, String> expMethodNames) {
		this.expMethodNames = expMethodNames;
	}

}
