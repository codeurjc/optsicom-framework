package es.optsicom.lib.expresults.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.Strings;
import es.optsicom.lib.util.description.Properties;

@Entity
public class DBProperties implements Properties {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	private static final String NAME = "name";

	@ElementCollection(fetch = FetchType.LAZY)
	private Map<String, String> props;

	@Column(length = 32672)
	protected String propsAsString;

	private DBProperties() {
		// JPA Needed constructor
	}

	@JsonCreator
	public DBProperties(@JsonProperty("map") Map<String, String> props) {
		this.props = props;
		createPropsAsString();
	}

	public DBProperties(DBProperties properties) {
		this.props = new HashMap<String, String>(properties.props);
		createPropsAsString();
	}

	public DBProperties(String name) {
		this.props = new HashMap<String, String>();
		setName(name);
		createPropsAsString();
	}

	@JsonIgnore
	public String getName() {
		return (String) props.get(NAME);
	}

	public void setName(String name) {
		props.put(NAME, name);
	}

	@JsonIgnore
	public String getPropsAString() {
		return propsAsString;
	}

	private void createPropsAsString() {

		StringBuilder sb = new StringBuilder("{");
		Set<String> keySet = this.props.keySet();
		if (keySet.size() > 0) {
			for (String key : new TreeSet<String>(keySet)) {
				sb.append(key + "=" + this.props.get(key) + ", ");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		this.propsAsString = sb.toString();
	}

	@Override
	public String toString() {
		return this.propsAsString;
	}

	@Override
	public int compareTo(Properties properties) {
		return Strings.compareNatural(this.toString(), properties.toString());
	}

	public Set<String> keySet() {
		return props.keySet();
	}

	public String get(String key) {
		return props.get(key);
	}

	public Map<String, String> getMap() {
		return props;
	}

	@Override
	public void put(String key, Object value) {
		props.put(key, ArraysUtil.toStringObj(value));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propsAsString == null) ? 0 : propsAsString.hashCode());
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
		DBProperties other = (DBProperties) obj;
		if (propsAsString == null) {
			if (other.propsAsString != null)
				return false;
		} else if (!propsAsString.equals(other.propsAsString))
			return false;
		return true;
	}

	@Override
	public List<Entry<String, String>> getSortedProperties() {

		List<Entry<String, String>> props = new ArrayList<Map.Entry<String, String>>(
				getMap().entrySet());
		Collections.sort(props, new Comparator<Entry<String, String>>() {

			@Override
			public int compare(Entry<String, String> o1,
					Entry<String, String> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		return props;
	}

}
