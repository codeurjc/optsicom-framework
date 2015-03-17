package es.optsicom.lib.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ReportConfiguration implements Serializable{

	private long expId;
	private List<Long> methods; // ids of methods
	private boolean bestValues;
	private boolean configuration;
	
	public ReportConfiguration() {
		super();
		this.expId = 0;
		this.methods = new ArrayList<Long>();
		this.bestValues = false;
		this.configuration = false;
	}
	
	public ReportConfiguration(long expId, List<Long> methods,
			boolean bestValues, boolean configuration) {
		super();
		this.expId = expId;
		this.methods = methods;
		this.bestValues = bestValues;
		this.configuration = configuration;
	}
	
	public long getExpId() {
		return expId;
	}
	public void setExpId(long expId) {
		this.expId = expId;
	}
	public List<Long> getMethods() {
		return methods;
	}
	public void setMethods(List<Long> methods) {
		this.methods = methods;
	}
	public boolean isBestValues() {
		return bestValues;
	}
	public void setBestValues(boolean bestValues) {
		this.bestValues = bestValues;
	}
	public boolean isConfiguration() {
		return configuration;
	}
	public void setConfiguration(boolean configuration) {
		this.configuration = configuration;
	}
	
	public void addMethod(Long method){
		if (method != null){
			this.methods.add(method);
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bestValues ? 1231 : 1237);
		result = prime * result + (configuration ? 1231 : 1237);
		result = prime * result + (int) (expId ^ (expId >>> 32));
		result = prime * result + ((methods == null) ? 0 : methods.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReportConfiguration))
			return false;
		ReportConfiguration other = (ReportConfiguration) obj;
		if (bestValues != other.bestValues)
			return false;
		if (configuration != other.configuration)
			return false;
		if (expId != other.expId)
			return false;
		if (methods == null) {
			if (other.methods != null)
				return false;
		} else if (!methods.equals(other.methods))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReportConfiguration [expId=" + expId + ", methods=" + methods
				+ ", bestValues=" + bestValues + ", configuration="
				+ configuration + "]";
	}
	
}
