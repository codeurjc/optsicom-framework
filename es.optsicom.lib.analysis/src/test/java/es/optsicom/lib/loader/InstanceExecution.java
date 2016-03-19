package es.optsicom.lib.loader;

public class InstanceExecution {

	private String instanceName;
	private double value;
	private long execTime;

	public InstanceExecution(String instanceName, double value, long execTime) {
		super();
		this.instanceName = instanceName;
		this.value = value;
		this.execTime = execTime;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getExecTime() {
		return execTime;
	}

	public void setExecTime(long execTime) {
		this.execTime = execTime;
	}

	@Override
	public String toString() {
		return "InstanceExecution [instanceName=" + instanceName + ", value=" + value + ", execTime=" + execTime + "]";
	}

}
