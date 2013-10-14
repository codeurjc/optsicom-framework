package es.optsicom.lib;

import java.io.IOException;
import java.util.List;

import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.instancefile.InstancesRepository;

/**
 * This class loads all instances in the selected useCase (or default useCase) and
 * calls to it's toString() method. This class is helpful to test the correct implementation
 * of instance loading code 
 */
public class InstanceLoaderTester {

	public static void loadAndPrintAllInstances(Problem problem){
		loadAndPrintAllInstances(problem,null);
	}
	
	public static void loadAndPrintAllInstances(Problem problem, String useCase) {
		
		InstancesRepository ir = problem.getInstancesRepository(useCase);
		
		List<InstanceFile> instanceFiles = ir.getAllInstanceFiles();
		
		int numInstance = 0;
		for(InstanceFile instanceFile : instanceFiles){
			try {
				System.out.println("Instance "+numInstance);
				System.out.println("InstanceFile: "+instanceFile);
				System.out.println("InstanceFile Properties: "+instanceFile.getProperties());
				Instance instance = ir.loadInstance(instanceFile);
				System.out.println("Instance: "+instance);				
			} catch (IOException e) {
				System.out.println("Exception loading instance file: "+instanceFile);
				e.printStackTrace();
			}
			System.out.println();
			numInstance++;
		}
		
	}
	
}
