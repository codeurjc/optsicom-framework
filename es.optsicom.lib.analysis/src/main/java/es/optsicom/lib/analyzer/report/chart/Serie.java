/**
 * Package that includes classes on the chart in the data model
 */
package es.optsicom.lib.analyzer.report.chart;

import java.io.Serializable;


/**
 * @author paco
 * Class to build the series of the chart from the table
 */
public class Serie implements Serializable{
	
	private static final long serialVersionUID = -8973199332420545497L;

	/** Position of column or row */
	private int seriesPosition;
	
	
	/**
	 * Constructor of the series
	 * @param seriesPosition Indicates the position of the column or of the row
	 */
	public Serie(int seriesPosition){
		this.seriesPosition = seriesPosition;
		
	}
	
	/**
	 * @param seriesPosition the seriesPosition to set
	 */
	public void setSeriesPosition(int seriesPosition) {
		this.seriesPosition = seriesPosition;
	}

	/**
	 * @return the seriesPosition
	 */
	public int getSeriesPosition() {
		return seriesPosition;
	}

}

