package es.optsicom.lib.experiment.db;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class ShowDBPredefinedData {

	public static void queryDummyData(DBManager dbManager, String problemName) {

		EntityManager em = dbManager.createEntityManager();

		Query q = em
				.createQuery("select e from Experiment e where e.name='predefined' and e.problemName = :problemName");
		q.setParameter("problemName", problemName);

		Experiment e = (Experiment) q.getSingleResult();

		System.out.println(e);

		System.out.println("Fetch methods and instances data from datebase");

		System.out.println("Instances: ");
		for (InstanceDescription i : e.getInstances()) {
			System.out.println(i);
			
			System.out.println("Methods: ");
			for (MethodDescription m : e.getMethods()) {
				System.out.println(m);

				q = em.createQuery("select e from " + 
						Execution.class.getSimpleName() + " e where e.experiment = :experiment " +
						"and e.instance = :instance and e.method = :method");
				q.setParameter("experiment", e);
				q.setParameter("instance", i);
				q.setParameter("method", m);
				Execution execution = (Execution) q.getResultList().get(0);
				for(Event event : execution.getEvents()) {
					System.out.println(event.getName() + "=" + event.getValue());
				}
			}
		}

		q = em.createQuery("select e from " + Execution.class.getSimpleName()
				+ " e where e.experiment = :experiment");
		q.setParameter("experiment", e);
		Execution execution = (Execution) q.getResultList().get(0);
		Event ev = execution.getEvents().get(0);
		System.out.println(ev.getName() + "=" + ev.getValue());

		System.out.println(e);
	}
	
}
