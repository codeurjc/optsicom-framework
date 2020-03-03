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
import es.optsicom.lib.exact.cplex.CplexFormRelaxation;
import es.optsicom.lib.experiment.CurrentExperiment;
import es.optsicom.lib.experiment.ExperimentExecution;
import es.optsicom.lib.instancefile.InstanceFile;
import es.optsicom.lib.util.description.Properties;

public class RelaxationExperiment<S extends Solution<I>, I extends Instance> extends ExperimentExecution<S, I> {

	private final List<CplexFormRelaxation<S, I>> relaxations;
	private final List<String> relaxationNames = new ArrayList<String>();

	private long timeLimit = -1;

	public RelaxationExperiment(List<CplexFormRelaxation<S, I>> relaxations) {
		this(relaxations, -1);
	}

	public RelaxationExperiment(List<CplexFormRelaxation<S, I>> relaxations, long timelimit) {
		this.relaxations = relaxations;
		for(int i=0; i<relaxations.size(); i++){
			relaxationNames.add(null);
		}
		this.timeLimit = timelimit;
	}

	public RelaxationExperiment() {
		this(new ArrayList<CplexFormRelaxation<S,I>>());
	}
	
	public void addMethod(String experimentRelaxationName, CplexFormRelaxation<S, I> relaxation){
		this.relaxations.add(relaxation);
		this.relaxationNames.add(experimentRelaxationName);
	}
	
	public void addMethod(CplexFormRelaxation<S, I> relaxation){
		this.relaxations.add(relaxation);
		this.relaxationNames.add(null);
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

			long experimentationDuration = this.instanceFiles.size() * relaxations.size() * timeLimit;
			long finishTime = System.currentTimeMillis() + experimentationDuration;

			System.out.println("Experimentation time: "
			        + DateFormat.getTimeInstance().format(new Date(experimentationDuration)));
			System.out.println("Finalization time: " + DateFormat.getDateTimeInstance().format(new Date(finishTime)));

		}

		super.executeExperiment();
	}

	@Override
	protected List<List<Execution>> executeExperiment(I instance) {

		List<List<Execution>> results = new ArrayList<List<Execution>>();

		for(int i=0; i<relaxations.size(); i++){
			
			CplexFormRelaxation<S, I> relaxation = relaxations.get(i);
			String relaxationName = relaxationNames.get(i);
			Properties methodProperties = relaxation.getProperties();
			
			if(relaxationName == null){
				relaxationName = methodProperties.getName();
			}			
			
			System.out.println("  Relaxation: " + relaxationName);
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

			if(relaxationNames.get(i) != null){
				CurrentExperiment.addEvent(Event.EXPERIMENT_METHOD_NAME, relaxationNames.get(i));
			}
			
			RelaxationResult<S> result = relaxation.execute(instance, this.timeLimit);
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

	private void createEvents(long duration, Execution exec, RelaxationResult<S> result) {

		Event relaxSolutionEvent = new Event(duration, "relaxSolution", result.getSolution());
		exec.addEvent(relaxSolutionEvent);
		
		Event modeEvent = new Event(duration, "mode", result.getMode().toString());
		exec.addEvent(modeEvent);
		
		Event valuesEvent = new Event(duration, "values", result.getValues());
		exec.addEvent(valuesEvent);
		
		Event varNamesEvent = new Event(duration, "varNames", result.getVarNames());
		exec.addEvent(varNamesEvent);

		Event finishTimeEvent = new Event(duration, Event.FINISH_TIME_EVENT);
		exec.addEvent(finishTimeEvent);

		if (result.isAbortedByTime()) {

			Event upperBoundEvent = new Event(duration, "upperBound", result.getUpperBound());
			exec.addEvent(upperBoundEvent);

			ExecutionHelper.packEvents(relaxSolutionEvent, modeEvent, valuesEvent, varNamesEvent, varNamesEvent, finishTimeEvent, upperBoundEvent);

		} else {
			
			ExecutionHelper.packEvents(relaxSolutionEvent, modeEvent, valuesEvent, varNamesEvent, varNamesEvent, finishTimeEvent);
		}

	}

}
