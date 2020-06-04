package es.optsicom.lib.expresults.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import es.optsicom.lib.util.BestMode;

@Entity
@JsonPropertyOrder(value = { "name", "date", "description", "timeLimit", "maxTimeLimit", "problemBestMode",
		"problemName", "recordEvolution", "useCase", "numExecs", "researcher" })
@JsonSerialize(include = Inclusion.NON_NULL)
public class Experiment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private Researcher researcher;

	@ManyToMany
	private List<InstanceDescription> instances = new ArrayList<InstanceDescription>();

	@ManyToMany
	private List<MethodDescription> methods = new ArrayList<MethodDescription>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne
	private ComputerDescription computer;

	private long timeLimit;

	private long maxTimeLimit;

	private BestMode problemBestMode;

	private String problemName;

	private boolean recordEvolution;

	private String name;

	private String useCase;

	private String description;

	private int numExecs;

	public Experiment() {
	}

	@JsonCreator
	public Experiment(@JsonProperty("name") String name, @JsonProperty("researcher") Researcher researcher,
			@JsonProperty("date") Date date, @JsonProperty("computer") ComputerDescription computer) {
		super();
		this.researcher = researcher;
		this.date = date;
		this.computer = computer;
		this.name = name;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public List<InstanceDescription> getInstances() {
		return instances;
	}

	public List<MethodDescription> getMethods() {
		return methods;
	}

	public Date getDate() {
		return date;
	}

	public ComputerDescription getComputer() {
		return computer;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Experiment [id=" + id + ", researcher=" + researcher + ", instances=" + instances + ", methods="
				+ methods + ", date=" + date + ", computer=" + computer + ", timeLimit=" + timeLimit + ", maxTimeLimit="
				+ maxTimeLimit + ", problemBestMode=" + problemBestMode + ", problemName=" + problemName
				+ ", recordEvolution=" + recordEvolution + ", name=" + name + ", useCase=" + useCase + ", description="
				+ description + ", numExecs=" + numExecs + "]";
	}

	public void setInstances(List<InstanceDescription> instances) {
		this.instances = instances;
	}

	public void setMethods(List<MethodDescription> methods) {
		this.methods = methods;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public BestMode getProblemBestMode() {
		return problemBestMode;
	}

	public void setProblemBestMode(BestMode problemBestMode) {
		this.problemBestMode = problemBestMode;
	}

	public long getMaxTimeLimit() {
		return maxTimeLimit;
	}

	public void setMaxTimeLimit(long maxTimeLimit) {
		this.maxTimeLimit = maxTimeLimit;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public boolean isRecordEvolution() {
		return recordEvolution;
	}

	public void setRecordEvolution(boolean recordEvolution) {
		this.recordEvolution = recordEvolution;
	}

	public String getUseCase() {
		return useCase;
	}

	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumExecs() {
		return numExecs;
	}

	public void setNumExecs(int numExecs) {
		this.numExecs = numExecs;
	}

}
