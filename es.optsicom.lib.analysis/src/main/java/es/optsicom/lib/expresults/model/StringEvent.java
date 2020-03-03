package es.optsicom.lib.expresults.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class StringEvent extends Event {

	@Column(length = 20000, columnDefinition = "text")
	private String value;

	public StringEvent() {
		// JPA Needed constructor
	}

	@JsonCreator
	public StringEvent(@JsonProperty("time") long time, @JsonProperty("name") String name,
			@JsonProperty("value") String value) {
		this(null, time, name, value);
	}

	public StringEvent(Execution execution, long time, String name, String value) {
		super(execution, time, name);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
