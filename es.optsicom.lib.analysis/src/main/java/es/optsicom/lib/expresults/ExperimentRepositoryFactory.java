package es.optsicom.lib.expresults;

import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.saver.ExperimentRepositorySaver;

public abstract class ExperimentRepositoryFactory {

	public abstract ExperimentRepositoryManager createExperimentRepositoryManager();
	
	public abstract ExperimentRepositorySaver createExperimentRepositorySaver();
	
}
