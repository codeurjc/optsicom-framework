package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;

@Entity
public class DoubleEvent extends Event {
	
	private double value;
	
	public DoubleEvent() {
		//JPA Needed constructor
	}
	
	@JsonCreator
	public DoubleEvent(@JsonProperty("time") long time, @JsonProperty("name") String name, @JsonProperty("value") double value) {
		this(null, time, name, value);
	}
	
	public DoubleEvent(Execution execution, long time, String name, double value) {
		super(execution,time, name);
		this.value = value;
	}
	
	public Double getValue() {
		return value;
	}
	
	@JsonIgnore
	public double getPrimitiveValue(){
		return value;
	}
}
