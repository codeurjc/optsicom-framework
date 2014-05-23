/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.approx.experiment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.ApproxMethod;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.experiment.ExperimentExecution;
import es.optsicom.lib.experiment.ExperimentMethodStopCriteria;
import es.optsicom.lib.experiment.OptsicomException;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.saver.DBExecutionSaver;
import es.optsicom.lib.expresults.saver.ExperimentSaver;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.lib.util.description.Properties;
import es.optsicom.lib.util.outprocess.OutprocessWithOperations;

/**
 * Esta clase realiza un experimento que consiste en construir una o varias
 * soluciones por cada constructivo.
 * 
 * @author Mica
 * 
 * @param <S>
 * @param <I>
 */
public class ApproxMethodExperiment<S extends Solution<I>, I extends Instance>
		extends ExperimentExecution<S, I> {

	private static final double EPSILON = 0.001;

	private final List<ApproxMethod<S, I>> methods;

	private int executions;
	private boolean recordEvolution = false;
	private List<String> methodNames;
	private String description;
	
	protected ApproxExpConf approxExpConf;

	public ApproxMethodExperiment(ApproxExpConf approxExpConf, List<ApproxMethod<S, I>> solCalcs,
			int execIterations) {
		this.methods = solCalcs;
		this.executions = execIterations;
		this.approxExpConf = approxExpConf;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExecutions() {
		return executions;
	}

	public void setExecutions(int executions) {
		this.executions = executions;
	}

	@Override
	protected void experimentStarted(ExperimentSaver saver) {

		if (timeLimit != -1 && this.instanceTimeLimits == null) {

			long experimentationDuration = this.instanceFiles.size()
					* methods.size() * executions * timeLimit;
			long finishTime = System.currentTimeMillis()
					+ experimentationDuration;

			System.out.println("Experimentation time: "
					+ DateFormat.getTimeInstance().format(
							new Date(experimentationDuration)));
			System.out.println("Finalization time: "
					+ DateFormat.getDateTimeInstance().format(
							new Date(finishTime)));

		}

	}

	@Override
	protected void executeExperiment(ExperimentSaver saver, I instance,
			int instanceIndex, long timeLimit, List<OptsicomException> thrownExceptions, boolean localExecution) {

		InstanceDescription instanceDesc = instance.getInstanceFile().createInstanceDescription();
		
		for (int j = 0; j < this.methods.size(); j++) {

			ApproxMethod<S, I> method = this.methods.get(j);
			
			String expMethodName = null;
			if (methodNames != null) {
				expMethodName = methodNames.get(j);
			}
			
			if (expMethodName == null) {
				Properties methodProperties = method.getProperties();
				expMethodName = methodProperties.getName();
			}

			System.out.println("  Method: " + expMethodName + " Properties: "
					+ method.getProperties());

			for (int i = 0; i < executions; i++) {

				ExperimentMethodStopCriteria.experimentStarted();
				
				try {

					System.out.println("     Execution " + (i + 1) + "/"
							+ executions + ":");

					long newSeed = 345845976 * i;
					System.out.println("     Seed: " + newSeed);
					RandomManager.setSeed(newSeed);
					//RandomManager.setSeed(0);

					method.setInstance(instance);

					ExecutionResult executionResult = null;
					
					DBExecutionSaver execSaver = saver.startExecution(method.createMethodDescription(), instanceDesc, timeLimit);
					
					
					//Remoto!!!!!
					
					CurrentExperiment.startExecution(execSaver);					
					
					if (methodNames.get(j) != null) {
						CurrentExperiment.addEvent(
								Event.EXPERIMENT_METHOD_NAME,
								methodNames.get(j));
					}
					
					if (localExecution) {

						if (recordEvolution) {

							ExecutionApproxMethodListener<S, I> listener = new ExecutionApproxMethodListener<S, I>(
									execSaver);

							method.setSolutionCalculatorListener(listener);

							executionResult = (ApproxExecResult) method
									.execute(timeLimit);

							execSaver.finishExecution();

						} else {

							executionResult = (ApproxExecResult) method
									.execute(timeLimit);

							execSaver.finishExecution(executionResult
									.getBestSolution().getWeight(),
									executionResult.getBestSolution()
											.getInfoToSave());

						}
						
						//TODO Rehacer completamente el sistema de logging de ejecución
						
					} else {
						
						OutprocessWithOperations outprocess = new OutprocessWithOperations();
						
						Class approxExpConfClass = this.approxExpConf.getClass(); 
						
						outprocess.startOutprocess(new RemoteExperimentExecutor(approxExpConfClass));
						executionResult = (RemoteApproxExecResult) outprocess.execOperation("executeMethodInstance", j, instanceIndex, newSeed);				
						
						execSaver.finishExecution(executionResult.getBestSolution().getWeight(), executionResult.getBestSolution().getInfoToSave());
						
					}
					
					CurrentExperiment.finishExecution();
					
					Solution bestSolution = executionResult.getBestSolution();
					
					System.out.println();
					System.out.format("\tTime: %d\t W:%.3f\r\n", execSaver.getExecutionTime(),
							bestSolution.getWeight());
					System.out.println("\tSolution: " + bestSolution);
					
					double naiveWeight = bestSolution.calculateNaiveWeight();					
					if(Math.abs(naiveWeight - bestSolution.getWeight()) > EPSILON){
						System.out.println("\tNaive weight: " + naiveWeight);
						System.out.println("\tSolution weight: " + bestSolution.getWeight());
						System.out.println("\tERROR: Naive weight and solution weight are different !!!!!!!!!!!!!");
					}
					
				} catch (Exception e) {
					System.out.println("\tException: " + e.getClass().getName()+":"+e.getMessage());
					e.printStackTrace();
					thrownExceptions.add(new OptsicomException(instance.getInstanceFile(), method, e));					
				}
			}
			
			//Delete me please!! It is a hack to test something...
			//RandomManager.setSeed(0);

			// it frees memory
			method.removeInstance();

			// Log.debugln("---------------------------------");
			// Log.debugln("Constructive: " + constructive.getDescription() +
			// " " + numConstructions + " W:"
			// + solution.getWeight());
		}

	}

	public boolean isRecordEvolution() {
		return recordEvolution;
	}

	public void setRecordEvolution(boolean recordEvolution) {
		this.recordEvolution = recordEvolution;
	}

	public void setMethodNames(List<String> methodNames) {
		this.methodNames = methodNames;
	}
}
