package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;

@Entity
public class LongEvent extends Event {
	
	private long value;
	
	public LongEvent() {
		//JPA Needed constructor
	}
	
	@JsonCreator
	public LongEvent(@JsonProperty("time") long time, @JsonProperty("name") String name, @JsonProperty("value") long value) {
		this(null, time, name, value);
	}
	
	public LongEvent(Execution execution,  long time,  String name,  long value) {
		super(execution,time, name);
		this.value = value;
	}

	public Long getValue() {
		return value;
	}
	
	@JsonIgnore
	public long getPrimitiveValue(){
		return value;
	}
	
}
