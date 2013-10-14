package es.optsicom.lib.expresults.manager;

import javax.persistence.EntityManager;


import es.optsicom.lib.expresults.db.DBManager;

public class ExperimentRepositoryManagerFactory {

	private DBManager dbManager;

	public ExperimentRepositoryManagerFactory(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	public ExperimentRepositoryManager createExperimentsManager() {
		EntityManager em = dbManager.createEntityManager();
		return new ExperimentRepositoryManager(em);
	}
}
