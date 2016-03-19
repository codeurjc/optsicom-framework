package es.optsicom.lib.expresults.manager;

import java.util.List;

import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;

public class ExecutionManager {

	private Execution execution;
	private ExperimentRepositoryManager manager;

	public ExecutionManager(Execution execution, ExperimentRepositoryManager manager) {
		this.execution = execution;
		this.manager = manager;
	}

	public double countEvents(String eventName) {
		return manager.countEvents(execution, eventName);
	}

	public List<Event> getEvents(String eventName) {
		return manager.getEvents(execution, eventName);
	}

	public Event getLastEvent(String eventName) {
		return manager.getLastEvent(execution, eventName);
	}

	public Event getLastEvent(String eventName, long timelimit) {
		return manager.getLastEvent(execution, eventName, timelimit);
	}

	public Execution getExecution() {
		return execution;
	}

	public List<Event> getEvents() {
		return execution.getEvents();
	}

}
