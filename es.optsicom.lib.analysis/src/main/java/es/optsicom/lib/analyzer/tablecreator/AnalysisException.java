package es.optsicom.lib.analyzer.tablecreator;

public class AnalysisException extends RuntimeException {

	private static final long serialVersionUID = -929744165118400012L;

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnalysisException(Throwable cause) {
		super(cause);
	}
}
