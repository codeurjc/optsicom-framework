/**
 * Package that includes classes on the chart in the data model
 */
package es.optsicom.lib.analyzer.report.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.table.Table;


/**
 * @author paco
 * Class to build the chart from the series
 */
public class Chart implements ReportElement, Serializable {
	
	private static final long serialVersionUID = -500366668493826899L;

	/** Name of the chart */
	private String nameChart;
	
	/** Name of abscissa axis */
	private String nameAxisX;
	
	/** Name of ordinate */
	private String nameAxisY;
	
	/** Minimum position of the series (columns or rows) */
	private int minCell;
	
	/** Maximum position of the series (columns or rows) */
	private int maxCell;
	
	/** Set of series of the chart */
	private List <Serie> series;
	
	/** Table corresponding to the chart */
	private Table table;
	
	/** Data series by row or column (column by default) */
	private ChartSourceType sourceType;
	
	/** Type of line of the chart*/
	private ChartType chartType; 
	
	/** The subtitle of the chart */
	private String subtitle;
	
	/** Grid of axis X */
	private boolean gridAxisX;
	
	/** Grid of axis Y */
	private boolean gridAxisY;
	
	/** Legend of the chart */
	private boolean legend;
	
	/** Position of the legend */
	private Position positionLegend;
	
	/** Indicate if the chart is built by basic constructor */
	private boolean basicConstructor = false;
	
	/**
	 * Constructor of the chart (by default, the series of the chart are built by column) 
	 * @param series Set of series of the chart (by default, the series cover the entire row or column)
	 * @param table Table corresponding to the chart
	 */
	public Chart (Serie series[], Table table) {
		
		int numSeries = series.length;
		this.series = new ArrayList <Serie> (numSeries);
		for (int i=0; i<numSeries; i++){
			this.series.add(series[i]);
		}
		this.nameChart = null;
		this.nameAxisX = null;
		this.nameAxisY = null;
		this.subtitle = null;
		this.sourceType = ChartSourceType.COLUMN;
		this.table = table;
		this.minCell = 0;
		this.maxCell = table.getNumRows()-1;
		this.basicConstructor = true;
		this.chartType = ChartType.LINES_AND_POINTS;
		this.gridAxisX = false;
		this.gridAxisY = true;
		this.legend = true;
		this.positionLegend = Position.RIGHT;
	}
	
	/**
	 * Constructor of the chart
	 * @param series Set of series of the chart
	 * @param table Table corresponding to the chart
	 * @param chartSourceType Data series by row or column 
	 * @param minCell Minimum position of the series 
	 * @param maxCell Maximum position of the series 
	 * @throws ChartException
	 */
	public Chart (Serie series[], Table table, ChartSourceType chartSourceType, int minCell, int maxCell) throws ChartException  {
		
		if ((minCell < 0) || (maxCell < 0) || (minCell>maxCell)){ 
			throw new ChartException ("The range of the serie is not valid", new ChartException());
		}
		int numSeries = series.length;
		this.series = new ArrayList <Serie> (numSeries);
		for (int i=0; i<numSeries; i++){
			this.series.add(series[i]);
		}
		this.nameChart = null;
		this.nameAxisX = null;
		this.nameAxisY = null;
		this.subtitle = null;
		this.table = table;
		this.sourceType = chartSourceType;
		this.minCell = minCell;
		this.maxCell = maxCell;
		this.chartType = ChartType.LINES_AND_POINTS;
		this.gridAxisX = false;
		this.gridAxisY = true;
		this.legend = true;
		this.positionLegend = Position.RIGHT;
	}

	/**
	 * @param chartSourceType the chartSourceType to set
	 */
	public void setSourceType(ChartSourceType chartSourceType) {
		if ((this.sourceType == ChartSourceType.COLUMN) && (chartSourceType == ChartSourceType.ROW) && this.basicConstructor){
			this.maxCell = table.getNumColumns()-1;
		}
		this.sourceType = chartSourceType;
	}

	/**
	 * @return the chartSourceType
	 */
	public ChartSourceType getSourceType() {
		return sourceType;
	}

	/**
	 * @param minCell the minCell to set
	 */
	public void setMinCell(int minCell) {
		if ((minCell <0) || (minCell > this.maxCell)){
			throw new ChartException ("The range of the series is not valid", new ChartException());
		}
		this.minCell = minCell;
	}

	/**
	 * @return the minCell
	 */
	public int getMinCell() {
		return minCell;
	}

	/**
	 * @param maxCell the maxCell to set
	 */
	public void setMaxCell(int maxCell) {
		if ((maxCell <0) || (maxCell < this.minCell)){
			throw new ChartException ("The range of the series is not valid", new ChartException());
		}
		this.maxCell = maxCell;
	}

	/**
	 * @return the maxCell
	 */
	public int getMaxCell() {
		return maxCell;
	}

	/**
	 * @param nameAxisX the nameAxisX to set
	 */
	public void setNameAxisX(String nameAxisX) {
		this.nameAxisX = nameAxisX;
	}

	/**
	 * @return the nameAxisX
	 */
	public String getNameAxisX() {
		return nameAxisX;
	}

	/**
	 * @param nameAxisY the nameAxisY to set
	 */
	public void setNameAxisY(String nameAxisY) {
		this.nameAxisY = nameAxisY;
	}

	/**
	 * @return the nameAxisY
	 */
	public String getNameAxisY() {
		return nameAxisY;
	}	
	
	/**
	 * Add a series to the chart
	 * @param series Series to add
	 */
	public void addSeries (Serie series){
		this.series.add(series);
	}
	
	/**
	 * Return the series corresponding to a position 
	 * @param position Position of the series
	 * @return The series
	 * @throws ChartException
	 */
	public Serie getSeries (int position)throws ChartException{
		if ((this.series.size() < position) || (position<0)){
			throw new ChartException ("The series not exists in this position", new ChartException());
		}
		return (Serie) this.series.get(position);
	}
	
	/**
	 * @return The number of series of this chart
	 */
	public int getNumSeries (){
		return this.series.size();
	}

	/**
	 * @param nameChart the nameChart to set
	 */
	public void setNameChart(String nameChart) {
		this.nameChart = nameChart;
	}

	/**
	 * @return the nameChart
	 */
	public String getNameChart() {
		return nameChart;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}


	/**
	 * @param chartType the chartType to set
	 */
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}

	/**
	 * @return the chartType
	 */
	public ChartType getChartType() {
		return chartType;
	}

	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}


	/**
	 * @param gridAxisX the gridAxisX to set
	 */
	public void setGridAxisX(boolean gridAxisX) {
		this.gridAxisX = gridAxisX;
	}


	/**
	 * @return the gridAxisX
	 */
	public boolean hasGridAxisX() {
		return gridAxisX;
	}


	/**
	 * @param gridAxisY the gridAxisY to set
	 */
	public void setGridAxisY(boolean gridAxisY) {
		this.gridAxisY = gridAxisY;
	}


	/**
	 * @return the gridAxisY
	 */
	public boolean hasGridAxisY() {
		return gridAxisY;
	}


	/**
	 * @param legend the legend to set
	 */
	public void setLegend(boolean legend) {
		this.legend = legend;
		// Default position legend
		if (legend){
			this.positionLegend = Position.RIGHT;
		}
	}


	/**
	 * @return the legend
	 */
	public boolean hasLegend() {
		return legend;
	}


	/**
	 * @param positionLegend the positionLegend to set
	 */
	public void setPositionLegend(Position positionLegend) {
		if (this.legend){
			this.positionLegend = positionLegend;
		}
	}


	/**
	 * @return the positionLegend
	 */
	public Position getPositionLegend() {
		return positionLegend;
	}
	
}

