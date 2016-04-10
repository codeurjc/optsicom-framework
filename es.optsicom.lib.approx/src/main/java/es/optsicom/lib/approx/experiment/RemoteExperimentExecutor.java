package es.optsicom.lib.approx.experiment;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Method;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExecutionLogger.Event;
import es.optsicom.lib.expresults.saver.ExecutionSaver;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.instancefile.InstancesRepository;
import es.optsicom.lib.util.RandomManager;

public class RemoteExperimentExecutor implements Serializable {

	private static final long serialVersionUID = 1254155131789129987L;

	private Class<? extends ApproxExpConf> clazz;

	public RemoteExperimentExecutor(Class<? extends ApproxExpConf> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RemoteApproxExecResult executeMethodInstance(int methodIndex, int instanceIndex, long randomSeed) {

		try {

			final List<Event> events = new ArrayList<Event>();

			CurrentExperiment.startExecution(new ExecutionSaver() {
				@Override
				public void addEvent(String eventName, Object value) {
					events.add(new Event(eventName, value));
				}
			});

			ApproxExpConf expConf = clazz.newInstance();
			
			InstancesRepository repository = createInstancesRepository(expConf);
			
			expConf.calculateInstanceFilesAndTimes(repository);

			Method method = expConf.getMethods().get(methodIndex);
			InstanceFile instanceFile = expConf.getInstanceFiles().get(instanceIndex);

			method.setInstance(instanceFile.loadInstance());

			long timeLimit = -1;
			List<Long> instanceTimeLimits = expConf.getInstanceTimeLimits();
			if (instanceTimeLimits != null) {
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
			throw new RuntimeException("Exception creating ApproxExpConf in remote JVM", e);
		}
	}
	
	private InstancesRepository createInstancesRepository(ApproxExpConf approxExpConf) {
		
		String instancesFilesDir = approxExpConf.getInstancesFilesDir();
		String useCase = approxExpConf.getUseCase();
		
		if (instancesFilesDir == null) {
			return approxExpConf.getProblem().getInstancesRepository(useCase);
		} else {
			return approxExpConf.getProblem().getInstancesRepository(new File(instancesFilesDir), useCase);
		}
	}

}
