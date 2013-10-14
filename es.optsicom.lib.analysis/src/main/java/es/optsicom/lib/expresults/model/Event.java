package es.optsicom.lib.expresults.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.util.ArraysUtil;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
abstract public class Event implements Comparable<Event> {

	//TODO We need a better way to handle this constants
	public static final String OBJ_VALUE_EVENT = "objValue";
	public static final String SOLUTION_EVENT = "solution";
	public static final String FINISH_TIME_EVENT = "finishTime";
	public static final String EXPERIMENT_METHOD_NAME = "experimentMethodName";
	
	public static final String START_TIME_APPROX_METHOD = "startTimeApproxMethod";
	public static final String FINISH_TIME_APPROX_METHOD = "finishTimeApproxMethod";
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	private long timestamp;
	private String name;
	
	@JsonIgnore
	@ManyToOne
	private Execution execution;

	public Event(Execution execution, long timestamp, String name) {
		super();
		this.execution = execution;
		this.timestamp = timestamp;
		this.name = name;
	}
	
	public Event() {}

	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;		
	}

	public String getName() {
		return name;
	}

	public abstract Object getValue();

	@Override
	public String toString() {
		return "Event [timestamp=" + timestamp + ", name=" + name
				+ ", getValue()=" + getValue() + "]";
	}
	
	@JsonIgnore
	public Execution getExecution() {
		return execution;
	}

	public static Event createEvent(Execution exec, long time, String name, String value) {
		return new StringEvent(exec,time, name,value);
	}
	
	public static Event createEvent(Execution exec, long time, String name, double value) {
		return new DoubleEvent(exec,time, name,value);
	}
	
	public static Event createEvent(Execution exec, long time, String name, long value) {
		return new LongEvent(exec,time, name,value);
	}
	
	public static Event createEvent(Execution exec, long time, String name) {
		return new NonValueEvent(exec,time, name);
	}

	public static Event createEvent(Execution exec, long time, String name, Object value) {
		if(value == null){
			return createEvent(exec, time, name);
		} else if(value instanceof String){
			return createEvent(exec, time, name, (String)value);
		} else if(Double.class.isAssignableFrom(value.getClass())){
			return createEvent(exec, time, name, ((Double)value).doubleValue());
		} else if(Long.class.isAssignableFrom(value.getClass())){
			return createEvent(exec, time, name, ((Long)value).longValue());
		} else {
			return createEvent(exec, time, name, ArraysUtil.toStringObj(value));
		}		
	}

	public int compareTo(Event e){
		return (int) (this.timestamp - e.timestamp);
	}
	
}
