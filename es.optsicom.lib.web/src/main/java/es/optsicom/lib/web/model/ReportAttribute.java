package es.optsicom.lib.web.model;

public class ReportAttribute {
private final String name;
private final String value;

public ReportAttribute() {
	this.name = "";
	this.value = "";
}
public ReportAttribute(String name, String value) {
	this.name = (name == null)?"":name;
	this.value = (value == null)?"null":value;
}

public String getName() {
	return name;
}

public String getValue() {
	return value;
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
	if (!(obj instanceof ReportAttribute))
		return false;
	ReportAttribute other = (ReportAttribute) obj;
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
	return "ReportAttribute [name=" + name + ", value=" + value + "]";
}


}
