package es.optsicom.lib.expresults.saver;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class ExperimentSaver {

	private DBManager dbManager;
	private EntityManager em;
	private Experiment experiment;

	public ExperimentSaver(DBManager dbManager) {
		this.dbManager = dbManager;
		this.em = dbManager.createEntityManager();
		em.getTransaction().begin();
	}

	public DBExecutionSaver startExecution(MethodDescription methodDesc,
			InstanceDescription instanceDesc, long timeLimit) {
		
		return new DBExecutionSaver(dbManager.createEntityManager(), this,
				instanceDesc, methodDesc, timeLimit);
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public boolean isInstanceExperimented(InstanceDescription instanceDesc) {
		return false;
	}

	public long getExperimentId() {
		return experiment.getId();
	}

	public void persistExperiment() {
		em.persist(experiment);
	}

	public void commitTx() {
		em.getTransaction().commit();
	}
	
	public void beginTx() {
		em.getTransaction().begin();		
	}

	public InstanceDescription findInstanceDescriptionByName(String name) {

		Query q = em.createQuery("select i from "
				+ InstanceDescription.class.getSimpleName()
				+ " i where i.name = :name");
		q.setParameter("name", name);

		return (InstanceDescription) q.getSingleResult();

	}

	public void persist(Object entity) {
		em.persist(entity);		
	}

	public InstanceDescription findInstanceDescription(String properties) {
		return findInstanceDescription(em,properties);
	}
	
	public MethodDescription findMethodDescription(String properties) {
		return findMethodDescription(em,properties);
	}
	
	public InstanceDescription findInstanceDescription(EntityManager entityManager,
			String properties) {
		return findElementDescription(entityManager, InstanceDescription.class, properties);
	}
	
	public MethodDescription findMethodDescription(EntityManager entityManager,
			String properties) {
		return findElementDescription(entityManager, MethodDescription.class, properties);
	}
	
	protected <E extends ElementDescription> E findElementDescription(EntityManager entityManager, 
			Class<E> clazz, String propsAsString) {
		Query q = entityManager.createQuery("select m from " + clazz.getSimpleName()
				+ " m where m.properties.propsAsString = :propsAsString");
		q.setParameter("propsAsString", propsAsString);
		return (E) q.getSingleResult();
	}

	

}
