package es.optsicom.lib.util.description;

import java.util.List;
import java.util.Map;

public interface Properties extends Comparable<Properties> {

	String getName();

	Map<String, String> getMap();

	String get(String key);

	void put(String key, Object value);

	List<Map.Entry<String, String>> getSortedProperties();

}
