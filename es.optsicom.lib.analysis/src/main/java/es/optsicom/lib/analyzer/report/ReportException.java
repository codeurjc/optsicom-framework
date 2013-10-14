/**
 * Package that includes the other packages of the Carmar project and classes that refer 
 * to the spreadsheet in memory
 */
package es.optsicom.lib.analyzer.report;

/**
 * @author paco
 *
 */
public class ReportException extends RuntimeException {

	private static final long serialVersionUID = 9162631312965111008L;

	/**
	 * 
	 */
	public ReportException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ReportException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ReportException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReportException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
