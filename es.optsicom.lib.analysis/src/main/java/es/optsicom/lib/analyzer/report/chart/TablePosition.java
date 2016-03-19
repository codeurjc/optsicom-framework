/**
 * Package that includes classes on the table in the data model
 */
package es.optsicom.lib.analyzer.report.chart;

import java.io.Serializable;

/**
 * @author paco Class refers to a coordinate on the table
 */
public class TablePosition implements Serializable {

	private static final long serialVersionUID = 8881692313915753301L;

	/** Row of the coordinate */
	private int row;

	/** Column of the coordinate */
	private int column;

	/**
	 * Constructor of the class
	 * 
	 * @param row
	 *            Row number of the coordinate
	 * @param column
	 *            Column number of the coordinate
	 * @throws ChartException
	 */
	public TablePosition(int row, int column) throws ChartException {
		if ((row < 0) || (column < 0)) {
			throw new ChartException("The values of coordinate must be positive", new ChartException());
		}
		this.row = row;
		this.column = column;
	}

	/**
	 * Constructor of the class
	 */
	public TablePosition() {

	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

}
