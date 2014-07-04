package es.optsicom.lib.expresults.util;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class PropertiesManager {

	private static final String PROPERTIES_DIR = "resources";
	private static final String PROPERTIES_FILE = "properties";

	private static PropertiesManager instance;

	private static Properties properties;

	private PropertiesManager() {
	}

	public static PropertiesManager getInstance() {
		if (instance == null) {
			instance = new PropertiesManager();
			loadProperties();
		}
		return instance;
	}

	public String getProperty(String key) {
		String property = System.getProperty(key);

		if (property == null) {
			property = properties.getProperty(key);
			System.out.println("PropertiesManager.file.get." + key + "=" + property);
		} else {
			System.out.println("PropertiesManager.system.get." + key + "=" + property);
		}

		return property;
	}

	private static void loadProperties() {
		properties = new Properties();
		try {

			// optsicom properties.
			File userOptsicomFile = new File(PROPERTIES_DIR, PROPERTIES_FILE);
			if (userOptsicomFile.exists()) {
				properties.load(new FileReader(new File(PROPERTIES_DIR, PROPERTIES_FILE)));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Couldn't load properties from file and no parameters specified");
		}
	}

}
