package es.optsicom.lib.expresults.manager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.Researcher;

public class ExperimentRepositoryManager {

	private EntityManager em;

	public ExperimentRepositoryManager(EntityManager em) {
		this.em = em;
	}

	public void beginTx() {
		this.em.getTransaction().begin();
	}

	public void commitTx() {
		this.em.getTransaction().commit();
	}

	public void rollbackTx() {
		this.em.getTransaction().rollback();
	}

	public boolean isActiveTx() {
		return this.em.getTransaction().isActive();
	}

	public void close() {
		em.close();
	}

	public void persist(Object entity) {
		em.persist(entity);
	}

	public InstanceDescription findInstanceDescription(String propsAsString) {
		return findElementDescription(InstanceDescription.class, propsAsString);
	}

	public MethodDescription findMethodDescription(String propsAsString) {
		return findElementDescription(MethodDescription.class, propsAsString);
	}

	protected <E extends ElementDescription> E findElementDescription(
			Class<E> clazz, String propsAsString) {
		Query q = em.createQuery("select m from " + clazz.getSimpleName()
				+ " m where m.properties.propsAsString = :propsAsString");
		q.setParameter("propsAsString", propsAsString);
		return (E) q.getSingleResult();
	}

	public InstanceDescription findInstanceDescriptionByName(String nameProperty) {
		return findElementDescriptionByName(InstanceDescription.class,
				nameProperty);
	}

	public MethodDescription findMethodDescriptionByName(String nameProperty) {
		return findElementDescriptionByName(MethodDescription.class,
				nameProperty);
	}

	protected <E extends ElementDescription> E findElementDescriptionByName(
			Class<E> clazz, String nameProperty) {

		Query q = em
				.createQuery("select m from "
						+ clazz.getSimpleName()
						+ " m"
						+ " join m.properties.props e where KEY(e) = 'name' and VALUE(e) = :nameProperty");
		q.setParameter("nameProperty", nameProperty);
		return (E) q.getSingleResult();
	}

	public Experiment reloadExperiment(Experiment experiment) {
		return em.find(Experiment.class, experiment.getId());
	}

	public Experiment findExperimentByName(String name, String problemName) {
		Query q = em.createQuery("select e from "
				+ Experiment.class.getSimpleName()
				+ " e where e.name = :name and e.problemName = :problem");
		q.setParameter("name", name);
		q.setParameter("problem", problemName);

		return (Experiment) q.getSingleResult();
	}

	public ExperimentManager findExperimentManagerByName(String name,
			String problemName) {
		Query q = em.createQuery("select e from "
				+ Experiment.class.getSimpleName()
				+ " e where e.name = :name and e.problemName = :problem");
		q.setParameter("name", name);
		q.setParameter("problem", problemName);

		try {

			return new LoadAllExperimentManager(
					(Experiment) q.getSingleResult(), this);

		} catch (NoResultException e) {
			throw new RuntimeException("Method \"" + name
					+ "\" not found for problem \"" + problemName + "\"", e);
		}
	}

	public ExperimentManager findExperimentManagerById(long experimentId) {

		Query q = em.createQuery("select e from "
				+ Experiment.class.getSimpleName()
				+ " e where e.id = :experimentId");
		q.setParameter("experimentId", experimentId);

		Experiment experiment = (Experiment) q.getSingleResult();

		LoadAllExperimentManager manager = new LoadAllExperimentManager(
				experiment, this);

		return manager;
	}

	public List<Execution> findExecutions(Experiment experiment,
			InstanceDescription instance, MethodDescription method) {

		Query q = em
				.createQuery("select e from "
						+ Execution.class.getSimpleName()
						+ " e where e.method = :method and e.instance = :instance and e.experiment = :experiment");
		q.setParameter("method", method);
		q.setParameter("instance", instance);
		q.setParameter("experiment", experiment);

		return (List<Execution>) q.getResultList();

	}

	public int countEvents(Execution execution, String eventName) {

		Query q = em.createQuery("select count(e) from "
				+ Event.class.getSimpleName()
				+ " e where e.execution = :execution and e.name = :name");
		q.setParameter("execution", execution);
		q.setParameter("name", eventName);

		return (Integer) q.getSingleResult();

	}

	public List<Event> getEvents(Execution execution, String eventName) {
		Query q = em.createQuery("select e from " + Event.class.getSimpleName()
				+ " e where e.execution = :execution and e.name = :name");
		q.setParameter("execution", execution);
		q.setParameter("name", eventName);

		return (List<Event>) q.getResultList();
	}

	public Event getLastEvent(Execution execution, String eventName) {
		Query q = em
				.createQuery("select e from "
						+ Event.class.getSimpleName()
						+ " e where e.execution = :execution and e.name = :name ORDER BY e.timestamp DESC");
		q.setParameter("execution", execution);
		q.setParameter("name", eventName);
		q.setMaxResults(1);
		List<Event> events = (List<Event>) q.getResultList();
		if (events.size() == 0) {
			return null;
		} else {
			return events.get(0);
		}
	}

	public Event getLastEvent(Execution execution, String eventName,
			long timelimit) {

		if (timelimit == -1) {
			return getLastEvent(execution, eventName);
		} else {
			Query q = em
					.createQuery("select e from "
							+ Event.class.getSimpleName()
							+ " e where e.execution = :execution and e.name = :name and e.timestamp <= :timelimit ORDER BY e.timestamp DESC");
			q.setParameter("execution", execution);
			q.setParameter("name", eventName);
			q.setParameter("timelimit", timelimit);
			q.setMaxResults(1);
			List<Event> events = (List<Event>) q.getResultList();
			if (events.size() == 0) {
				return null;
			} else {
				return events.get(0);
			}
		}
	}

	public String getExperimentMethodName(Experiment experiment,
			MethodDescription method) {

		Query q = em
				.createQuery("select event from "
						+ Event.class.getSimpleName()
						+ " event where event.execution.experiment = :experiment and event.execution.method = :method and event.name='"
						+ Event.EXPERIMENT_METHOD_NAME + "'");
		q.setParameter("experiment", experiment);
		q.setParameter("method", method);

		List<Event> events = (List<Event>) q.getResultList();
		if (events.isEmpty()) {
			return method.getProperties().getName();
		} else {
			Event event = (Event) q.getResultList().get(0);
			return (String) event.getValue();
		}
	}

	public long getTimeLimit(Experiment experiment,
			List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {

		return 1000;
	}

	public long getMaxTimeLimit(Experiment experiment,
			List<MethodDescription> subsetMethods,
			List<InstanceDescription> subsetInstances) {

		return 1000;
	}

	public List<ComputerDescription> findComputerDescriptionByName(
			String computerName) {

		Query q = em.createQuery("select c from "
				+ ComputerDescription.class.getSimpleName()
				+ " c where c.name = :name");
		q.setParameter("name", computerName);

		return (List<ComputerDescription>) q.getResultList();

	}

	public Researcher findResearcherByName(String researcherName) {

		Query q = em.createQuery("select r from "
				+ Researcher.class.getSimpleName() + " r where r.name = :name");
		q.setParameter("name", researcherName);

		return (Researcher) q.getSingleResult();

	}

	public List<Experiment> findExperiments() {

		Query q = em.createQuery("select e from "
				+ Experiment.class.getSimpleName() + " e");

		return (List<Experiment>) q.getResultList();
	}

	public Experiment findExperiment(long id) {

		Query q = em.createQuery("select e from "
				+ Experiment.class.getSimpleName() + " e where e.id = :id");
		q.setParameter("id", id);

		return (Experiment) q.getSingleResult();
	}

	public void removeExperiment(long experimentId) {

		em.createQuery(
				"DELETE FROM DoubleEvent event"
						+ " WHERE event.execution.experiment.id = :id")
				.setParameter("id", experimentId).executeUpdate();
		
		em.createQuery(
				"DELETE FROM LongEvent event"
						+ " WHERE event.execution.experiment.id = :id")
				.setParameter("id", experimentId).executeUpdate();
		
		em.createQuery(
				"DELETE FROM NonValueEvent event"
						+ " WHERE event.execution.experiment.id = :id")
				.setParameter("id", experimentId).executeUpdate();
		
		em.createQuery(
				"DELETE FROM StringEvent event"
						+ " WHERE event.execution.experiment.id = :id")
				.setParameter("id", experimentId).executeUpdate();

		em.createQuery(
				"DELETE FROM Execution exec WHERE exec.experiment.id = :id")
				.setParameter("id", experimentId).executeUpdate();

		em.createQuery("DELETE FROM Experiment e WHERE e.id = :id")
				.setParameter("id", experimentId).executeUpdate();

	}
}
