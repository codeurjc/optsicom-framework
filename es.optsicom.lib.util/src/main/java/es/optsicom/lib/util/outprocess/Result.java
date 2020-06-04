package es.optsicom.lib.util.outprocess;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 3333980249335087357L;

	private Object result;
	private Throwable exception;
	private boolean correctExecution;

	public Result(Object result) {
		this.result = result;
		this.correctExecution = true;
	}

	public Result(Throwable exception) {
		this.exception = exception;
		this.correctExecution = false;
	}

	public boolean isCorrectExecution() {
		return correctExecution;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "Result [result=" + result + ", exception=" + exception + "]";
	}

}
