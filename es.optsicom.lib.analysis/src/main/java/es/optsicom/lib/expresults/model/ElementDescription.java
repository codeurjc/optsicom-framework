package es.optsicom.lib.expresults.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.util.description.Descriptive;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ElementDescription implements Descriptive {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	private String name;

	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private DBProperties properties;

	protected ElementDescription() {
		// JPA needed constructor
	}

	public ElementDescription(String name) {
		this.name = name;
		this.properties = new DBProperties(name);
	}

	public long getId() {
		return id;
	}
	
	@JsonCreator
	public ElementDescription(@JsonProperty("properties") DBProperties properties) {
		this.properties = properties;
		this.name = properties.getName();
	}

	public DBProperties getProperties() {
		return properties;
	}

	@JsonIgnore
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementDescription other = (ElementDescription) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}
	
}
