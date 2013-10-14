package es.optsicom.lib.experiment.db;

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

public class ShowDBData {
	
	public static void queryDummyData(DBManager dbManager) {
			
			EntityManager em = dbManager.createEntityManager();
			
			Query q = em.createQuery("select e from Experiment e");
			List<Experiment> experiments = (List<Experiment>) q.getResultList(); 
			
			for(Experiment e : experiments){
				System.out.println(e);
			}
			
			System.out.println("Fetch methods and instances data from datebase");
			
			for(Experiment e : experiments){
				System.out.println(e.getName());
				
				System.out.println("Methods: ");
				for(MethodDescription m : e.getMethods()){
					System.out.println(m);
				}
				
				System.out.println("Instances: ");
				for(InstanceDescription i : e.getInstances()){
					System.out.println(i);
				}
				
				q = em.createQuery("select e from " + Execution.class.getSimpleName() + " e where e.experiment = :experiment");
				q.setParameter("experiment", e);
				
				
				if(!q.getResultList().isEmpty()) {
					Execution execution = (Execution) q.getResultList().get(0);
					Event ev = execution.getEvents().get(0);
					System.out.println(ev.getName() + "=" + ev.getValue());
				}
				
			}
		}
}

