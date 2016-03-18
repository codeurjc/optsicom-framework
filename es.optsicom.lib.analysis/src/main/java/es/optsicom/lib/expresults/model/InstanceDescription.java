package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.expresults.model.DBProperties;

@Entity
public class InstanceDescription extends ElementDescription {

	public InstanceDescription(String name){
		super(name);
	}
	
	public InstanceDescription() {
		//JPA needed constructor
	}
	
	@JsonCreator
	public InstanceDescription(@JsonProperty("properties") DBProperties properties) {
		super(properties);
	}

	@Override
	public String toString() {
		return "InstanceDescription [" + getProperties() + "]";
	}
	
}
