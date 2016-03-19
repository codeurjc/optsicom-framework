package es.optsicom.lib.approx.experiment;

import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Solution;
import es.optsicom.lib.experiment.EventNames;
import es.optsicom.lib.experiment.ExecutionLogger.Event;
import es.optsicom.lib.experiment.ExecutionResult;

@SuppressWarnings("rawtypes")
public class ApproxExecResult extends ExecutionResult {

	private static final long serialVersionUID = 7929398652280554156L;
	
	private Solution solution;

	public ApproxExecResult(Solution solution) {
		this.solution = solution;
	}

	@Override
	public Solution getBestSolution() {
		return solution;
	}

	@Override
	public String toString() {
		return super.toString() + "Weight:" + solution.getWeight() + "\r\nSolution:" + solution;
	}

	@Override
	protected List<Event> createFinishEvents() {

		List<Event> events = new ArrayList<Event>(rawEvents);

		events.add(new Event(EventNames.OBJ_VALUE, solution.getWeight()));
		events.add(new Event(EventNames.SOLUTION, solution.getInfoToSave()));
		events.add(new Event(EventNames.FINISH_TIME));

		return events;

	}

}
