package es.optsicom.lib.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;

public class ExperimentExtendResponseDTO {

	private long id;
	private String researcherName;
	private List<InstanceExtendResponseDTO> instances;
	private List<MethodExtendResponseDTO> methods;
	private Date date;
	private String computerName;
	private long timeLimit;
	private long maxTimeLimit;
	private String problemBestMode;
	private String problemName;
	private boolean recordEvolution;
	private String name;
	private String useCase;
	private String description;
	private int numExecs;

	public ExperimentExtendResponseDTO(long id, String researcherName, List<InstanceExtendResponseDTO> instances,
	        List<MethodExtendResponseDTO> methods, Date date, String computerName, long timeLimit, long maxTimeLimit,
	        String problemBestMode, String problemName, boolean recordEvolution, String name, String useCase,
	        String description, int numExecs) {
		this.id = id;
		this.researcherName = researcherName;
		this.instances = instances;
		this.methods = methods;
		this.date = date;
		this.computerName = computerName;
		this.timeLimit = timeLimit;
		this.maxTimeLimit = maxTimeLimit;
		this.problemBestMode = problemBestMode;
		this.problemName = problemName;
		this.recordEvolution = recordEvolution;
		this.name = name;
		this.useCase = useCase;
		this.description = description;
		this.numExecs = numExecs;
	}

	public ExperimentExtendResponseDTO(ExperimentManager expManager) {

		Experiment exp = expManager.getExperiment();

		this.id = exp.getId();
		this.researcherName = exp.getResearcher().getName();

		// TODO check these lists as they do not work with streams if a new ArrayList is
		// not used
		this.instances = new ArrayList<>(exp.getInstances()).stream()
		        .map(instance -> new InstanceExtendResponseDTO(instance.getId(),
		                instance.getName(),
		                instance.getProperties().get("filename"),
		                instance.getProperties().get("instancesetid"),
		                instance.getProperties().get("usecase")))
		        .collect(Collectors.toList());

		this.methods = new ArrayList<>(expManager.getMethods()).stream()
		        .map(method -> new MethodExtendResponseDTO(
		                method.getId(),
		                expManager.getExperimentMethodName(method),
		                method.getName(),
		                method.getProperties().getMap()))
		        .collect(Collectors.toList());

		this.date = exp.getDate();
		this.computerName = exp.getComputer().getName();
		this.timeLimit = exp.getTimeLimit();
		this.maxTimeLimit = exp.getMaxTimeLimit();
		this.problemBestMode = exp.getProblemBestMode().toString();
		this.problemName = exp.getProblemName();
		this.recordEvolution = exp.isRecordEvolution();
		this.name = exp.getName();
		this.useCase = exp.getUseCase();
		this.description = exp.getDescription();
		this.numExecs = exp.getNumExecs();
	}

	public long getId() {
		return id;
	}

	public String getResearcherName() {
		return researcherName;
	}

	public List<InstanceExtendResponseDTO> getInstances() {
		return instances;
	}

	public List<MethodExtendResponseDTO> getMethods() {
		return methods;
	}

	public Date getDate() {
		return date;
	}

	public String getComputerName() {
		return computerName;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public long getMaxTimeLimit() {
		return maxTimeLimit;
	}

	public String getProblemBestMode() {
		return problemBestMode;
	}

	public String getProblemName() {
		return problemName;
	}

	public boolean isRecordEvolution() {
		return recordEvolution;
	}

	public String getName() {
		return name;
	}

	public String getUseCase() {
		return useCase;
	}

	public String getDescription() {
		return description;
	}

	public int getNumExecs() {
		return numExecs;
	}

}
