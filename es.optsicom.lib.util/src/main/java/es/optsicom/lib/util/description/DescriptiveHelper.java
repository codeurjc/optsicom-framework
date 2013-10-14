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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.Id;

public class DescriptiveHelper {

	private static final String CLASS_NAME = "class";

	public static Properties createProperties(Descriptive obj) {

		MemoryProperties properties = new MemoryProperties();

		putProperties(null, obj, properties);

		return properties;
	}

	private static void putProperties(String prefix, Descriptive obj,
			MemoryProperties properties) {

		properties.put(getPropName(prefix, CLASS_NAME), obj.getClass()
				.getName());

		for (Method method : obj.getClass().getMethods()) {
			if (method.isAnnotationPresent(Id.class)
					&& method.getParameterTypes().length == 0
					&& method.getReturnType() != void.class) {

				String methodName = method.getName();
				String propName;
				if (methodName.startsWith("is")) {
					propName = methodName.substring(2);
				} else if (methodName.startsWith("get")) {
					propName = methodName.substring(3);
				} else {
					continue;
				}
				propName = propName.toLowerCase();

				try {
					Object value = method.invoke(obj);

					propName = getPropName(prefix, propName);

					processValue(properties, propName, value);

				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}

		if (properties.get("name") == null) {
			properties.put("name", obj.getClass().getSimpleName());
		}
	}

	private static void processValue(MemoryProperties description,
			String propName, Object value) {

		if (value == null) {

			description.put(propName, "null");

		} else if (value instanceof MemoryProperties) {

			MemoryProperties p = (MemoryProperties) value;
			p = putPrefix(propName, p);
			description.putAll(p);

		} else if (value instanceof Descriptive) {

			Descriptive desc = (Descriptive) value;
			MemoryProperties p = new MemoryProperties(desc.getProperties());
			String propValueName = p.getName();
			p.remove("name");
			p = putPrefix(propName, p);
			description.putAll(p);
			description.put(propName, propValueName);

		} else if (value instanceof List) {

			List elements = (List) value;
			int counter = 0;
			for (Object elem : elements) {
				processValue(description, propName + "." + counter, elem);
				counter++;
			}

		} else if (value instanceof Enum) {

			description.put(propName, value.toString());

		} else if (value.getClass().isArray() || value instanceof Number || value instanceof Character || value instanceof String ||
				value instanceof Boolean) {

			description.put(propName, ArraysUtil.toStringObj(value));

		} else {

			description.put(propName + "." + CLASS_NAME, value.getClass()
					.getSimpleName());
			description.put(propName + ".toString", value.toString());

		}
	}

	private static MemoryProperties putPrefix(String prefix, MemoryProperties p) {
		MemoryProperties np = new MemoryProperties();
		for (Map.Entry<String, String> entry : p.entrySet()) {
			np.put(prefix + "." + entry.getKey(), entry.getValue());
		}
		return np;
	}

	private static String getPropName(String prefix, String propName) {
		if (prefix != null) {
			propName = prefix + "." + propName;
		}
		return propName;
	}

}
