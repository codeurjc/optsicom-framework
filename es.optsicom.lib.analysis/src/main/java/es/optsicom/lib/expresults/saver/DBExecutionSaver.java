package es.optsicom.lib.expresults.saver;

import javax.persistence.EntityManager;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;

public class DBExecutionSaver implements ExecutionSaver {

	private ExperimentSaver expSaver;
	private EntityManager em;
	private Execution execution;
	private long startTime;
	private long executionTime;

	public DBExecutionSaver(EntityManager em, ExperimentSaver experimentSaver, InstanceDescription instance,
			MethodDescription method, long timeLimit) {
		this.em = em;
		this.expSaver = experimentSaver;

		// TODO This can be optimized reusing MethodDescription object from
		// Experiment configuration. Although this require associate the
		// MethodDescription to method object.
		method = expSaver.findMethodDescription(em, method.getProperties().toString());
		instance = expSaver.findInstanceDescription(em, instance.getProperties().toString());

		this.execution = new Execution(experimentSaver.getExperiment(), method, instance);
		this.execution.setTimeLimit(timeLimit);
		this.startTime = System.currentTimeMillis();
		this.em.getTransaction().begin();
	}

	public void finishExecution() {

		this.executionTime = calculateTimestamp();

		Event event3 = Event.createEvent(execution, executionTime, Event.FINISH_TIME_EVENT);
		execution.addEvent(event3);

		this.em.persist(execution);
		this.em.getTransaction().commit();
		this.em.close();
	}

	public void finishExecution(double solutionValue, Object solution) {

		this.executionTime = calculateTimestamp();

		addSolutionEvents(solutionValue, solution, executionTime);

		Event event3 = Event.createEvent(execution, executionTime, Event.FINISH_TIME_EVENT);
		execution.addEvent(event3);

		this.em.persist(execution);
		this.em.getTransaction().commit();
		this.em.close();
	}

	public void addEvent(String name, Object value) {
		Event event = Event.createEvent(execution, calculateTimestamp(), name, value);
		execution.addEvent(event);
	}

	public void addSolutionEvents(double solutionValue, Object solution) {

		long timestamp = calculateTimestamp();

		addSolutionEvents(solutionValue, solution, timestamp);

	}

	protected void addSolutionEvents(double solutionValue, Object solution, long timestamp) {
		Event event = Event.createEvent(execution, timestamp, Event.OBJ_VALUE_EVENT, solutionValue);
		execution.addEvent(event);

		Event event2 = Event.createEvent(execution, timestamp, Event.SOLUTION_EVENT, solution);
		execution.addEvent(event2);
	}

	public void addSolutionValueEvent(double solutionValue) {

		long timestamp = calculateTimestamp();

		Event event = Event.createEvent(execution, timestamp, Event.OBJ_VALUE_EVENT, solutionValue);
		execution.addEvent(event);

	}

	private long calculateTimestamp() {
		return System.currentTimeMillis() - startTime;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public long getStartTime() {
		return startTime;
	}

}
