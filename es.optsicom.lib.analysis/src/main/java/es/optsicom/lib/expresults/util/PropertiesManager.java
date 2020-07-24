package es.optsicom.lib.expresults.util;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesManager {

	private static final Logger log = LoggerFactory.getLogger(PropertiesManager.class);

	private static final String OPTSICOM_PROPERTIES_LOCATION_PROPERTY = "optsicom.properties.location";

	private static final String PROPERTIES_FILE = "optsicom.properties";

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
		String keyEnvVariable = key.toUpperCase().replace(".", "_");
		String property = System.getenv(keyEnvVariable);

		if (property == null) {
			property = System.getProperty(key);

			if (property == null) {
				property = properties.getProperty(key);
				
				System.out.println("PropertiesManager.file.get." + key + "=" + property);
			} else {
				System.out.println("PropertiesManager.system.get." + key + "=" + property);
			}
		} else {
			System.out.println("PropertiesManager.env.get." + keyEnvVariable + "=" + property);
		}

		return property;
	}

	private static void loadProperties() {
		properties = new Properties();

		Path propertiesFile = null;
		try {

			String location = System.getProperty(OPTSICOM_PROPERTIES_LOCATION_PROPERTY);

			if (location != null) {
				propertiesFile = Paths.get(location);

				if (Files.isDirectory(propertiesFile)) {
					propertiesFile = propertiesFile.resolve(PROPERTIES_FILE);
				}

				if (!Files.exists(propertiesFile)) {
					log.warn("Optsicom properties not found in path '" + propertiesFile + "'. Searching in workdir: '"
							+ Paths.get("").toAbsolutePath() + "'");
				}
			}

			if (propertiesFile == null) {
				propertiesFile = Paths.get(PROPERTIES_FILE);
				if (!Files.exists(propertiesFile)) {
					propertiesFile = Paths.get(System.getProperty("user.home"), ".optsicom", PROPERTIES_FILE);
				}
			}

			if (Files.exists(propertiesFile)) {
				properties.load(Files.newBufferedReader(propertiesFile, Charset.forName("utf-8")));
			} else {
				log.debug("File optsicom.properties not found. Resorting to default values");
			}

		} catch (Exception e) {
			throw new Error("Couldn't load properties from file '" + propertiesFile + "'", e);
		}
	}
}
