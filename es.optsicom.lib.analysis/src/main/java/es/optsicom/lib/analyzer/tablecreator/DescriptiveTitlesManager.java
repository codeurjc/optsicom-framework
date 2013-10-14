package es.optsicom.lib.analyzer.tablecreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.MemoryProperties;

/**
 * Esta clase sirve para gestionar los nombres a la hora de generar las tablas.
 * Las AttributedTables permiten que los valores de los atributos sean Object,
 * pero trata de manera especial los valores "Descriptible". Esta clase permite
 * gestionar los alias, la repetición de name, etc.
 */
public class DescriptiveTitlesManager {

	public enum ColisionResolutionMode {
		NUMBERS, DIFERENT_PROPERTIES
	}

	private Map<String, List<Descriptive>> descriptivesByTitle = new HashMap<String, List<Descriptive>>();
	private Map<String, Descriptive> descriptivesByUniqueTitles = new HashMap<String, Descriptive>();
	private Map<Descriptive, String> uniqueTitlesByDescriptive = new HashMap<Descriptive, String>();
	private List<String> uniqueTitles = new ArrayList<String>();

	public DescriptiveTitlesManager(List<? extends Descriptive> descriptives) {
		this(null, descriptives, ColisionResolutionMode.NUMBERS);
	}

	public DescriptiveTitlesManager(List<String> names,
			List<? extends Descriptive> descriptives) {
		this(names, descriptives, ColisionResolutionMode.NUMBERS);
	}

	public DescriptiveTitlesManager(List<String> names,
			List<? extends Descriptive> descriptives,
			ColisionResolutionMode mode) {

		if (names != null && names.size() > 0) {

			for(int i = 0; i<names.size(); i++){
				
				Descriptive descriptive = descriptives.get(i);
				String name = names.get(i);
				
				if(name == null){
					name = descriptive.getProperties().getName();
				}
				
				List<Descriptive> descs = descriptivesByTitle.get(name);
				if (descs == null) {
					descs = new ArrayList<Descriptive>();
					descriptivesByTitle.put(name, descs);
				}
				descs.add(descriptive);
			}
			
		} else {

			for (Descriptive descriptive : descriptives) {
				List<Descriptive> descs = descriptivesByTitle.get(descriptive
						.getProperties().getName());
				if (descs == null) {
					descs = new ArrayList<Descriptive>();
					descriptivesByTitle.put(descriptive.getProperties()
							.getName(), descs);
				}
				descs.add(descriptive);
			}
		}

		for (Entry<String, List<Descriptive>> entry : descriptivesByTitle
				.entrySet()) {

			List<Descriptive> sameTitleDescriptives = entry.getValue();
			String title = entry.getKey();

			if (sameTitleDescriptives.size() == 1) {
				Descriptive descriptive = sameTitleDescriptives.get(0);

				addUniqueTitleAttribute(title, descriptive);
			} else {

				switch (mode) {
				case NUMBERS:
					processColisionNumbers(title, sameTitleDescriptives);
					break;
				case DIFERENT_PROPERTIES:
					processColisionPropertiesNaive(title, sameTitleDescriptives);
					// processColisionPropertiesElaborated(title,
					// sameTitleDescriptives);
					break;
				default:
					throw new Error();
				}
			}
		}

		for (Descriptive desc : descriptives) {
			this.uniqueTitles.add(this.uniqueTitlesByDescriptive.get(desc));
		}
	}

	private void processColisionPropertiesNaive(String title,
			List<Descriptive> sameTitleDescriptives) {

		// Podemos suponer que los objetos con colisión tienen las mismas
		// propiedades o no
		// Hay que contemplar propiedades simples y propiedades compuestas
		// Hay que determinar como se muestra el null frente a otros valores

		// Primero calculamos las propiedades comunes
		MemoryProperties commonProperties = new MemoryProperties(sameTitleDescriptives.get(
				0).getProperties());
		for (int i = 1; i < sameTitleDescriptives.size(); i++) {
			Descriptive desc = sameTitleDescriptives.get(i);
			Set<String> commonProps = new HashSet<String>();
			for (Entry<String, String> prop : desc.getProperties().getMap().entrySet()) {
				if (prop.getValue().equals(commonProperties.get(prop.getKey()))) {
					commonProps.add(prop.getKey());
				}
			}
			commonProperties.keySet().retainAll(commonProps);
		}

		// Luego quitamos esas propiedades de cada uno de ellos y generamos el
		// titulo
		for (Descriptive desc : sameTitleDescriptives) {
			MemoryProperties props = new MemoryProperties(desc.getProperties());
			props.keySet().removeAll(commonProperties.keySet());

			// Luego, por cada lista de propiedades distintas, tenemos que
			// quitar las subpropiedades. Es decir,
			// por ejemplo, si la clase de un objeto es diferente, ya no me
			// interesa saber en la tabla en que
			// se diferencian cada uno de las propiedades de los diferentes
			// objetos.
			// retainFirstLevel(props);

			// Este enfoque no funciona. Si la diferencia está en una
			// subpropiedad, se quitarán todas las subpropiedades
			// y se mostrará la propiedad de alto nivel, que para todos son
			// iguales. Lo q hay que ver es si hay
			// varios elementos que colisionan quitando los subniveles, quitar
			// el siguiente nivel, etc.
			// Pero eso es complicado porque requiere comparar los elementos
			// entre si.

			addUniqueTitleAttribute(title + " " + props, desc);
		}
	}

	private void retainFirstLevel(DBProperties props) {

		// Obtenemos los nombres de las propiedades
		List<String> propNames = new ArrayList<String>(props.keySet());

		// Ordenamos por el número de niveles de cada propiedad
		Collections.sort(propNames, new Comparator<String>() {
			public int compare(String propA, String propB) {
				StringTokenizer stA = new StringTokenizer(propA, ".");
				StringTokenizer stB = new StringTokenizer(propB, ".");
				return stA.countTokens() - stB.countTokens();
			}
		});

		// Por cada propiedad mirarmos si existe otra propiedad que sea
		// padre de ella (usando startsWith). Si es así, la borramos.
		List<String> removeProps = new ArrayList<String>();
		for (int i = 0; i < propNames.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (propNames.get(i).startsWith(propNames.get(j))) {
					removeProps.add(propNames.get(i));
				}
			}
		}

		props.keySet().removeAll(removeProps);
	}

	private void processColisionNumbers(String title,
			List<Descriptive> sameTitleDescriptives) {
		int numTitle = 1;
		for (Descriptive descriptive : sameTitleDescriptives) {
			String uniqueTitle = title + " (" + numTitle + ")";
			addUniqueTitleAttribute(uniqueTitle, descriptive);
			numTitle++;
		}
	}

	private void addUniqueTitleAttribute(String uniqueTitle,
			Descriptive descriptive) {
		descriptivesByUniqueTitles.put(uniqueTitle, descriptive);
		uniqueTitlesByDescriptive.put(descriptive, uniqueTitle);
	}

	public String getUniqueTitle(Descriptive descriptive) {
		return uniqueTitlesByDescriptive.get(descriptive);
	}

	public Descriptive getValue(String uniqueTitle) {
		return descriptivesByUniqueTitles.get(uniqueTitle);
	}

	public List<String> getUniqueTitles() {
		return uniqueTitles;
	}

}
