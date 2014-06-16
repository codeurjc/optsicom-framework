package es.optsicom.lib.web.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ImportSampleData {

	private static final String DATABASE_NAME = "optsicom";

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		String aSQLScriptFilePath = "/import.sql";

		// Create Database Connection
		File derbyDatabaseDir = new File("derby_exp_repo");

		if (!derbyDatabaseDir.exists()) {
			boolean succesfull = derbyDatabaseDir.mkdirs();
			if (!succesfull) {
				throw new SQLException("Error creating database dir: "
						+ derbyDatabaseDir.getAbsolutePath());
			}
		}

		System.setProperty("derby.system.home",
				derbyDatabaseDir.getAbsolutePath());

		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

		boolean newDB = false;

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:" + DATABASE_NAME);
		} catch (SQLException e) {
			conn = DriverManager.getConnection("jdbc:derby:" + DATABASE_NAME
					+ ";create=true");
			System.out
					.println("There is no database. It is necessary to create it.");
			newDB = true;
		}

		Statement stmt = null;

		try {
			// Initialize object for ScripRunner
			ScriptRunner sr = new ScriptRunner(conn, false, false);

			// Give the input file to Reader
			Reader reader = new BufferedReader(new InputStreamReader(
					ImportSampleData.class
							.getResourceAsStream(aSQLScriptFilePath)));

			// Exctute script
			sr.runScript(reader);

		} catch (Exception e) {
			System.err.println("Failed to Execute " + aSQLScriptFilePath
					+ ". The error is " + e.getMessage()+" (Type "+e.getClass().getName()+")");
			e.printStackTrace();
		}

		conn.close();
	}

}
