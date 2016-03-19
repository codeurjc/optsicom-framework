package es.optsicom.lib.expresults.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class DerbyDBManager extends DBManager {

	private static final Logger log = Logger.getLogger(DerbyDBManager.class.getName());

	private static final String DB_WORKSPACE_DEFAULT = "derby_exp_repo";
	public static final String DB_DATA_DIR_DEFAULT = "derby_data";
	private static final String DB_SCHEMA_DEFAULT = "optsicom";

	private static final String DB_WORKSPACE = "workspace";
	private static final String DB_DATA_DIR = "dataDir";
	private static final String DB_SCHEMA = "schema";

	private enum DbRegenerationMode {
		// do not generate DDL; no schema is generated.

		NONE,
		// create DDL for all tables; drop all existing tables
		DROP_AND_CREATE_TABLES,
		// create DDL for non-existent tables; leave existing tables unchanged
		CREATE_TABLES
	}

	private final File dbDir;

	private final String dataDir;

	private final String schema;

	public DerbyDBManager(String db) throws SQLException {
		PropertiesManager pm = PropertiesManager.getInstance();
		db += ".";

		this.dbDir = new File(
				pm.getProperty(db + DB_WORKSPACE) != null ? pm.getProperty(db + DB_WORKSPACE) : DB_WORKSPACE_DEFAULT);

		this.dataDir = pm.getProperty(db + DB_DATA_DIR) != null ? pm.getProperty(db + DB_DATA_DIR)
				: DB_DATA_DIR_DEFAULT;

		this.schema = pm.getProperty(db + DB_SCHEMA) != null ? pm.getProperty(db + DB_SCHEMA) : DB_SCHEMA_DEFAULT;

		log.info(dbDir + ":" + dataDir + ":" + schema);

		connect();
	}

	public DerbyDBManager(File dbDir) throws SQLException {
		this.dbDir = dbDir;
		this.dataDir = DB_DATA_DIR_DEFAULT;
		this.schema = DB_SCHEMA_DEFAULT;
		connect();
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

		File derbyDatabaseDir = new File(dbDir, dataDir);

		log.info("Connecting to database in dir \"" + derbyDatabaseDir.getAbsolutePath() + "\"");

		if (!derbyDatabaseDir.exists()) {
			boolean succesfull = derbyDatabaseDir.mkdirs();
			if (!succesfull) {
				throw new SQLException("Error creating database dir: " + derbyDatabaseDir.getAbsolutePath());
			}
		}

		System.setProperty("derby.system.home", derbyDatabaseDir.getAbsolutePath());

		loadDriver();
		log.info("Driver loaded");

		boolean newDB = false;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:" + schema);
		} catch (SQLException e) {
			conn = DriverManager.getConnection("jdbc:derby:" + schema + ";create=true");
			log.info("There is no database. It is necessary to create it.");
			newDB = true;
		}

		conn.close();

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("eclipselink.jdbc.url", "jdbc:derby:" + schema);
		properties.put("eclipselink.jdbc.driver", "org.apache.derby.jdbc.EmbeddedDriver");
		properties.put("eclipselink.logging.level", "WARNING");
		properties.put("eclipselink.target-database", "Derby");

		// properties.put("javax.persistence.jdbc.url",
		// "jdbc:derby:"+DATABASE_NAME);
		// properties.put("javax.persistence.jdbc.driver",
		// "org.apache.derby.jdbc.EmbeddedDriver");
		// properties.put("hibernate.dialect",
		// "org.hibernate.dialect.DerbyDialect");

		DbRegenerationMode dbRegenerationModeEnum;
		if (newDB) {
			dbRegenerationModeEnum = DbRegenerationMode.CREATE_TABLES;
		} else {
			dbRegenerationModeEnum = DbRegenerationMode.NONE;
		}

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

		properties.put("eclipselink.ddl-generation", dbRegenerationMode);

		// properties.put("toplink.ddl-generation.output-mode", "database");

		// This property creates dll file in disk
		properties.put("eclipselink.ddl-generation.output-mode", "both");

		entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("optsicom", properties);

		log.info("Connected to database");
	}

	/**
	 * Loads the appropriate JDBC driver for this environment/framework. For
	 * example, if we are in an embedded environment, we load Derby's embedded
	 * Driver, <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
	 */
	private void loadDriver() {

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		} catch (ClassNotFoundException cnfe) {
			System.err.println("\nUnable to load the JDBC driver");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err.println("\nUnable to instantiate the JDBC driver");
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err.println("\nNot allowed to access the JDBC driver");
			iae.printStackTrace(System.err);
		}
	}

	@Override
	public void close() throws SQLException {

		super.close();

		try {
			// the shutdown=true attribute shuts down Derby
			DriverManager.getConnection("jdbc:derby:;shutdown=true");

			// To shut down a specific database only, but keep the
			// engine running (for example for connecting to other
			// databases), specify a database in the connection URL:
			// DriverManager.getConnection("jdbc:derby:" + dbName +
			// ";shutdown=true");
		} catch (SQLException se) {
			if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {
				// we got the expected exception
				// Note that for single database shutdown, the expected
				// SQL state is "08006", and the error code is 45000.
			} else {
				// if the error code or SQLState is different, we have
				// an unexpected exception (shutdown failed)
				throw new SQLException("The database is not closed succesfully", se);
			}
		}
	}

	@Override
	public EntityManager createEntityManager() {
		if (!entityManagerFactory.isOpen()) {
			try {
				this.connect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entityManagerFactory.createEntityManager();
	}
}
