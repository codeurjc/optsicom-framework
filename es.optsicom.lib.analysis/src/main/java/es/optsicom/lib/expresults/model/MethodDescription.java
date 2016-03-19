package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class MethodDescription extends ElementDescription {

	MethodDescription() {
		// JPA needed constructor
	}

	public MethodDescription(String name) {
		super(name);
	}

	@JsonCreator
	public MethodDescription(@JsonProperty("properties") DBProperties properties) {
		super(properties);
	}

	@Override
	public String toString() {
		return "MethodDescription [" + getProperties() + "]";
	}

}
