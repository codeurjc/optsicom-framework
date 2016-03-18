package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.expresults.model.DBProperties;

@Entity
public class ComputerDescription extends ElementDescription {

	public ComputerDescription(String name) {
		super(name);
	}
	
	@JsonCreator
	public ComputerDescription(@JsonProperty("properties") DBProperties properties){
		super(properties);
	}
	
	public ComputerDescription(){
		//JPA needed constructor
	}

	@Override
	public String toString() {
		return "ComputerDescription [" + getProperties() + "]";
	}
	
}
