package es.optsicom.lib.expresults.db;

import java.io.File;
import java.sql.SQLException;

import es.optsicom.lib.expresults.util.PropertiesManager;

public class DBManagerProvider {

	private static final String MYSQL = "mysql";
	private static final String DERBY = "derby";

	/**
	 * Loads a DBManager defined by properties file in src/main/resources/properties if there is no properties file, it
	 * loads a default DbManager with Derby with default config.
	 * 
	 * @return DBManager
	 * @throws SQLException
	 */
	public static DBManager getDBManager() throws SQLException {
		String db = PropertiesManager.getInstance().getProperty("db");

		if (db != null) {
			String type = PropertiesManager.getInstance().getProperty(db);

			if (MYSQL.equals(type)) {
				System.out.println("DBManager.MYSQL." + type);
				return new MySQLDBManager(db);
			} else if (DERBY.equals(type)) {
				System.out.println("DBManager.DERBY." + type);
				return new DerbyDBManager(db);
			}
		}

		// default DBManager
		System.out.println("DBManager.Default");
		return new DerbyDBManager(new File("derby_exp_repo"));
	}

}
