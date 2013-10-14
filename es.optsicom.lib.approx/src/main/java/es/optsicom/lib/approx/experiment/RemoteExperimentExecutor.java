package es.optsicom.lib.approx.experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Method;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExecutionLogger.Event;
import es.optsicom.lib.expresults.saver.ExecutionSaver;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.util.RandomManager;

public class RemoteExperimentExecutor implements Serializable {
	
	private Class<ApproxExpConf> clazz;
	
	public RemoteExperimentExecutor(Class<ApproxExpConf> clazz) {
		this.clazz = clazz;
	}
	
	public RemoteApproxExecResult executeMethodInstance(int methodIndex, int instanceIndex, long randomSeed){
		
		try {
			
			final List<Event> events = new ArrayList<Event>();
			
			CurrentExperiment.startExecution(new ExecutionSaver() {
				@Override
				public void addEvent(String eventName, Object value) {
					events.add(new Event(eventName,value));					
				}
			});
			
			ApproxExpConf expConf = clazz.newInstance();
			expConf.calculateInstanceFilesAndTimes();
			
			Method method = expConf.getMethods().get(methodIndex);
			InstanceFile instanceFile = expConf.getInstanceFiles().get(instanceIndex);
			
			method.setInstance(instanceFile.loadInstance());
			
			long timeLimit = -1;
			List<Long> instanceTimeLimits = expConf.getInstanceTimeLimits();
			if(instanceTimeLimits != null){
				timeLimit = instanceTimeLimits.get(instanceIndex);
			} else {
				timeLimit = expConf.getTimeLimitInMillis();
			}
			
			System.out.println("     Seed: " + randomSeed);
			RandomManager.setSeed(randomSeed);
			
			ApproxExecResult execResult = (ApproxExecResult) method.execute(timeLimit);
			
			RemoteApproxExecResult remoteExecResult = new RemoteApproxExecResult(execResult.getBestSolution(), events);
			
			CurrentExperiment.finishExecution();
			
			return remoteExecResult;
			
		} catch (Exception e) {
			throw new RuntimeException("Exception creating ApproxExpConf in remote JVM",e);
		}
	}
	
}
