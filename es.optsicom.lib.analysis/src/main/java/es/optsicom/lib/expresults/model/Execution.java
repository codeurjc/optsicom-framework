package es.optsicom.lib.expresults.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

@Entity
public class Execution {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private InstanceDescription instance;
	
	@ManyToOne
	private MethodDescription method;
	
	@OneToMany(mappedBy="execution",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Event> events = new ArrayList<Event>();
	
	private int numExecution;
	
	private long timeLimit;

	@JsonIgnore
	@ManyToOne
	private Experiment experiment;
	
	@JsonCreator
	public Execution(@JsonProperty("experiment") Experiment experiment, @JsonProperty("instance") InstanceDescription instance, @JsonProperty("method") MethodDescription method, @JsonProperty("timeLimit") long timeLimit, @JsonProperty("numExecution") int numExecution) {
		super();
		this.instance = instance;
		this.method = method;
		this.numExecution = numExecution;
		this.experiment = experiment;
		this.timeLimit = timeLimit;
	}

	public Execution(Experiment experiment, InstanceDescription instance, MethodDescription method, long timeLimit) {
		super();
		this.instance = instance;
		this.method = method;
		this.numExecution = 1;
		this.experiment = experiment;
		this.timeLimit = timeLimit;
	}
	
	public Execution() {}
	
	public Execution(Experiment experiment, MethodDescription method, InstanceDescription instance) {
		this(experiment, instance, method, -1);
	}

	public MethodDescription getMethod() {
		return method;
	}
	
	public InstanceDescription getInstance() {
		return instance;
	}
	
	@JsonIgnore
	public Experiment getExperiment() {
		return experiment;
	}
	
	public int getNumExecution() {
		return numExecution;
	}
	
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		Collections.sort(events);
		this.events = events;		
	}
	
	public long getTimeLimit() {
		return timeLimit;
	}
	
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;		
	}
	
	public Event getLastEvent(String eventName) {
		return getLastEvent(eventName, -1);
	}
		
	public Event getLastEvent(String eventName, long timelimit) {

		if (timelimit == -1) {
			timelimit = Long.MAX_VALUE;
		}

		Event event = null;
		for (int j = events.size() - 1; j >= 0; j--) {
			Event anEvent = events.get(j);
			if (anEvent.getTimestamp() <= timelimit && (event == null || anEvent.getTimestamp() > event.getTimestamp())) {
				if (anEvent.getName().equals(eventName)) {
					event = anEvent;
				}
			}
		}

		return event;
	}
	
	public List<Event> getEventsWithValue(String eventName, Object value) {
		
		List<Event> newEvents = new ArrayList<Event>();
		for (Event event : events) {
			if (event != null && value.equals(event.getValue())) {
				newEvents.add(event);
			}
		}

		return newEvents;
	}

	public void addFinishEvents(double weight, Object infoToSave, long executionTime, Execution exec) {
		// TODO Auto-generated method stub
		
	}

	public void addEvent(Event event) {
		this.events.add(event);		
	}

	@JsonIgnore
	public long getExecutionTime() {
		for(int i=events.size()-1; i>=0; i--){
			Event e = events.get(i);
			if(e.getName().equals(Event.FINISH_TIME_EVENT)){
				return e.getTimestamp();
			}
		}
		throw new RuntimeException("No FINISH_TIME_EVENT found");
	}

}
