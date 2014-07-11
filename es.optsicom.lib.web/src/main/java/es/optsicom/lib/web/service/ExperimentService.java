package es.optsicom.lib.web.service;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import es.optsicom.lib.expresults.DBExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.ExperimentRepositoryFactory;
import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.db.DBManagerProvider;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.model.Experiment;

@Component
public class ExperimentService {

	private static final String DB_DIR = "derby_exp_repo";
	// private static final String DB_DIR = "/home/mica/Data/Universidad/Investigación/"
	// + "TesisAnthanhRuben/git/optsicom-diversity/es.optsicom.problem.edp/derby_exp_repo";

	ExperimentRepositoryManager expRepoManager;

	@PostConstruct
	public void init() throws SQLException {

		DBManager dbManager = DBManagerProvider.getDBManager();

		ExperimentRepositoryFactory expRepoFactory = null;
		expRepoFactory = new DBExperimentRepositoryManagerFactory(dbManager);

		expRepoManager = expRepoFactory.createExperimentRepositoryManager();

	}

	public ExperimentManager findExperimentManagerById(long id) {
		return expRepoManager.findExperimentManagerById(id);
	}

	public List<Experiment> findExperiments() {
		return expRepoManager.findExperiments();
	}

}