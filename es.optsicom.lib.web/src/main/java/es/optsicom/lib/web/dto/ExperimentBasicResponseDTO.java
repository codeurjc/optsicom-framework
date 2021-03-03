package es.optsicom.lib.web.dto;

import java.util.Date;

import es.optsicom.lib.expresults.model.Experiment;

public class ExperimentBasicResponseDTO {

	private long id;
	private String researcherName;
	private int nInstances;
	private int nMethods;
	private Date date;
	private String problemName;
	private String name;

	public ExperimentBasicResponseDTO(long id, String researcherName, int nInstances, int nMethods, Date date,
	        String problemName, String name) {
		this.id = id;
		this.researcherName = researcherName;
		this.nInstances = nInstances;
		this.nMethods = nMethods;
		this.date = date;
		this.problemName = problemName;
		this.name = name;
	}

	public ExperimentBasicResponseDTO(Experiment exp) {
		this(exp.getId(),
		        exp.getResearcher().getName(),
		        exp.getInstances().size(),
		        exp.getMethods().size(),
		        exp.getDate(),
		        exp.getProblemName(),
		        exp.getName());
	}

	public long getId() {
		return id;
	}

	public String getResearcherName() {
		return researcherName;
	}

	public int getnInstances() {
		return nInstances;
	}

	public int getnMethods() {
		return nMethods;
	}

	public Date getDate() {
		return date;
	}

	public String getProblemName() {
		return problemName;
	}

	public String getName() {
		return name;
	}

}
