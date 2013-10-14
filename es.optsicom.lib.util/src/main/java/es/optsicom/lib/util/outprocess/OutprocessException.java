package es.optsicom.lib.util.outprocess;

public class OutprocessException extends Exception {

	private static final long serialVersionUID = 6671919117815769653L;

	public OutprocessException(String message) {
		super(message);
	}

	public OutprocessException(Throwable cause) {
		super(cause);
	}

	public OutprocessException(String message, Throwable cause) {
		super(message, cause);
	}

}
