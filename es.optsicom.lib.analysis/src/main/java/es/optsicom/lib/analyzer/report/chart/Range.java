/**
 * Package that includes classes on the table in the data model
 */
package es.optsicom.lib.analyzer.report.chart;

import java.io.Serializable;

/**
 * @author paco Class refers to a range of cells
 */
public class Range implements Serializable {

	private static final long serialVersionUID = -6961775104889752350L;

	/** Coordinate where start the range */
	private TablePosition start;

	/** Coordinate where end the range */
	private TablePosition end;

	/**
	 * Constructor of the class
	 * 
	 * @param start
	 *            Coordinate where start the range
	 * @param end
	 *            Coordinate where end the range
	 */
	public Range(TablePosition start, TablePosition end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * @return the start
	 */
	public TablePosition getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public TablePosition getEnd() {
		return end;
	}

}
