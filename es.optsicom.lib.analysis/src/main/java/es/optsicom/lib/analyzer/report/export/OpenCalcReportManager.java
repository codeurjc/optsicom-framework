/**
 * Package that includes the methods used to interface to export tables and graphs to 
 * different formats. 
 */
package es.optsicom.lib.analyzer.report.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.star.awt.Rectangle;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.chart.ChartSymbolType;
import com.sun.star.chart.XAxisXSupplier;
import com.sun.star.chart.XAxisYSupplier;
import com.sun.star.chart.XChartDocument;
import com.sun.star.chart.XDiagram;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.document.XEmbeddedObjectSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.Locale;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellHoriJustify;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.table.XTableChart;
import com.sun.star.table.XTableCharts;
import com.sun.star.table.XTableChartsSupplier;
import com.sun.star.table.XTableColumns;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.chart.Chart;
import es.optsicom.lib.analyzer.report.chart.ChartSourceType;
import es.optsicom.lib.analyzer.report.chart.ChartType;
import es.optsicom.lib.analyzer.report.chart.Position;
import es.optsicom.lib.analyzer.report.table.Cell;
import es.optsicom.lib.analyzer.report.table.CellFormat;
import es.optsicom.lib.analyzer.report.table.NumericFormat;
import es.optsicom.lib.analyzer.report.table.Table;

/**
 * @author paco Class that is responsible for export to a physical file our
 *         spreadsheet
 */
public class OpenCalcReportManager {

	/** Path of the document */
	private File path;

	/** Type of export file */
	private FileType fileType;

	/** Number of letters of alphabet */
	private static int NUM_LETTERS = 26;

	private XConnection connection;

	/**
	 * Constructor of CalcExporter, by default the file type is ODS
	 * 
	 * @param path
	 *            Path of the document
	 */
	public OpenCalcReportManager(File path) {
		this.path = path;
		this.fileType = FileType.ODS;
	}

	/**
	 * Constructor of CalcExporter
	 * 
	 * @param path
	 *            Path of the document
	 * @param fileType
	 *            Type of export file
	 */
	public OpenCalcReportManager(File path, FileType fileType) {
		this.path = path;
		this.fileType = fileType;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(File path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public File getPath() {
		return path;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the fileType
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * @param spreadsheetDocument
	 *            The document
	 * @param numberFormatString
	 *            The number format within a string
	 * @return The corresponding number format
	 * @throws ExportException
	 */
	private int getNumberFormat(XSpreadsheetDocument spreadsheetDocument,
			String numberFormatString) throws ExportException {
		if (spreadsheetDocument == null || numberFormatString == null
				|| numberFormatString.trim().equals("")) {
			return 0;
		}
		// Query the number formats supplier of the spreadsheet document
		XNumberFormatsSupplier xNumberFormatsSupplier = (XNumberFormatsSupplier) UnoRuntime
				.queryInterface(XNumberFormatsSupplier.class,
						spreadsheetDocument);

		// Get the number formats from the supplier
		XNumberFormats xNumberFormats = xNumberFormatsSupplier
				.getNumberFormats();

		// Get the default locale
		Locale defaultLocale = new Locale();

		// Check if the number format string already exists
		int numberFormatKey = xNumberFormats.queryKey(numberFormatString,
				defaultLocale, false);

		// If not, add the number format string to number formats collection
		if (numberFormatKey == -1) {
			try {
				numberFormatKey = xNumberFormats.addNew(numberFormatString,
						defaultLocale);
			} catch (com.sun.star.util.MalformedNumberFormatException e) {
				throw new ExportException("Bad number format code  ", e);
			}
		}

		return numberFormatKey;
	}

	/**
	 * Connect to a instance of OpenOffice
	 * 
	 * @return The object that simulate the bridge between server and client
	 * @throws ExportException
	 */
	private XBridge connectToOpenOffice() throws ExportException {

		// Connect to OOo
		XBridge bridge;
		try {
			XComponentContext xRemoteContext = com.sun.star.comp.helper.Bootstrap
					.createInitialComponentContext(null);

			Object x = xRemoteContext
					.getServiceManager()
					.createInstanceWithContext(
							"com.sun.star.connection.Connector", xRemoteContext);

			XConnector xConnector = (XConnector) UnoRuntime.queryInterface(
					XConnector.class, x);
			for (int i = 0;; ++i) {
				try {
					connection = xConnector
							.connect("socket,host=localhost,port=9100");
					break;
				} catch (com.sun.star.connection.NoConnectException ex) {
					// Wait 500 ms, then try to connect again, but do not wait
					// longer than 5 min (= 600 * 500 ms) total:
					if (i == 600) {
						throw new ExportException(
								"Error when connecting to openoffice ", ex);
					}
					Thread.sleep(500);
				}
			}

			x = xRemoteContext.getServiceManager().createInstanceWithContext(
					"com.sun.star.bridge.BridgeFactory", xRemoteContext);
			XBridgeFactory xBridgeFactory = (XBridgeFactory) UnoRuntime
					.queryInterface(XBridgeFactory.class, x);

			// this is the bridge that you will dispose
			bridge = xBridgeFactory.createBridge("", "urp", connection, null);

		} catch (java.lang.Exception e) {
			throw new ExportException("Error when connecting to openoffice ", e);
		}

		return bridge;
	}

	/**
	 * Create a component loader of OpenOffice
	 * 
	 * @param bridge
	 *            The object that simulate the bridge between server and client
	 * @return A component loader
	 * @throws ExportException
	 */
	private XComponentLoader createComponentLoader(XBridge bridge)
			throws ExportException {

		XComponentLoader xComponentLoader;
		try {
			// get the remote instance
			Object x = bridge.getInstance("StarOffice.ServiceManager");

			// Query the initial object for its main factory interface
			XMultiComponentFactory xRemoteServiceManager = (XMultiComponentFactory) UnoRuntime
					.queryInterface(XMultiComponentFactory.class, x);

			// retrieve the component context (it's not yet exported from the
			// office)
			// Query for the XPropertySet interface.
			XPropertySet xProperySet = (XPropertySet) UnoRuntime
					.queryInterface(XPropertySet.class, xRemoteServiceManager);

			// Get the default context from the office server.
			Object oDefaultContext = xProperySet
					.getPropertyValue("DefaultContext");

			// Query for the interface XComponentContext.
			XComponentContext xOfficeComponentContext = (XComponentContext) UnoRuntime
					.queryInterface(XComponentContext.class, oDefaultContext);
			// now create the desktop service
			// NOTE: use the office component context here !
			Object oDesktop = xRemoteServiceManager.createInstanceWithContext(
					"com.sun.star.frame.Desktop", xOfficeComponentContext);

			xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
					XComponentLoader.class, oDesktop);
		} catch (UnknownPropertyException e) {
			throw new ExportException("Error when connecting to openoffice ", e);
		} catch (WrappedTargetException e) {
			throw new ExportException("Error when connecting to openoffice ", e);
		} catch (Exception e) {
			throw new ExportException("Error when connecting to openoffice ", e);
		}
		return xComponentLoader;
	}

	/**
	 * Load the spreadsheetComponent
	 * 
	 * @param xComponentLoader
	 * @param path
	 *            The path where the document is load
	 * @return The spreadsheet component
	 * @throws ExportException
	 */
	private XComponent loadSpreadsheetComponent(
			XComponentLoader xComponentLoader, File path)
			throws ExportException {

		// Hide the window

		PropertyValue[] loadProps = new PropertyValue[1];
		loadProps[0] = new PropertyValue();
		loadProps[0].Name = "Hidden";
		loadProps[0].Value = true;

		// Check if the file exists or not, then open the document or create a
		// new

		XComponent xSpreadsheetComponent = null;

		try {
			xSpreadsheetComponent = xComponentLoader.loadComponentFromURL(
					"private:factory/scalc", "_blank", 0, loadProps);

		} catch (Exception e) {
			throw new ExportException("Error to load the spreadsheet", e);
		}

		return xSpreadsheetComponent;

	}

	/**
	 * Generate the letters (in the OoO) corresponding to the number of column
	 * 
	 * @param numLetter
	 *            The number of column of the OpenCalc
	 * @return The letters of the OpenCalc
	 */
	private String generatorLetters(int numLetter) {
		char letter = 'A';
		String letters = null;
		if (numLetter <= NUM_LETTERS) {
			numLetter--;
			letter = (char) ('A' + numLetter);
			letters = letter + "";
		} else if (numLetter > NUM_LETTERS) {
			int numFirstLetter;
			int numSecondLetter;
			if (numLetter % NUM_LETTERS == 0) {
				numFirstLetter = (numLetter / NUM_LETTERS) - 2;
				numSecondLetter = 25;
			} else {
				numFirstLetter = (numLetter / NUM_LETTERS) - 1;
				numSecondLetter = (numLetter % NUM_LETTERS) - 1;
			}
			char firstLetter = (char) ('A' + numFirstLetter);
			char secondLetter = (char) ('A' + numSecondLetter);
			letters = Character.toString(firstLetter)
					+ Character.toString(secondLetter);
		}
		return letters;
	}

	/**
	 * Fill the table with the data
	 * 
	 * @param xSpreadsheet
	 *            The sheet where is located the table
	 * @param xSpreadsheetDocument
	 *            The document where is located the table
	 * @param table
	 *            Object of the class Table
	 * @param numRows
	 *            Number of rows of the table
	 * @param numColumns
	 *            Number of columns of the table
	 * @return The spreadsheet where is located the table
	 * @throws ExportException
	 */
	private XSpreadsheet fillTheTable(XSpreadsheet xSpreadsheet,
			XSpreadsheetDocument xSpreadsheetDocument, Table table,
			int numRows, int numColumns) throws ExportException {

		try {
			// Catch the range of the table

			XCellRange xCellRange = xSpreadsheet.getCellRangeByPosition(1, 1,
					numColumns, numRows);
			for (int x = 0; x < numRows; x++) {

				for (int y = 0; y < numColumns; y++) {
					Cell cell = table.getCell(x, y);
					XCell xCell = xCellRange.getCellByPosition(y, x);

					// Set the format to the cell

					XPropertySet xCellProp = (com.sun.star.beans.XPropertySet) UnoRuntime
							.queryInterface(
									com.sun.star.beans.XPropertySet.class,
									xCell);
					xCellProp.setPropertyValue("HoriJustify",
							CellHoriJustify.CENTER);

					if (cell != null) {
						
						CellFormat format = cell.getFormat();

						if (format instanceof NumericFormat) {

							NumericFormat nFormat = (NumericFormat) format;

							if (nFormat.getType() == NumericFormat.NumberType.INTEGER) {
								int numberFormatKey = getNumberFormat(
										xSpreadsheetDocument, "0");
								xCellProp.setPropertyValue("NumberFormat",
										new Integer(numberFormatKey));
							} else if (nFormat.getType() == NumericFormat.NumberType.PERCENT) {

								String numZeroes = createNumZeroes(nFormat
										.getNumDecimals());

								int numberFormatKey = getNumberFormat(
										xSpreadsheetDocument, "0," + numZeroes
												+ "%");
								xCellProp.setPropertyValue("NumberFormat",
										new Integer(numberFormatKey));
							} else if (nFormat.getType() == NumericFormat.NumberType.DECIMAL) {

								String numZeroes = createNumZeroes(nFormat
										.getNumDecimals());

								int numberFormatKey = getNumberFormat(
										xSpreadsheetDocument, "0," + numZeroes);
								xCellProp.setPropertyValue("NumberFormat",
										new Integer(numberFormatKey));
							}
						}

						// Differentiate between NumericalCell and FormulaCell

						if (cell.getValue() instanceof Number) {

							// Set the value to cell
							xCell.setValue(((Number) cell.getValue())
									.doubleValue());

						} // End if NumericalCell
						// else {
						// // Set the formula in the cell
						//
						// FormulaCell formCell = (FormulaCell) cell;
						// if (formCell.getRange() != null) {
						// String formulaString = "=";
						//
						// // Set the operation
						//
						// if (formCell.operation == Operation.SUM) {
						// formulaString += "sum(";
						// } else if (formCell.operation == Operation.AVERAGE) {
						// formulaString += "average(";
						// }
						//
						// // Set the range of the operations
						//
						// int startRow = formCell.getRange().getStart()
						// .getRow() + 2;
						// int startColumn = formCell.getRange().getStart()
						// .getColumn() + 2;
						// int endRow = formCell.getRange().getEnd().getRow() +
						// 2;
						// int endColumn = formCell.getRange().getEnd()
						// .getColumn() + 2;
						//
						// formulaString += generatorLetters(startColumn)
						// + String.valueOf(startRow);
						// formulaString += ":" + generatorLetters(endColumn)
						// + String.valueOf(endRow) + ")";
						//
						// // Set the formula to the cell
						//
						// xCell.setFormula(formulaString);
						// }
						//
						// }// End FormulaCell

					}

				} // End for internal
			} // End for external
		} // End try
		catch (IndexOutOfBoundsException e) {
			throw new ExportException(" Error to fill the table", e);
		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to fill the table", e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to fill the table", e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to fill the table", e);
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to fill the table", e);
		}

		return xSpreadsheet;
	}

	private String createNumZeroes(int numZeroes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numZeroes; i++) {
			sb.append("0");
		}
		return sb.toString();
	}

	/**
	 * Set the labels of the table
	 * 
	 * @param xSpreadsheet
	 *            The sheet where is located the table
	 * @param table
	 *            Object of the class Table
	 * @param numRows
	 *            Number of rows of the table
	 * @param numColumns
	 *            Number of columns of the table
	 * @param xSpreadsheetDocument
	 *            The document
	 * @return The spreadsheet where is located the table
	 * @throws ExportException
	 */
	private XSpreadsheet writeTitles(XSpreadsheet xSpreadsheet, Table table,
			int numRows, int numColumns,
			XSpreadsheetDocument xSpreadsheetDocument) throws ExportException {

		try {
			// Put the labels of the columns

			XCellRange xColumnsNames = xSpreadsheet.getCellRangeByPosition(1,
					0, numColumns, 0);

			for (int i = 0; i < numColumns; i++) {
				XCell columnName = xColumnsNames.getCellByPosition(i, 0);

				// Set the text format
				int numberFormatKey = getNumberFormat(xSpreadsheetDocument, "@");
				XPropertySet xCellProp = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, xColumnsNames);
				xCellProp.setPropertyValue("NumberFormat", new Integer(
						numberFormatKey));

				// TODO Actualmente sólo soporta tablas con títulos simples
				String columnTitle = table.getColumnTitles().get(i)
						.getAttributes().get(0).getTitle();
				if (columnTitle != null) {
					XText xCellText = (XText) UnoRuntime.queryInterface(
							XText.class, columnName);
					XTextCursor xTextCursor = xCellText.createTextCursor();
					xCellText.insertString(xTextCursor, columnTitle, false);
					XPropertySet xPropSetCell = (XPropertySet) UnoRuntime
							.queryInterface(XPropertySet.class, xCellText);
					xPropSetCell.setPropertyValue("CharWeight", new Float(
							com.sun.star.awt.FontWeight.BOLD));
				}
			}

			// Put the labels of the rows

			XCellRange xRowsNames = xSpreadsheet.getCellRangeByPosition(0, 1,
					0, numRows);

			for (int l = 0; l < numRows; l++) {
				XCell nameRow = xRowsNames.getCellByPosition(0, l);

				// Set the text format
				int numberFormatKey = getNumberFormat(xSpreadsheetDocument, "@");
				XPropertySet xCellProp = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, xRowsNames);
				xCellProp.setPropertyValue("NumberFormat", new Integer(
						numberFormatKey));

				// TODO Actualmente sólo soporta tablas con títulos simples
				String rowTitle = table.getRowTitles().get(l).getAttributes()
						.get(0).getTitle();

				if (rowTitle != null) {
					XText xCellText = (XText) UnoRuntime.queryInterface(
							XText.class, nameRow);
					XTextCursor xTextCursor = xCellText.createTextCursor();
					xCellText.insertString(xTextCursor, rowTitle, false);
					XPropertySet xPropSetCell = (XPropertySet) UnoRuntime
							.queryInterface(XPropertySet.class, xCellText);
					xPropSetCell.setPropertyValue("CharWeight", new Float(
							com.sun.star.awt.FontWeight.BOLD));
				}
			}

			// Adjust column widths
			for (int j = 0; j <= numColumns; j++) {
				// Get columns
				XColumnRowRange columnRowRange = (XColumnRowRange) UnoRuntime
						.queryInterface(XColumnRowRange.class, xSpreadsheet);
				XTableColumns columns = columnRowRange.getColumns();

				Object column = columns.getByIndex(j);

				XPropertySet columnProp = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, column);
				columnProp.setPropertyValue("OptimalWidth", new Boolean(true));
			}
		} // End try
		catch (IndexOutOfBoundsException e) {
			throw new ExportException(" Error to put the labels of the table",
					e);
		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to put the labels of the table",
					e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to put the labels of the table",
					e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to put the labels of the table",
					e);
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to put the labels of the table",
					e);
		}

		return xSpreadsheet;

	}

	/**
	 * @param chart
	 *            A chart
	 * @return true, if all series on the chart begin by zero(the first element
	 *         of the table), false otherwise
	 */
	private boolean isChartEntire(Chart chart) {

		boolean chartEntire = (chart.getMinCell() == 0);
		return chartEntire;
	}

	/**
	 * Function that return the range of a determinate chart
	 * 
	 * @param xSpreadsheet
	 *            The spreadsheet
	 * @param chart
	 *            The chart
	 * @return The range of a chart
	 * @throws ExportException
	 */
	private CellRangeAddress[] rangeOfChart(XSpreadsheet xSpreadsheet,
			Chart chart) throws ExportException {

		int numSeries = chart.getNumSeries() + 1;
		CellRangeAddress[] aAddresses = new CellRangeAddress[numSeries];
		try {
			// take the ranges of the series of charts
			int minRange = chart.getMinCell();
			int maxRange = chart.getMaxCell();
			ChartSourceType chartSourceType = chart.getSourceType();

			// The first series are the labels of the columns of or the rows
			if (chartSourceType == ChartSourceType.COLUMN) {
				XCellRange xRowsNames = xSpreadsheet.getCellRangeByPosition(0,
						minRange + 1, 0, maxRange + 1);
				XCellRangeAddressable xRangeAddr = (XCellRangeAddressable) UnoRuntime
						.queryInterface(XCellRangeAddressable.class, xRowsNames);
				CellRangeAddress aRangeAddress = xRangeAddr.getRangeAddress();
				aAddresses[0] = aRangeAddress;
			} else if (chartSourceType == ChartSourceType.ROW) {
				XCellRange xColumnsNames = xSpreadsheet.getCellRangeByPosition(
						minRange + 1, 0, maxRange + 1, 0);
				XCellRangeAddressable xRangeAddr = (XCellRangeAddressable) UnoRuntime
						.queryInterface(XCellRangeAddressable.class,
								xColumnsNames);
				CellRangeAddress aRangeAddress = xRangeAddr.getRangeAddress();
				aAddresses[0] = aRangeAddress;
			}

			// The remaining series
			for (int i = 1; i < numSeries; i++) {

				int seriesTypePosition = chart.getSeries(i - 1)
						.getSeriesPosition();
				XCellRange xSeriesRange = null;
				if (chartSourceType == ChartSourceType.COLUMN
						&& isChartEntire(chart)) {
					xSeriesRange = xSpreadsheet.getCellRangeByPosition(
							seriesTypePosition + 1, minRange,
							seriesTypePosition + 1, maxRange + 1);
				} else if (chartSourceType == ChartSourceType.COLUMN) {
					xSeriesRange = xSpreadsheet.getCellRangeByPosition(
							seriesTypePosition + 1, minRange + 1,
							seriesTypePosition + 1, maxRange + 1);
				} else if (chartSourceType == ChartSourceType.ROW
						&& isChartEntire(chart)) {
					xSeriesRange = xSpreadsheet.getCellRangeByPosition(
							minRange, seriesTypePosition + 1, maxRange + 1,
							seriesTypePosition + 1);
				} else if (chartSourceType == ChartSourceType.ROW) {
					xSeriesRange = xSpreadsheet.getCellRangeByPosition(
							minRange + 1, seriesTypePosition + 1, maxRange + 1,
							seriesTypePosition + 1);
				}

				// also get an array of CellRangeAddresses containing
				// the data you want to visualize and store them in aAddresses

				XCellRangeAddressable xRangeAddr = (XCellRangeAddressable) UnoRuntime
						.queryInterface(XCellRangeAddressable.class,
								xSeriesRange);
				CellRangeAddress aRangeAddress = xRangeAddr.getRangeAddress();
				aAddresses[i] = aRangeAddress;
			}
		} catch (IndexOutOfBoundsException e) {
			throw new ExportException(
					" Error to define the range of the chart", e);
		}

		return aAddresses;
	}

	/**
	 * Create a chart embedded in the sheet
	 * 
	 * @param aChartCollection
	 *            The collection of charts
	 * @param aChartCollectionNA
	 *            The collection of name of the charts
	 * @param sChartName
	 *            The name of the chart
	 * @param numCharts
	 *            The number of charts that will be the sheet
	 * @param aAddresses
	 *            The range of the cells of the chart
	 * @param chart
	 *            The chart
	 * @param numRows
	 *            The number of rows of the table
	 * @return A sheet document with the chart created
	 * @throws ExportException
	 */
	private XChartDocument createAChart(XTableCharts aChartCollection,
			XNameAccess aChartCollectionNA, String sChartName, int numCharts,
			CellRangeAddress[] aAddresses, Chart chart, int numRows)
			throws ExportException {

		XChartDocument aChartDocument;
		try {
			// following rectangle parameters are measured in 1/100 mm
			Rectangle aRect = new com.sun.star.awt.Rectangle(200, 500
					* (numRows + 1) + 10000 * (numCharts), 15000, 9270);

			ChartSourceType chartSourceType = chart.getSourceType();
			if (isChartEntire(chart)) {
				// first bool: ColumnHeaders
				// second bool: RowHeaders
				aChartCollection.addNewByName(sChartName, aRect, aAddresses,
						true, true);
			} else if (chartSourceType == ChartSourceType.COLUMN) {
				aChartCollection.addNewByName(sChartName, aRect, aAddresses,
						false, true);
			} else if (chartSourceType == ChartSourceType.ROW) {
				aChartCollection.addNewByName(sChartName, aRect, aAddresses,
						true, false);
			}

			XTableChart aTableChart = (com.sun.star.table.XTableChart) UnoRuntime
					.queryInterface(com.sun.star.table.XTableChart.class,
							aChartCollectionNA.getByName(sChartName));

			// the table chart is an embedded object which contains the chart
			// document
			aChartDocument = (XChartDocument) UnoRuntime.queryInterface(
					XChartDocument.class, ((XEmbeddedObjectSupplier) UnoRuntime
							.queryInterface(XEmbeddedObjectSupplier.class,
									aTableChart)).getEmbeddedObject());
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to create a chart", e);
		} catch (NoSuchElementException e) {
			throw new ExportException(" Error to create a chart", e);
		}
		return aChartDocument;

	}

	/**
	 * Set the title to the chart
	 * 
	 * @param aChartDocument
	 *            The chart document
	 * @param chart
	 *            The chart
	 * @return The chart with the title
	 * @throws ExportException
	 */
	private XChartDocument setTitle(XChartDocument aChartDocument, Chart chart)
			throws ExportException {

		try {

			if (chart.getNameChart() != null) {
				XPropertySet pSet = (XPropertySet) UnoRuntime.queryInterface(
						XPropertySet.class, aChartDocument);
				pSet.setPropertyValue("HasMainTitle", new Boolean(true));
				XShape shapeTitle = aChartDocument.getTitle();
				pSet = (XPropertySet) UnoRuntime.queryInterface(
						XPropertySet.class, shapeTitle);
				pSet.setPropertyValue("String", chart.getNameChart());
			}
			if (chart.getSubtitle() != null) {
				XPropertySet pSet = (XPropertySet) UnoRuntime.queryInterface(
						XPropertySet.class, aChartDocument);
				pSet.setPropertyValue("HasSubTitle", new Boolean(true));
				XShape shapeSubtitle = aChartDocument.getSubTitle();
				pSet = (XPropertySet) UnoRuntime.queryInterface(
						XPropertySet.class, shapeSubtitle);
				pSet.setPropertyValue("String", chart.getSubtitle());
			}
		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to set the title to the chart", e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to set the title to the chart", e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to set the title to the chart", e);
		} catch (WrappedTargetException e) {
			throw new ExportException("Error to set the title to the chart", e);
		}

		return aChartDocument;
	}

	/**
	 * Specifies if the data series displayed in the chart, take their values
	 * from the row or the column
	 * 
	 * @param aDiagram
	 *            Diagram of chart
	 * @param chart
	 *            The chart
	 * @return The diagram with the data series
	 * @throws ExportException
	 */
	private XDiagram setChartDataSeriesSource(XDiagram aDiagram, Chart chart)
			throws ExportException {

		try {
			XPropertySet pSet = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, aDiagram);
			if (chart.getSourceType() == ChartSourceType.COLUMN) {
				pSet.setPropertyValue("DataRowSource",
						com.sun.star.chart.ChartDataRowSource.COLUMNS);
			} else if (chart.getSourceType() == ChartSourceType.ROW) {
				pSet.setPropertyValue("DataRowSource",
						com.sun.star.chart.ChartDataRowSource.ROWS);
			}
		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to set the source of the series",
					e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to set the source of the series",
					e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to set the source of the series",
					e);
		} catch (WrappedTargetException e) {
			throw new ExportException("Error to set the source of the series",
					e);
		}
		return aDiagram;

	}

	/**
	 * Set the title to the axis X and to the axis Y
	 * 
	 * @param aDiagram
	 *            Diagram of chart
	 * @param chart
	 *            The chart
	 * @return The diagram with the titles of the axes
	 * @throws ExportException
	 */
	private XDiagram setAxesTitles(XDiagram aDiagram, Chart chart)
			throws ExportException {

		try {
			// Put the title of x axis
			if (chart.getNameAxisX() != null) {
				XAxisXSupplier xAxis = (com.sun.star.chart.XAxisXSupplier) UnoRuntime
						.queryInterface(
								com.sun.star.chart.XAxisXSupplier.class,
								aDiagram);

				XPropertySet xAxisProp = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, xAxis);
				xAxisProp.setPropertyValue("HasXAxisTitle", new Boolean(true));

				if (xAxis != null) {
					XShape xAxisShape = (XShape) xAxis.getXAxisTitle();
					XPropertySet xAxisTitleProp = (XPropertySet) UnoRuntime
							.queryInterface(XPropertySet.class, xAxisShape);
					xAxisTitleProp.setPropertyValue("String",
							chart.getNameAxisX());
				}
			}

			// Put the title of y axis
			if (chart.getNameAxisY() != null) {
				XAxisYSupplier yAxis = (com.sun.star.chart.XAxisYSupplier) UnoRuntime
						.queryInterface(
								com.sun.star.chart.XAxisYSupplier.class,
								aDiagram);

				XPropertySet yAxisProp = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, yAxis);
				yAxisProp.setPropertyValue("HasYAxisTitle", new Boolean(true));

				if (yAxis != null) {
					XShape yAxisShape = (XShape) yAxis.getYAxisTitle();
					XPropertySet yAxisTitleProp = (XPropertySet) UnoRuntime
							.queryInterface(XPropertySet.class, yAxisShape);
					yAxisTitleProp.setPropertyValue("String",
							chart.getNameAxisY());
				}
			}
		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to set the name to the axes", e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to set the name to the axes", e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to set the name to the axes", e);
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to set the name to the axes", e);
		}

		return aDiagram;
	}

	/**
	 * Set the type of line of the chart
	 * 
	 * @param aDiagram
	 *            Diagram of the chart
	 * @param chart
	 *            The chart
	 * @return The diagram with the type of chart
	 * @throws ExportException
	 */
	private XDiagram setChartType(XDiagram aDiagram, Chart chart)
			throws ExportException {
		try {
			XPropertySet pSet = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, aDiagram);
			if (chart.getChartType() == ChartType.LINES_3D) {
				pSet.setPropertyValue("Dim3D", true);
			}
			if (chart.getChartType() == ChartType.LINES) {
				pSet.setPropertyValue("SymbolType", ChartSymbolType.NONE);
				pSet.setPropertyValue("Lines", true);
			} else if (chart.getChartType() == ChartType.POINTS) {
				pSet.setPropertyValue("SymbolType", ChartSymbolType.AUTO);
				pSet.setPropertyValue("Lines", false);
			} else if (chart.getChartType() == ChartType.LINES_AND_POINTS) {
				pSet.setPropertyValue("SymbolType", ChartSymbolType.AUTO);
				pSet.setPropertyValue("Lines", true);
			}

		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to set the type of the chart", e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to set the type of the chart", e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to set the type of the chart", e);
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to set the type of the chart", e);
		}

		return aDiagram;
	}

	/**
	 * Set the grid axis to the chart
	 * 
	 * @param aDiagram
	 *            Diagram of the chart
	 * @param chart
	 *            The chart
	 * @return The diagram with the grid axis
	 * @throws ExportException
	 */
	private XDiagram setGridAxis(XDiagram aDiagram, Chart chart)
			throws ExportException {
		try {
			XPropertySet pSet = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, aDiagram);
			if (chart.hasGridAxisX()) {
				pSet.setPropertyValue("HasXAxisGrid", true);
			} else {
				pSet.setPropertyValue("HasXAxisGrid", false);
			}
			if (chart.hasGridAxisY()) {
				pSet.setPropertyValue("HasYAxisGrid", true);
			} else {
				pSet.setPropertyValue("HasYAxisGrid", false);
			}
		} catch (UnknownPropertyException e) {
			throw new ExportException(
					" Error to set the type of the grid axis of the chart", e);
		} catch (PropertyVetoException e) {
			throw new ExportException(
					" Error to set the type of the grid axis of the chart", e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(
					" Error to set the type of the grid axis of the chart", e);
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to set the type of the chart", e);
		}

		return aDiagram;
	}

	/**
	 * Set the legend of the chart
	 * 
	 * @param aDiagram
	 *            Diagram of the chart
	 * @param chart
	 *            The chart
	 * @return The diagram with or without the legend
	 * @throws ExportException
	 */
	private XChartDocument setLegend(XChartDocument aChartDocument, Chart chart)
			throws ExportException {
		try {
			XShape shapeLegend = aChartDocument.getLegend();
			XPropertySet pSet = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, shapeLegend);
			if (chart.hasLegend()) {
				if (chart.getPositionLegend() == Position.RIGHT) {
					pSet.setPropertyValue("Alignment",
							com.sun.star.chart.ChartLegendPosition.RIGHT);
				} else if (chart.getPositionLegend() == Position.BOTTOM) {
					pSet.setPropertyValue("Alignment",
							com.sun.star.chart.ChartLegendPosition.BOTTOM);
				} else if (chart.getPositionLegend() == Position.LEFT) {
					pSet.setPropertyValue("Alignment",
							com.sun.star.chart.ChartLegendPosition.LEFT);
				} else if (chart.getPositionLegend() == Position.TOP) {
					pSet.setPropertyValue("Alignment",
							com.sun.star.chart.ChartLegendPosition.TOP);
				}
			} else {
				pSet.setPropertyValue("Alignment",
						com.sun.star.chart.ChartLegendPosition.NONE);
			}
		} catch (UnknownPropertyException e) {
			throw new ExportException(" Error to set the legend", e);
		} catch (PropertyVetoException e) {
			throw new ExportException(" Error to set the legend", e);
		} catch (IllegalArgumentException e) {
			throw new ExportException(" Error to set the legend", e);
		} catch (WrappedTargetException e) {
			throw new ExportException(" Error to set the legend", e);
		}
		return aChartDocument;
	}

	/**
	 * Store the spreadsheet in a file type
	 * 
	 * @param fileType
	 *            Type of file to export
	 * @param xSpreadsheetComponent
	 *            The spreadsheet component
	 * @param path
	 *            Place where store the file
	 * @throws ExportException
	 */
	private void storeSpreadSheet(FileType fileType,
			XComponent xSpreadsheetComponent, File filePath)
			throws ExportException {

		String path = filePath.getAbsolutePath();
		
		try {
			if (fileType == FileType.ODS) {
				// Store as OpenCalc

				XStorable xStorable = (XStorable) UnoRuntime.queryInterface(
						XStorable.class, xSpreadsheetComponent);
				PropertyValue[] storeProps = new PropertyValue[0];

				// In the case that the extension of the file is unknown, we add
				// the extension
				if (!(path.endsWith(".ods") || path.endsWith(".ODS"))) {
					path = path.concat(".ods");
				}
				xStorable.storeToURL("file:///" + path, storeProps);

			} else if (fileType == FileType.EXCEL) {
				// Store as Excel

				XStorable xStorableExcel = (XStorable) UnoRuntime
						.queryInterface(XStorable.class, xSpreadsheetComponent);
				PropertyValue[] storeExcelProps = new PropertyValue[2];
				storeExcelProps[0] = new PropertyValue();
				storeExcelProps[0].Name = "FilterName";
				storeExcelProps[0].Value = "MS Excel 97";
				storeExcelProps[1] = new PropertyValue();
				storeExcelProps[1].Name = "Overwrite";
				storeExcelProps[1].Value = true;

				// In the case that the extension of the file is unknown, we add
				// the extension
				if (!(path.endsWith(".xls") || path.endsWith(".XLS"))) {
					path = path.concat(".xls");
				}
				xStorableExcel.storeToURL("file:///" + path, storeExcelProps);
				
			} else if (fileType == FileType.EXCEL2007) {
				// Store as Excel

				XStorable xStorableExcel = (XStorable) UnoRuntime
						.queryInterface(XStorable.class, xSpreadsheetComponent);
				PropertyValue[] storeExcelProps = new PropertyValue[2];
				storeExcelProps[0] = new PropertyValue();
				storeExcelProps[0].Name = "FilterName";
				storeExcelProps[0].Value = "Calc MS Excel 2007 XML";
				storeExcelProps[1] = new PropertyValue();
				storeExcelProps[1].Name = "Overwrite";
				storeExcelProps[1].Value = true;

				// In the case that the extension of the file is unknown, we add
				// the extension
				if (!(path.endsWith(".xlsx") || path.endsWith(".XLSX"))) {
					path = path.concat(".xlsx");
				}
				xStorableExcel.storeToURL("file:///" + path, storeExcelProps);

			} else if (fileType == FileType.PDF) {
				// Store as PDF

				XStorable xStorablePDF = (XStorable) UnoRuntime.queryInterface(
						XStorable.class, xSpreadsheetComponent);
				PropertyValue[] storePDFProps = new PropertyValue[1];
				storePDFProps[0] = new PropertyValue();
				storePDFProps[0].Name = "FilterName";
				storePDFProps[0].Value = "calc_pdf_Export";

				// Print only a range of the cells
				/*
				 * storePDFProps[1].Name = "FilterData"; PropertyValue[]
				 * filterData = new PropertyValue[1]; filterData[0] = new
				 * PropertyValue(); filterData[0].Name = "Selection";
				 * filterData[0].Value = xCellRange; storePDFProps[1].Value =
				 * filterData;
				 */

				// In the case that the extension of the file is unknown, we add
				// the extension
				if (!(path.endsWith(".pdf") || path.endsWith(".PDF"))) {
					path = path.concat(".pdf");
				}
				xStorablePDF.storeToURL("file:///" + path, storePDFProps);

			} else if (fileType == FileType.HTML) {
				// Store as PDF

				XStorable xStorableHTML = (XStorable) UnoRuntime
						.queryInterface(XStorable.class, xSpreadsheetComponent);
				PropertyValue[] storeHTMLProps = new PropertyValue[1];
				storeHTMLProps[0] = new PropertyValue();
				storeHTMLProps[0].Name = "FilterName";
				storeHTMLProps[0].Value = "HTML (StarCalc)";

				// In the case that the extension of the file is unknown, we add
				// the extension
				if (!(path.endsWith(".html") || path.endsWith(".HTML"))) {
					path = path.concat(".html");
				}

				xStorableHTML.storeToURL("file:///" + path, storeHTMLProps);
			}
		} catch (IOException e) {
			throw new ExportException(" Error to store the spreadsheet", e);
		}

	}

	/**
	 * Close the document correctly
	 * 
	 * @param xSpreadsheetDocument
	 *            The spreadsheet document
	 */
	private void closeDocument(XSpreadsheetDocument xSpreadsheetDocument) {

		XCloseable xcloseable = (XCloseable) UnoRuntime.queryInterface(
				XCloseable.class, xSpreadsheetDocument);
		try {
			xcloseable.close(true);
		} catch (CloseVetoException e) {
			throw new ExportException(" Error to close the document", e);
		}
	}

	/**
	 * Export the CalcFile to the file on the file system
	 * 
	 * @param calcFile
	 *            the calc file
	 */
	public void exportToFile(Report calcFile) {

		// Start OOo
		Process process;
		try {
			process = Runtime
					.getRuntime()
					.exec("soffice -headless -accept=socket,host=localhost,port=9100;urp,Negotiate=0,ForceSynchronous=0;");
						
		} catch (java.io.IOException e) {
			throw new ExportException(" Error to execute OpenOffice", e);
		}

		// Connect to OoO
		XBridge bridge = connectToOpenOffice();
		XComponentLoader xComponentLoader = createComponentLoader(bridge);

		// Load the spreadsheet component
		XComponent xSpreadsheetComponent = loadSpreadsheetComponent(
				xComponentLoader, this.path);

		// Access to the document interface
		XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
				.queryInterface(XSpreadsheetDocument.class,
						xSpreadsheetComponent);

		// Access to the sheets of document
		XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();

		createCalcDocument(calcFile, xSpreadsheetDocument, xSpreadsheets);

		storeSpreadSheet(this.fileType, xSpreadsheetComponent, this.path);

		// Close the document

		closeDocument(xSpreadsheetDocument);
		
		// Closing the bridge (the client)
		XComponent xcomponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, bridge);
		xcomponent.dispose();
		
		// Destroy the process
		process.destroy();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void createCalcDocument(Report calcFile,
			XSpreadsheetDocument xSpreadsheetDocument,
			XSpreadsheets xSpreadsheets) {
		int totalNumChart = 0;
		
		List<ReportPage> pages = new ArrayList<ReportPage>();
		for(ReportBlock block : calcFile.getReportBlocks()) {
			pages.addAll(block.getReportPages());
		}
		
		// Traveling the sheets to insert
		for (int j = 0; j < pages.size(); j++) {

			// Insert a sheet
			xSpreadsheets.insertNewByName(pages.get(j).getName(),
					(short) j);

			// Access to the previously inserted sheet
			Object sheet;
			try {
				sheet = xSpreadsheets.getByName(pages.get(j)
						.getName());
			} catch (Exception e) {
				throw new ExportException(" Error to access to the sheet", e);
			}
			XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
					.queryInterface(XSpreadsheet.class, sheet);

			// Find the table of this sheet
			for(ReportElement reportElement : pages.get(j).getReportElements()) {
				if(reportElement instanceof Table) {
					addTable((Table)reportElement, xSpreadsheet, xSpreadsheetDocument);
				}
			}

			for(ReportElement reportElement : pages.get(j).getReportElements()) {
				if(reportElement instanceof Chart) {
					
			// ************* CREATE THE CHARTS
			// ************************************************************* //


				final String sChartName = "MyChart"
						+ String.valueOf(totalNumChart);

				// get the sheet in which you want to insert a chart
				// and query it for XTableChartsSupplier and store it in aSheet
				XTableChartsSupplier aSheet = (XTableChartsSupplier) UnoRuntime
						.queryInterface(XTableChartsSupplier.class, sheet);

				// Get the chart
				Chart chart = (Chart) reportElement;

				// take the ranges of the series of charts
				CellRangeAddress[] aAddresses = rangeOfChart(xSpreadsheet,
						chart);

				XTableCharts aChartCollection = aSheet.getCharts();
				XNameAccess aChartCollectionNA = (XNameAccess) UnoRuntime
						.queryInterface(XNameAccess.class, aChartCollection);

				// only insert the chart if it does not already exist
				if (aChartCollectionNA != null
						&& !aChartCollectionNA.hasByName(sChartName)) {

					// Create a chart embedded in the document
					XChartDocument aChartDocument = createAChart(
							aChartCollection, aChartCollectionNA, sChartName,
							totalNumChart, aAddresses, chart, 5);

					// Set the title of the chart
					aChartDocument = setTitle(aChartDocument, chart);

					// Set the legend
					aChartDocument = setLegend(aChartDocument, chart);

					// Change the chart type to the line chart
					// get the factory that can create diagrams

					XMultiServiceFactory aFact = (XMultiServiceFactory) UnoRuntime
							.queryInterface(XMultiServiceFactory.class,
									aChartDocument);

					XDiagram aDiagram;
					try {
						aDiagram = (XDiagram) UnoRuntime
								.queryInterface(
										XDiagram.class,
										aFact.createInstance("com.sun.star.chart.LineDiagram"));
					} catch (Exception e) {
						throw new ExportException(
								" Error to create the line diagram", e);
					}
					// Set the type of chart
					aDiagram = setChartType(aDiagram, chart);

					// Specifies if the data series displayed in the chart, take
					// their values from the row or the column in the underlying
					// data source
					aDiagram = setChartDataSeriesSource(aDiagram, chart);

					// Set the title of axes
					aDiagram = setAxesTitles(aDiagram, chart);

					// Set the grid Axis
					aDiagram = setGridAxis(aDiagram, chart);

					// Put the new diagram in a chart document
					aChartDocument.setDiagram(aDiagram);
				}// End if
				totalNumChart++;
				}
			}// End for (traveling the charts)
		} // End for (traveling the sheets)

		// Store the spreadsheet in the specified format
	}
	
	private void addTable(Table table, XSpreadsheet xSpreadsheet, XSpreadsheetDocument xSpreadsheetDocument) {
		// The number of rows and columns of the table
		int numRows = table.getNumRows();
		int numColumns = table.getNumColumns();

		// Fill the table with the data
		xSpreadsheet = fillTheTable(xSpreadsheet, xSpreadsheetDocument,
				table, numRows, numColumns);

		// Put the labels of the table
		xSpreadsheet = writeTitles(xSpreadsheet, table, numRows,
				numColumns, xSpreadsheetDocument);

	}

}
