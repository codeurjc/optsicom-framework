package es.optsicom.lib.expresults.saver;

import es.optsicom.lib.expresults.db.DBManager;

public class ExperimentRepositorySaver {

	private DBManager dbManager;

	public ExperimentRepositorySaver(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	public ExperimentSaver createExperimentSaver() {
		return new DBManagerExperimentSaver(dbManager);
	}

}
