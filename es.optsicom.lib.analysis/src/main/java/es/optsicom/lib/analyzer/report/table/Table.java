package es.optsicom.lib.analyzer.report.table;

import java.util.List;

import es.optsicom.lib.analyzer.report.ReportElement;

/**
 * Esta tabla es un paso intermedio entre las AttributedTable (que permite
 * gestionar valores con atributos) y las SheetTable, que básicamente son
 * ficheros Excel. Esta tabla surge por la necesidad de disponer de una tabla
 * más sencilla que tenga el concepto de valor y título y que se pueda
 * "renderizar" a Excel de una forma sencilla. Además, esta tabla también
 * permitirá fusionar tablas generadas de distinta forma, por ejemplo cuando
 * quieres poner una fila de resumen al final de una tabla.
 * 
 * En esta tabla, los títulos pueden tener varios niveles o atributos, de forma
 * que puedan representarse las AttributedTable sin perder información pero sin
 * la complejidad en la gestión de estas. Es posible incluso que las
 * AttributedTable puedan eliminarse si esta forma de gestionar las tablas de
 * resultados se demuestra más efectiva.
 * 
 * @author Administrador
 * 
 */

public class Table implements ReportElement {

	private List<Title> rowTitles;
	private List<Title> columnTitles;
	private Cell[][] cellValues;
	private Leyend leyend;

	public Table() {
	}

	public Table(List<Title> rowsTitles, List<Title> columnTitles) {
		cellValues = new Cell[rowsTitles.size()][columnTitles.size()];
		this.rowTitles = rowsTitles;
		this.columnTitles = columnTitles;
	}

	public Leyend getLeyend() {
		return leyend;
	}

	public void setLeyend(Leyend leyend) {
		this.leyend = leyend;
	}

	public void setCell(int numRow, int numColumn, Cell cellValue) {
		this.cellValues[numRow][numColumn] = cellValue;
	}

	public Cell getCell(int numRow, int numColumn) {
		return this.cellValues[numRow][numColumn];
	}

	public List<Title> getRowTitles() {
		return rowTitles;
	}

	public List<Title> getColumnTitles() {
		return columnTitles;
	}

	public Cell[][] getCellValues() {
		return cellValues;
	}

	public int getNumRows() {
		return rowTitles.size();
	}

	public int getNumColumns() {
		return columnTitles.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Row Titles: \n");
		for (Title title : rowTitles) {
			sb.append(title).append("\n");
		}
		sb.append("\n");

		sb.append("Column Titles: \n");
		for (Title title : columnTitles) {
			sb.append(title).append("\n");
		}
		sb.append("\n");

		for (int i = 0; i < this.getNumRows(); i++) {

			for (int j = 0; j < this.getNumColumns(); j++) {
				sb.append(
						"Row: " + i + " Col:" + j + " Value:"
								+ this.cellValues[i][j]).append("\n");
			}
		}

		return sb.toString();
	}

	public void addColumn(Title title, Cell[] myCells) {
		throw new UnsupportedOperationException();
	}

	public void addRow(Title title, Cell[] myCells) {
		throw new UnsupportedOperationException();
	}
}
