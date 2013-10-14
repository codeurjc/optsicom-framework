package es.optsicom.lib.experiment.db;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import es.optsicom.lib.expresults.db.DerbyDBManager;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.DoubleEvent;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.NonValueEvent;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.expresults.model.StringEvent;

public class CreateDummyData {

	private static final int NUM_INSTANCES = 5;
	private static final int NUM_ALGORITHM = 5;

	public static void main(String[] args) throws SQLException {
		
		DerbyDBManager dbManager = new DerbyDBManager(new File("derby-test"));
				
		createDummyData(dbManager);
		queryDummyData(dbManager);
		
		dbManager.close();
		
	}

	private static void createDummyData(DerbyDBManager dbManager) {
		EntityManager em = dbManager.createEntityManager();
		em.getTransaction().begin();
		
		//Create and persist instances
		List<InstanceDescription> instances = new ArrayList<InstanceDescription>();
		for(int i=0; i<NUM_INSTANCES; i++){
			
			Map<String,String> properties = new HashMap<String,String>();
			
			properties.put("name", "file"+i+".txt");
			properties.put("prop1", "value_inst_"+i);
			properties.put("prop2", "value_inst_"+i);
			properties.put("prop3", "value_inst_"+i);
			
			InstanceDescription instance = new InstanceDescription(properties);
			
			instances.add(instance);
			em.persist(instance);
		}

		//Create and persist algorithms
		List<MethodDescription> methods = new ArrayList<MethodDescription>();
		for(int i=0; i<NUM_ALGORITHM; i++){
			
			Map<String,String> properties = new HashMap<String,String>();
			
			properties.put("name", "Method "+i);
			properties.put("prop1", "value_inst_"+i);
			properties.put("prop2", "value_inst_"+i);
			properties.put("prop3", "value_inst_"+i);
			
			MethodDescription method = new MethodDescription(properties);
			
			methods.add(method);
			em.persist(method);
		}
		
		//Create and persist the User
		Researcher user = new Researcher("User1");
		em.persist(user);
		
		em.getTransaction().commit();
		
		System.out.println("Primera fase completada.");
		
		em.getTransaction().begin();
		
		ComputerDescription cd = new ComputerDescription("Machine1");
		em.persist(cd);
		
		//Create and persist the executed Experiment
		Experiment exp = new Experiment("name",user,new Date(), cd);
				
		//Load all instances in experiment
		for(InstanceDescription instance : instances){
			exp.getInstances().add(instance);
		}
		
		//Load all algorithms in experiment		
		for(MethodDescription algorithm : methods){
			exp.getMethods().add(algorithm);
		}
		
		em.persist(exp);
		
		em.getTransaction().commit();
		
		System.out.println("Segunda fase completada.");
		
		em.getTransaction().begin();
		
		//Load raw data into execution
		for(InstanceDescription instance: instances){
			
			for(MethodDescription algorithm: methods){
				
				Execution exec = new Execution(exp,instance,algorithm,-1);
		
				//Considering 3 events per execution by instance and method
				em.persist( new NonValueEvent(exec,15454, "FINISH") );
				em.persist( new DoubleEvent(exec,15454, "OBJ_VALUE", 3.4f) );
				em.persist( new StringEvent(exec,15454, "SOLUTION", "[1,34,33,12]" ));
				
				em.persist(exec);
			}
			
		}		
		
		em.getTransaction().commit();	
		em.close();
		
		System.out.println("Tercera fase completada.");
	}

	private static void queryDummyData(DerbyDBManager dbManager) {
		
		EntityManager em = dbManager.createEntityManager();
		
		Query q = em.createQuery("select e from Experiment e");
		List<Experiment> experiments = (List<Experiment>) q.getResultList(); 
		
		for(Experiment e : experiments){
			System.out.println(e);
		}
		
		System.out.println("Fetch methods and instances data from datebase");
		
		for(Experiment e : experiments){
			
			System.out.println("Methods: ");
			for(MethodDescription m : e.getMethods()){
				System.out.println(m);
			}
			
			System.out.println("Instances: ");
			for(InstanceDescription i : e.getInstances()){
				System.out.println(i);
			}
			
			System.out.println(e);
		}
	}

}
