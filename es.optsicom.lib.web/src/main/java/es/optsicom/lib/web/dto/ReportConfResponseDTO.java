package es.optsicom.lib.web.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportConfResponseDTO {

	private List<Long> expId;
	private List<Long> selectedMethods;
	private List<ExperimentMethodBasicResponseDTO> expMethods;
	private List<Long> selectedInstances;
	private List<InstanceBasicResponseDTO> instances;

	public ReportConfResponseDTO() {
		this.expId = new ArrayList<>();
		this.selectedMethods = new ArrayList<>();
		this.expMethods = new ArrayList<>();
		this.instances = new ArrayList<>();
	}

	public ReportConfResponseDTO(List<Long> expId, List<Long> selectedMethods,
	        List<ExperimentMethodBasicResponseDTO> expMethods,
	        List<Long> selectedInstances, List<InstanceBasicResponseDTO> instances) {
		this.expId = expId;
		this.selectedMethods = selectedMethods;
		this.expMethods = expMethods;
		this.selectedInstances = selectedInstances;
		this.instances = instances;
	}

	public List<Long> getExpId() {
		return expId;
	}

	public List<Long> getSelectedMethods() {
		return selectedMethods;
	}

	public List<ExperimentMethodBasicResponseDTO> getExpMethods() {
		return expMethods;
	}

	public List<Long> getSelectedInstances() {
		return selectedInstances;
	}

	public List<InstanceBasicResponseDTO> getInstances() {
		return instances;
	}

}
