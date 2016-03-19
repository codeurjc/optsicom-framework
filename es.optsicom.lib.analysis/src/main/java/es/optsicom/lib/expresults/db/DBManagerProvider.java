package es.optsicom.lib.expresults.db;

import java.io.File;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.optsicom.lib.expresults.util.PropertiesManager;

public class DBManagerProvider {

	private static final Logger log = LoggerFactory.getLogger(DBManagerProvider.class);

	private static final String MYSQL = "mysql";
	private static final String DERBY = "derby";

	/**
	 * Loads a DBManager defined by properties file in
	 * src/main/resources/properties if there is no properties file, it loads a
	 * default DbManager with Derby with default config.
	 * 
	 * @return DBManager
	 * @throws SQLException
	 */
	public static DBManager getDBManager() throws SQLException {
		String db = PropertiesManager.getInstance().getProperty("db");

		if (db != null) {
			String type = PropertiesManager.getInstance().getProperty(db);

			if (MYSQL.equals(type)) {
				log.info("DBManager.MYSQL." + type);

				return new MySQLDBManager(db);
			} else if (DERBY.equals(type)) {
				log.info("DBManager.DERBY." + type);
				return new DerbyDBManager(db);
			}
		}

		// default DBManager
		log.info("DBManager.Default");
		return new DerbyDBManager(new File("derby_exp_repo"));
	}

}
