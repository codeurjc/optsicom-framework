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
package es.optsicom.lib.exact;


import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExperimentExecution;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.util.description.Properties;

public class ExactExperiment<S extends Solution<I>, I extends Instance> extends ExperimentExecution<S, I> {

	private final List<ExactMethod<S, I>> exactMethods;
	private final List<String> methodNames = new ArrayList<String>();

	public ExactExperiment(List<ExactMethod<S, I>> exactMethods) {
		this(exactMethods, -1);
	}

	public ExactExperiment(List<ExactMethod<S, I>> exactMethods, long timelimit) {
		this.exactMethods = exactMethods;
		for(int i=0; i<exactMethods.size(); i++){
			methodNames.add(null);
		}
		this.timeLimit = timelimit;
	}

	public ExactExperiment() {
		this(new ArrayList<ExactMethod<S,I>>());
	}
	
	public void addMethod(String experimentMethodName, ExactMethod<S, I> method){
		this.exactMethods.add(method);
		this.methodNames.add(experimentMethodName);
	}
	
	public void addMethod(ExactMethod<S, I> method){
		this.exactMethods.add(method);
		this.methodNames.add(null);
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	@Override
	public void executeExperiment() throws IOException {

		if (timeLimit != -1) {

			long experimentationDuration = this.instanceFiles.size() * exactMethods.size() * timeLimit;
			long finishTime = System.currentTimeMillis() + experimentationDuration;

			System.out.println("Experimentation time: "
			        + DateFormat.getTimeInstance().format(new Date(experimentationDuration)));
			System.out.println("Finalization time: " + DateFormat.getDateTimeInstance().format(new Date(finishTime)));

		}

		super.executeExperiment();
	}

	@Override
	protected List<List<Execution>> executeExperiment(I instance, long timeLimit) {

		List<List<Execution>> results = new ArrayList<List<Execution>>();

		for(int i=0; i<exactMethods.size(); i++){
			
			ExactMethod<S, I> method = exactMethods.get(i);
			String expMethodName = methodNames.get(i);
			Properties methodProperties = method.getProperties();
			
			if(expMethodName == null){
				expMethodName = methodProperties.getName();
			}			
			
			System.out.println("  Method: " + expMethodName);
			System.out.println("  Properties: " + methodProperties);

			List<Execution> execs = new ArrayList<Execution>();
			results.add(execs);

			long duration;

			MethodDescription methodDescription = new MethodDescription(methodProperties);

			Execution exec = new Execution(methodDescription, InstanceDescriptorHelper
			        .createInstanceDescription(instance));

			long startTime = System.currentTimeMillis();
			CurrentExperiment.setStartTime(startTime);
			CurrentExperiment.setExecution(exec);

			if(methodNames.get(i) != null){
				CurrentExperiment.addEvent(Event.EXPERIMENT_METHOD_NAME, methodNames.get(i));
			}
			
			ExactResult<S> result = method.execute(instance, timeLimit);
			duration = System.currentTimeMillis() - startTime;

			if (result != null) {
				createEvents(duration, exec, result);

				execs.add(exec);

				System.out.format("\tResult: " + result + "\n");
			} else {
				System.out.println("\tSolution not found");
			}

			// Log.debugln("---------------------------------");
			// Log.debugln("Constructive: " + constructive.getDescription() + "
			// " + numConstructions + " W:"
			// + solution.getWeight());
		}

		return results;

	}

	private void createEvents(long duration, Execution exec, ExactResult<S> result) {

		Event objValueEvent = new Event(duration, Event.OBJ_VALUE_EVENT, result.getSolution().getWeight());
		exec.addEvent(objValueEvent);

		Event solutionEvent = new Event(duration, Event.SOLUTION_EVENT, result.getSolution().getInfoToSave());
		exec.addEvent(solutionEvent);

		Event finishTimeEvent = new Event(duration, Event.FINISH_TIME_EVENT);
		exec.addEvent(finishTimeEvent);

		Event numNodesEvent = new Event(duration, "numNodes", result.getNumVisitedNodes());
		exec.addEvent(numNodesEvent);

		if (result.isAbortedByTime()) {

			Event lowerBoundEvent = new Event(duration, "lowerBound", result.getLowerBound());
			exec.addEvent(lowerBoundEvent);
			Event upperBoundEvent = new Event(duration, "upperBound", result.getUpperBound());
			exec.addEvent(upperBoundEvent);

			ExecutionHelper.packEvents(objValueEvent, solutionEvent, finishTimeEvent, lowerBoundEvent, upperBoundEvent,
			        numNodesEvent);

		} else {
			ExecutionHelper.packEvents(objValueEvent, solutionEvent, finishTimeEvent, numNodesEvent);
		}

	}

}
