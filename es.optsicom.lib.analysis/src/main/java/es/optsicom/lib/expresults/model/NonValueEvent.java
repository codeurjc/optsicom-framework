package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class NonValueEvent extends Event {

	NonValueEvent() {
		// JPA Needed constructor
	}

	@JsonCreator
	public NonValueEvent(@JsonProperty("time") long time, @JsonProperty("name") String name) {
		this(null, time, name);
	}

	public NonValueEvent(Execution execution, long time, String name) {
		super(execution, time, name);
	}

	@JsonIgnore
	public Object getValue() {
		return null;
	}

}
