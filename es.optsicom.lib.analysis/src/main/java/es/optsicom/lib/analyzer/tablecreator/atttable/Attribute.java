/**
 * 
 */
package es.optsicom.lib.analyzer.tablecreator.atttable;

import es.optsicom.lib.util.Strings;
import es.optsicom.lib.util.description.Descriptive;

public class Attribute implements Comparable<Attribute> {

	private String name;
	private Object value;

	// El titleText se utiliza para mostrar el valor en una SheetTable.
	// Si no existe, su hace un toString() del value. Su valor
	// se indica con el alias por defecto del objeto o bien con un
	// alias especificado en el TableConfiguration.
	private String titleText;

	public Attribute(String name, Object value, String titleText) {
		super();
		this.name = name;
		this.value = value;
		this.titleText = titleText;
	}

	public Attribute(String name, Object value) {
		this(name, value, null);
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getTitle() {
		if (titleText == null) {
			titleText = value.toString();
		}
		return titleText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", value=" + value + ", titleText=" + titleText + "]";
	}

	@SuppressWarnings("unchecked")
	public int compareTo(Attribute att) {
		if (value instanceof Comparable) {
			return ((Comparable<Object>) value).compareTo(att.value);
		} else if (value instanceof Descriptive) {
			Descriptive dValue = (Descriptive) value;
			return dValue.getProperties().compareTo(((Descriptive) att.value).getProperties());
		} else {
			return Strings.compareNatural(value.toString(), att.value.toString());
		}
	}
}