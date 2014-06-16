/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.util.description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.Strings;

/**
 * Todos los objetos Properties deberían tener una propiedad llamada "name", que
 * sería el nombre corto por defecto para identificar a ese elemento. En la medida
 * de los posible ese nombre debería utilizarse para identificar el elemento
 * univocamente entre los demás elementos de su tipo, pero también debería ser
 * relativamente compacto. El problema está en que dado un elemento puede no saberse
 * a priori el contexto en el que se va a encontrar. Por ejemplo, un método puede tener
 * un nombre pero luego aparecer en un experimento con varios valores de un determinado
 * parámetro. En otros casos, como las instancias, si es sencillo generar un nombre único
 * que identifique a la instancia en cualquier contexto y sea compacto. 
 * En conclusión, el nombre es el identificador del elemento, pero en casos donde es
 * razonable tener una colisión por otra propiedad, el que hace uso de las
 * properties debería mostrar aquellas propiedades que hacen a estos dos valores
 * diferentes. 
 * 
 * @author Administrador
 */
public class MemoryProperties extends HashMap<String, String> implements Properties {

	private static final long serialVersionUID = 2997971963616552407L;
	
	private static final String NAME = "name";	

	public MemoryProperties(Properties properties) {
		this(properties.getMap());
	}
	
	public MemoryProperties(Map<String, String> properties) {
		super(properties);
	}

	public MemoryProperties() {
	}

	public MemoryProperties(String name) {
		setName(name);
	}

	public String getName() {
		return get(NAME);
	}
	
	public void setName(String name){
		put(NAME,name);
	}

	@Override
	public String toString() {
		//TODO Hay que buscar un mecanismo para hacer esto más eficiente. Hacer un "dirty" de las properties o algo así
		//if (cachedToString == null) {
			StringBuilder sb = new StringBuilder("{");
			if (this.keySet().size() > 0) {
				for (String key : new TreeSet<String>(this.keySet())) {
					sb.append(key + "=" + this.get(key) + ", ");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("}");
			return sb.toString();
		//}
		//return cachedToString;
	}

	@Override
	public int compareTo(Properties properties) {
		return Strings.compareNatural(this.toString(),properties.toString());
	}

	public Map<String, String> createStringMapWithoutNulls() {
		Map<String,String> stringMap = new HashMap<String,String>();
		for(Map.Entry<String,String> entry : this.entrySet()) {
			Object value = entry.getValue();
			String valueAsString = null;
			if(value != null){
				valueAsString = value.toString();
			} else {
				//This is a hack. Aparently, EclipseLink doesn't allow null values in maps.
				valueAsString = "";
			}
			stringMap.put(entry.getKey(), valueAsString);
			
		}
		return stringMap;
	}

	@Override
	public Map<String, String> getMap() {
		return this;
	}

	@Override
	public String get(String key) {
		return super.get(key);
	}

	@Override
	public void put(String key, Object value) {
		this.put(key,ArraysUtil.toStringObj(value));		
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
