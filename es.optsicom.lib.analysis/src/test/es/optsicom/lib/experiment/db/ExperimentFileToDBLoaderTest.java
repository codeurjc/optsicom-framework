package es.optsicom.lib.experiment.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import es.optsicom.lib.expresults.db.DerbyDBManager;
import es.optsicom.lib.expresults.db.ExperimentFileToDBLoader;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManagerFactory;
import es.optsicom.lib.expresults.manager.ExperimentRepositoryManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.Researcher;

public class ExperimentFileToDBLoaderTest {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException, SQLException {

		DerbyDBManager dbManager = new DerbyDBManager(new File("derby-test"));
				
		ExperimentRepositoryManagerFactory daoFactory = new ExperimentRepositoryManagerFactory(dbManager);

		ExperimentRepositoryManager dao = daoFactory.createExperimentsManager();

		Researcher r = new Researcher("Mica");
		dao.persist(r);

		ComputerDescription c = new ComputerDescription("Neuron1");
		dao.persist(c);

		ExperimentFileToDBLoader loader = new ExperimentFileToDBLoader();
		loader.load(dao, new File("Experiments"), r, new Date(), c);

		dao.commitTx();

		dao.close();
		dbManager.close();

	}

}
