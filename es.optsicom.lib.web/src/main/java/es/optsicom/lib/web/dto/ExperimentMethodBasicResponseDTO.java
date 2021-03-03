package es.optsicom.lib.web.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;

public class ExperimentMethodBasicResponseDTO {

	private long id;
	private List<MethodBasicResponseDTO> methods;
	private String name;

	public ExperimentMethodBasicResponseDTO(long id, List<MethodBasicResponseDTO> methods, String name) {
		super();
		this.id = id;
		this.methods = methods;
		this.name = name;
	}

	public ExperimentMethodBasicResponseDTO(ExperimentManager expManager) {

		Experiment exp = expManager.getExperiment();

		this.id = exp.getId();

		// TODO check these lists as they do not work with streams if a new ArrayList is
		// not used
		this.methods = new ArrayList<>(expManager.getMethods()).stream()
		        .map(method -> new MethodBasicResponseDTO(
		                method.getId(),
		                expManager.getExperimentMethodName(method),
		                method.getName()))
		        .collect(Collectors.toList());

		this.name = exp.getName();
	}

	public long getId() {
		return id;
	}

	public List<MethodBasicResponseDTO> getMethods() {
		return methods;
	}

	public String getName() {
		return name;
	}

}
