package es.optsicom.lib.expresults;

import javax.persistence.EntityManager;

import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.saver.ExperimentRepositorySaver;

public class DBExperimentRepositoryManagerFactory extends ExperimentRepositoryFactory {

	private DBManager dbManager;

	public DBExperimentRepositoryManagerFactory(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	@Override
	public ExperimentRepositoryManager createExperimentRepositoryManager() {
		EntityManager em = dbManager.createEntityManager();
		return new ExperimentRepositoryManager(em);
	}

	@Override
	public ExperimentRepositorySaver createExperimentRepositorySaver() {
		return new ExperimentRepositorySaver(dbManager);
	}

}
