package es.optsicom.lib.web.model.reportrest;

import java.util.ArrayList;
import java.util.List;
import es.optsicom.lib.web.model.experimentrest.MethodName;

public class ReportRestConfiguration {

	private List<Long> expId;
	private List<Long> selectedMethods;
	private List<MethodName> methods;
	private boolean bestValues;

	public ReportRestConfiguration() {
		this.expId = new ArrayList<>();
		this.selectedMethods = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.bestValues = false;
	}

	public ReportRestConfiguration(List<Long> expId, List<Long> selectedMethods, List<MethodName> methods,
			boolean bestValues) {
		this.expId = expId;
		this.selectedMethods = selectedMethods;
		this.methods = methods;
		this.bestValues = bestValues;
	}

	public List<Long> getExpId() {
		return expId;
	}

	public List<Long> getSelectedMethods() {
		return selectedMethods;
	}

	public List<MethodName> getMethods() {
		return methods;
	}

	public boolean isBestValues() {
		return bestValues;
	}

	public void addMethod(MethodName method) {
		if (method != null) {
			this.methods.add(method);
		}
	}

}
