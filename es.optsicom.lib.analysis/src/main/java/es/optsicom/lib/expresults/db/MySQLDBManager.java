package es.optsicom.lib.expresults.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import es.optsicom.lib.expresults.util.PropertiesManager;

/**
 * 
 * @author mica
 */
public class MySQLDBManager extends DBManager {

	private static final Logger log = Logger.getLogger(MySQLDBManager.class.getName());

	private static final String DB_SCHEMA_DEFAULT_NAME = "optsicom2";
	private static final String OPTSICOM_DB = "optsicom.db";

	private static final String DB_HOST = "host";
	private static final String DB_PORT = "port";
	private static final String DB_USER = "user";
	private static final String DB_PASSWORD = "password";
	private static final String DB_SCHEMA = "schema";

	private final String host;
	private final int port;
	private final String user;
	private final String password;

	private final String database;

	private enum DbRegenerationMode {
		// do not generate DDL; no schema is generated.
		NONE,
		// create DDL for all tables; drop all existing tables
		DROP_AND_CREATE_TABLES,
		// create DDL for non-existent tables; leave existing tables unchanged
		CREATE_TABLES
	}

	public MySQLDBManager() throws SQLException {
		this(false);
	}

	public MySQLDBManager(boolean create) throws SQLException {

		this(OPTSICOM_DB, create);
	}

	public MySQLDBManager(String host, int port, String user, String pass, String database) throws SQLException {
		this(host, port, user, pass, database, false);
	}

	public MySQLDBManager(String host, int port, String user, String password, String database, boolean create)
			throws SQLException {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;

		if (create) {
			createDatabase();
		}

		connect();
	}

	public MySQLDBManager(String db, boolean create) throws SQLException {
		PropertiesManager pm = PropertiesManager.getInstance();

		db += ".";
		this.host = pm.getProperty(db + DB_HOST);
		this.port = Integer.parseInt(pm.getProperty(db + DB_PORT));
		this.user = pm.getProperty(db + DB_USER);
		this.password = pm.getProperty(db + DB_PASSWORD);
		this.database = (pm.getProperty(db + DB_SCHEMA) != null) ? pm.getProperty(db + DB_SCHEMA)
				: DB_SCHEMA_DEFAULT_NAME;

		log.info(host + ":" + port + ":" + user + ":" + password + ":" + database);

		// TODO: fix this, createDatabase do not work
		// if (create) {
		// createDatabase();
		// }

		connect();
	}

	public MySQLDBManager(String db) throws SQLException {
		this(db, true);
	}

	private void createDatabase() {

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("eclipselink.jdbc.url", "jdbc:mysql://" + host + ":" + port + "/" + database);
		properties.put("eclipselink.jdbc.driver", "com.mysql.jdbc.Driver");
		properties.put("eclipselink.logging.level", "WARNING");
		properties.put("eclipselink.target-database", "MYSQL");
		properties.put("eclipselink.jdbc.user", user);
		properties.put("eclipselink.jdbc.password", password);

		// DbRegenerationMode dbRegenerationModeEnum =
		// DbRegenerationMode.DROP_AND_CREATE_TABLES;
		DbRegenerationMode dbRegenerationModeEnum = DbRegenerationMode.CREATE_TABLES;

		String dbRegenerationMode = getRegenerationModeString(dbRegenerationModeEnum);

		properties.put("eclipselink.ddl-generation", dbRegenerationMode);

		// properties.put("toplink.ddl-generation.output-mode", "database");

		// This property creates dll file in disk
		properties.put("eclipselink.ddl-generation.output-mode", "both");

		entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("optsicom", properties);

		// try {
		// entityManagerFactory.createEntityManager().createQuery("SELECT e FROM
		// EXPERIMENT e");
		// } catch(IllegalArgumentException e) {
		// log.info("Database doesn't exist. Creating it.");
		//
		// entityManagerFactory.close();
		//
		// // Try to create the tables
		// dbRegenerationModeEnum = DbRegenerationMode.CREATE_TABLES;
		//
		// dbRegenerationMode =
		// getRegenerationModeString(dbRegenerationModeEnum);
		//
		// properties.put("eclipselink.ddl-generation", dbRegenerationMode);
		//
		// //properties.put("toplink.ddl-generation.output-mode", "database");
		//
		// entityManagerFactory = javax.persistence.Persistence
		// .createEntityManagerFactory("optsicom", properties);
		// }

		log.info("Created database: " + entityManagerFactory.getProperties().get("eclipselink.jdbc.url"));

		entityManagerFactory.close();
	}

	/**
	 * JPA LogLevel. Values:<br/>
	 * <ul>
	 * <li>OFF: This setting disables the generation of the log output. You may
	 * want to set logging to OFF during production to avoid the overhead of
	 * logging.</li>
	 * <li>SEVERE: This level enables reporting of failure cases only. Usually,
	 * if the failure occurs, the application stops.</li>
	 * <li>WARNING: This level enables logging of issues that have a potential
	 * to cause problems. For example, a setting that is picked by the
	 * application and not by the user.</li>
	 * <li>INFO This level enables the standard output. The contents of this
	 * output is very limited.</li>
	 * <li>CONFIG: This level enables logging of such configuration details as
	 * your database login information and some metadata information. You may
	 * want to use the CONFIG log level at deployment time.</li>
	 * <li>FINE: This level enables logging of the first level of the debugging
	 * information and SQL. You may want to use this log level during debugging
	 * and testing, but not at production.</li>
	 * <li>FINER: This level enables logging of more debugging information than
	 * the FINE setting. For example, the transaction information is logged at
	 * this level. You may want to use this log level during debugging and
	 * testing, but not at production.</li>
	 * <li>FINEST: This level enables logging of more debugging information than
	 * the FINER setting, such as a very detailed information about certain
	 * features (for example, sequencing). You may want to use this log level
	 * during debugging and testing, but not at production.</li>
	 * </ul>
	 * 
	 * @throws SQLException
	 */

	@Override
	protected void connect() throws SQLException {

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("eclipselink.jdbc.url", "jdbc:mysql://" + host + ":" + port + "/" + database);
		properties.put("eclipselink.jdbc.driver", "com.mysql.jdbc.Driver");
		properties.put("eclipselink.logging.level", "WARNING");
		properties.put("eclipselink.target-database", "MYSQL");
		properties.put("eclipselink.jdbc.user", user);
		properties.put("eclipselink.jdbc.password", password);

		// DbRegenerationMode dbRegenerationModeEnum =
		// DbRegenerationMode.DROP_AND_CREATE_TABLES;
		DbRegenerationMode dbRegenerationModeEnum = DbRegenerationMode.CREATE_TABLES;

		String dbRegenerationMode = getRegenerationModeString(dbRegenerationModeEnum);

		properties.put("eclipselink.ddl-generation", dbRegenerationMode);

		// properties.put("toplink.ddl-generation.output-mode", "database");

		// This property creates dll file in disk
		properties.put("eclipselink.ddl-generation.output-mode", "both");

		entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("optsicom", properties);

		// try {
		// entityManagerFactory.createEntityManager().createQuery("SELECT e FROM
		// EXPERIMENT e");
		// } catch(IllegalArgumentException e) {
		// log.info("Database doesn't exist. Creating it.");
		//
		// entityManagerFactory.close();
		//
		// // Try to create the tables
		// dbRegenerationModeEnum = DbRegenerationMode.CREATE_TABLES;
		//
		// dbRegenerationMode =
		// getRegenerationModeString(dbRegenerationModeEnum);
		//
		// properties.put("eclipselink.ddl-generation", dbRegenerationMode);
		//
		// //properties.put("toplink.ddl-generation.output-mode", "database");
		//
		// entityManagerFactory = javax.persistence.Persistence
		// .createEntityManagerFactory("optsicom", properties);
		// }

		log.info("Connected to database: " + entityManagerFactory.getProperties().get("eclipselink.jdbc.url"));
	}

	private String getRegenerationModeString(DbRegenerationMode dbRegenerationModeEnum) {

		String dbRegenerationMode = null;
		switch (dbRegenerationModeEnum) {
		case NONE:
			dbRegenerationMode = "none";
			break;
		case CREATE_TABLES:
			dbRegenerationMode = "create-tables";
			break;
		case DROP_AND_CREATE_TABLES:
			dbRegenerationMode = "drop-and-create-tables";
			break;
		}

		return dbRegenerationMode;
	}

	@Override
	public EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
}
