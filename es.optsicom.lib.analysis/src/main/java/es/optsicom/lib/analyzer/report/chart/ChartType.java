/**
 *  Package includes classes related to the graphical interface
 */
package es.optsicom.lib.analyzer.report.chart;

import java.io.Serializable;

/**
 * @author paco
 * Specifies if the chart XY is composed by only points, only lines or lines and points
*/

public enum ChartType implements Serializable {POINTS, LINES, LINES_AND_POINTS, LINES_3D};
