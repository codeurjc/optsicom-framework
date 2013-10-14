/**
 *  Package that includes classes on the chart in the data model
 */
package es.optsicom.lib.analyzer.report.chart;

import java.io.Serializable;

/**
 * @author paco
 * Specifies if the data series displayed in the chart, take their values 
 * from the row or the column 
 */

public enum ChartSourceType implements Serializable{COLUMN, ROW};
