package es.optsicom.lib.util.outprocess;

import java.io.Serializable;
import java.util.Arrays;

public class OperationPacket implements Serializable {

	private static final long serialVersionUID = -993107529368398160L;
	
	private String operationName;
	private Object[] params;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "OperationPacket [operationName=" + operationName + ", params="
				+ Arrays.toString(params) + "]";
	}

	public OperationPacket(String operationName, Object[] params) {
		super();
		this.operationName = operationName;
		this.params = params;
	}

}
