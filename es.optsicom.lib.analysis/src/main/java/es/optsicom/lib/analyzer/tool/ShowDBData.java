package es.optsicom.lib.analyzer.tool;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import es.optsicom.lib.expresults.db.DBManager;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.StringEvent;

public class ShowDBData {

	public static void queryDummyData(DBManager dbManager) {

		EntityManager em = dbManager.createEntityManager();

		Query q = em.createQuery("select e from Experiment e");
		List<Experiment> experiments = (List<Experiment>) q.getResultList();

		for (Experiment e : experiments) {
			showExperimentInfo(em,e);
		}
	}

	public static void showExperiments(DBManager dbManager) {
		EntityManager em = dbManager.createEntityManager();

		Query q = em.createQuery("select e from Experiment e");
		List<Experiment> experiments = (List<Experiment>) q.getResultList();

		for (Experiment e : experiments) {
			System.out.println("Name: " + e.getName());
			System.out.println("Id: " + e.getId());
			System.out.println("Date: " + e.getDate());
			System.out.println();
		}
	}

	public static void showExperimentInfo(DBManager dbManager, int id) {

		EntityManager em = dbManager.createEntityManager();

		Query q = em.createQuery("select e from Experiment e where e.id = :id");
		q.setParameter("id", id);
		Experiment e = (Experiment) q.getResultList().get(0);

		showExperimentInfo(em, e);

	}

	private static void showExperimentInfo(EntityManager em, Experiment e) {
		Query q;
		System.out.println(e.getName() + " (id=" + e.getId() + ")");

		System.out.println("Num instances: " + e.getInstances().size());

		System.out.println("Methods: ");

		q = em.createQuery("select distinct ev.value, e.method from StringEvent ev, Execution e where e = ev.execution and ev.execution.experiment = :experiment and ev.name = :name");
		q.setParameter("experiment", e);
		q.setParameter("name", "experimentMethodName");

		
		for (Object[] values : (List<Object[]>) q.getResultList()) {

			String methodExpName = (String) values[0];
			MethodDescription method = (MethodDescription) values[1];
			System.out.println(" Method: " + methodExpName + " -> "
					+ method);
		}
		
		q = em.createQuery("select e from Execution e where e.experiment = :experiment");
		q.setParameter("experiment", e);
		
		List<Execution> executions = (List<Execution>) q.getResultList();
		System.out.println("Num execs: "+executions.size());
		for (Execution exec : executions) {

			System.out.println(exec.getInstance().getName()+" \t "+exec.getLastEvent(Event.OBJ_VALUE_EVENT).getValue());
		}
	}

}
