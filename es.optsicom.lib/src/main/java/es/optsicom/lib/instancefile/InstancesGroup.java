package es.optsicom.lib.instancefile;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.OrElementsFilter;


public abstract class InstancesGroup {
	
	private List<InstanceFile> instanceFiles = new ArrayList<InstanceFile>();
	protected InstancesRepository instancesRepository;
	
	public List<InstanceFile> getInstanceFiles(){
		addInstanceFiles();
		return instanceFiles;
	}
	
	protected void addInstanceFile(InstanceFile instanceFile){
		instanceFiles.add(instanceFile);
	}
	
	protected void addByName(String instanceName){
		instanceFiles.add(instancesRepository.getInstanceFileByName(instanceName));
	}
	
	protected abstract void addInstanceFiles();

	public void setInstancesRepository(InstancesRepository repository) {
		this.instancesRepository = repository;		
	}
	
}
