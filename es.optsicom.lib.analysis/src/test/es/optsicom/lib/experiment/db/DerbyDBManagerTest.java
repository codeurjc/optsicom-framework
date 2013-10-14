package es.optsicom.lib.experiment.db;

import java.io.File;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import es.optsicom.lib.expresults.db.DerbyDBManager;

public class DerbyDBManagerTest {

	public static void main(String[] args) throws SQLException {

		DerbyDBManager dbManager = new DerbyDBManager(new File("derby-test"));
		dbManager.connect();
		EntityManager em = dbManager.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().commit();
		dbManager.close();

	}

}
