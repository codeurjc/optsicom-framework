package es.optsicom.lib.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ReportConfiguration implements Serializable{

	private List<Long> expId;
	private List<Long> methods; // ids of methods
	private boolean bestValues;
	private boolean configuration;
	
	public ReportConfiguration() {
		super();
		this.expId = new ArrayList<Long>();
		this.methods = new ArrayList<Long>();
		this.bestValues = false;
		this.configuration = false;
	}
	
	public ReportConfiguration(List<Long> expId, List<Long> methods,
			boolean bestValues, boolean configuration) {
		super();
		this.expId = expId;
		this.methods = methods;
		this.bestValues = bestValues;
		this.configuration = configuration;
	}
	
	public List<Long> getExpId() {
		return expId;
	}
	public void setExpId(List<Long> expId) {
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
	public String toString() {
		return "ReportConfiguration [expId=" + expId + ", methods=" + methods
				+ ", bestValues=" + bestValues + ", configuration="
				+ configuration + "]";
	}
	
}
