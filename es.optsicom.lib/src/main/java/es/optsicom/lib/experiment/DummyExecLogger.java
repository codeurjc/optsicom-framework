package es.optsicom.lib.experiment;

import es.optsicom.lib.Solution;

public class DummyExecLogger implements ExecutionLogger {

	@Override
	public void finishExecution(ExecutionResult execResult) {
	}

	@Override
	public void addEvent(Event event) {
	}

	@Override
	public void addEvents(Event... event) {
	}

	@Override
	public void newSolutionFound(double weight) {
	}

	@Override
	public void newSolutionFound(Solution<?> solution) {
	}

	@Override
	public void disableLogging(String scopeName) {
	}

	@Override
	public void enableLogging(String scopeName) {
	}

}
