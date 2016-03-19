package es.optsicom.lib.approx.experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.experiment.EventNames;
import es.optsicom.lib.experiment.ExecutionLogger.Event;
import es.optsicom.lib.experiment.ExecutionResult;
import es.optsicom.lib.util.ArraysUtil;

public class RemoteApproxExecResult extends ExecutionResult implements Serializable {

	private static final long serialVersionUID = 8391367084252225494L;

	private DummySolution bestSolution;
	private List<Event> events;

	public RemoteApproxExecResult(Solution<?> solution) {
		bestSolution = new DummySolution(solution.getWeight(), ArraysUtil.toStringObj(solution.getInfoToSave()));
	}

	public RemoteApproxExecResult(Solution<?> bestSolution, List<Event> events) {
		this(bestSolution);
		this.events = events;
	}

	/**
	 * Events recorded during execution
	 * 
	 * @return Events recorded during execution
	 */
	public List<Event> getEvents() {
		return events;
	}

	@Override
	protected List<Event> createFinishEvents() {

		List<Event> events = new ArrayList<Event>(rawEvents);

		events.add(new Event(EventNames.OBJ_VALUE, bestSolution.getWeight()));
		events.add(new Event(EventNames.SOLUTION, bestSolution.getInfoToSave()));
		events.add(new Event(EventNames.FINISH_TIME));

		return events;

	}

	@Override
	public Solution<?> getBestSolution() {

		return bestSolution;

	}

}

@SuppressWarnings("rawtypes")
class DummySolution extends Solution implements Serializable {

	private static final long serialVersionUID = 5110095336691047983L;
	
	private double weight;
	private String solutionInfo;

	public DummySolution(double weight, String solutionInfo) {
		this.weight = weight;
		this.solutionInfo = solutionInfo;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public Solution createCopy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Instance getInstance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getInfoToSave() {
		return solutionInfo;
	}

	@Override
	public boolean isBetterThan(Solution aSolution) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void asSolution(Solution solution) {
		throw new UnsupportedOperationException();
	}

}
