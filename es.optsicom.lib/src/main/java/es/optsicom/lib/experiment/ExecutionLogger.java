package es.optsicom.lib.experiment;

import java.io.Serializable;

import es.optsicom.lib.Solution;

public interface ExecutionLogger {

	public static class Event implements Serializable {
		
		private String name;
		private Object value;
		
		public Event(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}
		
		public Event(String name) {
			super();
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public Object getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return name+(value!=null?":"+value:"");
		}
	}
	
	void finishExecution(ExecutionResult execResult);

	void addEvent(Event event);
	
	void addEvents(Event... event);

	void newSolutionFound(double weight);

	void newSolutionFound(Solution<?> solution);
	
	void disableLogging(String scopeName);
	
	void enableLogging(String scopeName);

}
