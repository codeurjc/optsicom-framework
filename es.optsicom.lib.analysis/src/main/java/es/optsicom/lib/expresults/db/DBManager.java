package es.optsicom.lib.expresults.db;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class DBManager {

	protected EntityManagerFactory entityManagerFactory;
	
	public abstract EntityManager createEntityManager();

	public void close() throws SQLException {
		entityManagerFactory.close();
	}

	protected abstract void connect() throws SQLException;

}
