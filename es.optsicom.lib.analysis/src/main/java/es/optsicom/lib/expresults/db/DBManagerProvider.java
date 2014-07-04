package es.optsicom.lib.expresults.db;

import java.sql.SQLException;

import es.optsicom.lib.expresults.util.PropertiesManager;

public class DBManagerProvider {

	private static final String MYSQL = "mysql";
	private static final String DERBY = "derby";

	public DBManager getDBManager() throws SQLException {
		String db = PropertiesManager.getInstance().getProperty("db");

		db = db == null ? db : "";

		if (db != null) {
			String type = PropertiesManager.getInstance().getProperty(db);
			if (MYSQL.equals(type)) {
				return new MySQLDBManager(db);
			}
		}

		// default DBManager
		return new DerbyDBManager(db);
	}

}
