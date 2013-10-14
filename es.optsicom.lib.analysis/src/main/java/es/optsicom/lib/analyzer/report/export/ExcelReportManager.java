package es.optsicom.lib.analyzer.report.export;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.Cell;
import es.optsicom.lib.analyzer.report.table.CellFormat;
import es.optsicom.lib.analyzer.report.table.Leyend;
import es.optsicom.lib.analyzer.report.table.LeyendElement;
import es.optsicom.lib.analyzer.report.table.NumericFormat;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.report.table.Title;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.util.Strings;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.Properties;

public class ExcelReportManager {

	private Workbook wb;
	private CreationHelper createHelper;
	private CellStyle defaultStyle;
	private Font boldFont;

	public void generateExcelFile(Report report, File file) {

		wb = new XSSFWorkbook();
		createHelper = wb.getCreationHelper();

		boldFont = wb.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		for (ReportBlock reportBlock : report.getReportBlocks()) {
			for(ReportPage reportPage : reportBlock.getReportPages()) {
				createReportPage(reportPage);
			}
		}

		try {
			saveToExcelFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			wb = null;
			createHelper = null;
		}
	}

	private void createReportPage(ReportPage reportPage) {

		Sheet sheet = wb.createSheet(reportPage.getName());

		int col = 1;
		for (ReportElement element : reportPage.getReportElements()) {

			if (element instanceof Table) {
				Table table = (Table) element;
				col += addTable(table, sheet, 1, col);
			}
		}

	}

	private int addTable(Table table, Sheet sheet, int fromRow, int fromCol) {

		int numRowsForColsTitles = table.getColumnTitles().get(0)
				.getAttributes().size();
		int numColsForRowsTitles = table.getRowTitles().get(0).getAttributes()
				.size();

		writeColTitles(table, sheet, fromRow, fromCol, numRowsForColsTitles,
				numColsForRowsTitles);
		writeRowTitles(table, sheet, fromRow, fromCol, numRowsForColsTitles,
				numColsForRowsTitles);

		int rowBase = fromRow + numRowsForColsTitles;
		int colBase = fromCol + numColsForRowsTitles;

		writeCells(table, sheet, rowBase, colBase);

		setHorizontalBorder(sheet, rowBase, rowBase + table.getNumRows() - 1,
				fromCol, colBase + table.getNumColumns());

		int toCol = fromCol + numColsForRowsTitles + table.getNumColumns();

		adjustCellWidth(sheet, fromCol, toCol);

//		writeLeyend(table, sheet, fromRow + table.getNumRows()
//				+ numRowsForColsTitles + 2, fromCol);

		return table.getNumColumns() + numColsForRowsTitles + 1;
	}

	private void writeLeyend(Table table, Sheet sheet, int fromRow, int fromCol) {

		Leyend leyend = table.getLeyend();

		int numRow = fromRow;
		for (LeyendElement element : leyend.getLeyendElements()) {

			org.apache.poi.ss.usermodel.Cell cell = getCell(sheet, numRow,
					fromCol);
			cell.setCellValue(element.getTitle().toUpperCase());
			
			CellStyle style = getCellStyleOf(cell);
			style.setFont(boldFont);
			
			numRow += 2;

			for (Attribute att : element.getAttributes()) {

				Object value = att.getValue();				
				
				cell = getCell(sheet, numRow, fromCol);
				cell.setCellValue(att.getTitle());
				style = getCellStyleOf(cell);
				style.setFont(boldFont);
				numRow++;
				
				if (value instanceof Descriptive) {

					Descriptive desc = (Descriptive) value;

					Properties properties = desc.getProperties();

					List<String> props = new ArrayList<String>(
							properties.getMap().keySet());
					
					Collections.sort(props,Strings.getNaturalComparator());

					for (String prop : props) {
						cell = getCell(sheet, numRow, fromCol);
						cell.setCellValue(prop + " = " + properties.get(prop));
						numRow++;
					}

				} else {
					cell = getCell(sheet, numRow, fromCol);
					cell.setCellValue(att.toString());
					numRow++;
				}

				numRow++;
			}

			numRow++;
		}

	}

	private org.apache.poi.ss.usermodel.Cell getCell(Sheet sheet, int numRow,
			int numCol) {
		return getCell(getRow(sheet, numRow), numCol);
	}

	private void adjustCellWidth(Sheet sheet, int fromCol, int toCol) {
		for (int i = fromCol; i < toCol; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, (int) (sheet.getColumnWidth(i) * 1.05));
		}
	}

	private void writeCells(Table table, Sheet sheet, int rowBase, int colBase) {

		DataFormat format = wb.createDataFormat();

		for (int i = 0; i < table.getNumRows(); i++) {

			// Create a row and put some cells in it. Rows are 0 based.
			Row row = getRow(sheet, i + rowBase);

			for (int j = 0; j < table.getNumColumns(); j++) {

				Cell optsicomCell = table.getCell(i, j);

				if (optsicomCell != null) {

					org.apache.poi.ss.usermodel.Cell cell = getCell(row, j
							+ colBase);

					setValueToCell(cell, optsicomCell);
					setFormatToCell(format, cell, optsicomCell);
					setColorToCell(cell, optsicomCell);

				}
			}
		}
	}

	private void setValueToCell(org.apache.poi.ss.usermodel.Cell cell,
			Cell optsicomCell) {
		Object value = optsicomCell.getValue();

		if (value instanceof String) {
			cell.setCellValue(createHelper.createRichTextString((String) value));
		} else if (value instanceof Double) {
			cell.setCellValue(((Double) value).doubleValue());
		} else if (value instanceof Integer) {
			cell.setCellValue(((Integer) value).intValue());
		}
	}

	private void setColorToCell(org.apache.poi.ss.usermodel.Cell cell,
			Cell optsicomCell) {
		if (optsicomCell.getColor() != null) {

			Color cellColor = optsicomCell.getColor();
			CellStyle style = getCellStyleOf(cell);

			((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(
					cellColor));
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		}
	}

	private void setFormatToCell(DataFormat format,
			org.apache.poi.ss.usermodel.Cell cell, Cell optsicomCell) {
		CellFormat cellFormat = optsicomCell.getFormat();

		if (cellFormat instanceof NumericFormat) {

			NumericFormat numberFormat = (NumericFormat) cellFormat;
			CellStyle style = getCellStyleOf(cell);

			int numDecimals = numberFormat.getNumDecimals();
			String decimalsAsZeroes = "";
			for (int k = 0; k < numDecimals; k++) {
				decimalsAsZeroes += "0";
			}

			switch (numberFormat.getType()) {
			case DECIMAL:
				style.setDataFormat(format.getFormat("0." + decimalsAsZeroes));
				break;
			case INTEGER:
				style.setDataFormat(format.getFormat("0"));
				break;
			case PERCENT:
				style.setDataFormat(format.getFormat("0." + decimalsAsZeroes
						+ "%"));
				break;
			case TIME:
				style.setDataFormat(format.getFormat("0." + decimalsAsZeroes));
				break;
			default:
				break;
			}

		}
	}

	private void writeColTitles(Table table, Sheet sheet, int fromRow,
			int fromCol, int numRowsForColsTitles, int numColsForRowsTitles) {

		for (int numTitle = 0; numTitle < table.getColumnTitles().size(); numTitle++) {

			Title title = table.getColumnTitles().get(numTitle);

			int numAtt = 0;
			for (Attribute att : title.getAttributes()) {

				// A continuación está el código necesario para fusionar las
				// celdas de
				// aquellos títulos que tienen el mismo valor. Por ejemplo,
				// habitualmente
				// el nombre del método debe ocupar una celda fusionada sobre
				// los estadísticos

				boolean newColumn = false;

				if (numTitle > 0) {
					Title previousTitle = table.getColumnTitles().get(
							numTitle - 1);
					String previousAttTitle = previousTitle.getAttributes()
							.get(numAtt).getTitle();
					newColumn = !previousAttTitle.equals(att.getTitle());
				} else {
					newColumn = true;
				}

				if (newColumn) {

					int numRow = fromRow + numAtt;
					Row row = getRow(sheet, numRow);

					int numColumn = numTitle + fromCol + numColsForRowsTitles;
					org.apache.poi.ss.usermodel.Cell cell = getCell(row,
							numColumn);

					cell.setCellValue(att.getTitle());

					// Determinar el tamaño de la celda a fusionar
					int i;
					for (i = numTitle + 1; i < table.getColumnTitles().size(); i++) {
						Title nextTitle = table.getColumnTitles().get(i);
						String nextAttTitle = nextTitle.getAttributes()
								.get(numAtt).getTitle();
						if (!nextAttTitle.equals(att.getTitle())) {
							break;
						}
					}

					// Fusionador de celdas
					if (i - 1 > numTitle) {
						int lastMergedColumn = fromCol + numColsForRowsTitles
								+ i - 1;
						sheet.addMergedRegion(new CellRangeAddress(numRow,
								numRow, numColumn, lastMergedColumn));

						// Cuando se fusionan celdas se crea un borde a ambos
						// lados
						// de la fusión
						// on the left of numColumn and the right of
						// lastMergedColum

						int toRow = numRowsForColsTitles + table.getNumRows()
								+ fromRow;

						setVerticalBorder(sheet, fromRow, toRow, numColumn,
								lastMergedColumn);

					}

					// Formato
					CellStyle style = getCellStyleOf(cell);
					style.setFont(boldFont);
					style.setAlignment(CellStyle.ALIGN_CENTER);

				}

				numAtt++;
			}
		}
	}

	private void setVerticalBorder(Sheet sheet, int fromRow, int toRow,
			int fromColumn, int toColumn) {
		for (int numRowLines = fromRow; numRowLines < toRow; numRowLines++) {

			org.apache.poi.ss.usermodel.Cell leftCell = getCell(sheet,
					numRowLines, fromColumn);

			CellStyle style = getCellStyleOf(leftCell);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());

			org.apache.poi.ss.usermodel.Cell rightCell = getCell(sheet,
					numRowLines, toColumn);

			style = getCellStyleOf(rightCell);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		}
	}

	private void writeRowTitles(Table table, Sheet sheet, int fromRow,
			int fromCol, int numRowsForColsTitles, int numColsForRowsTitles) {
		// Row Titles
		int numTitle = 0;
		for (Title title : table.getRowTitles()) {

			int numAtt = 0;
			for (Attribute att : title.getAttributes()) {

				boolean newRow = false;

				if (numTitle > 0) {
					Title previousTitle = table.getRowTitles()
							.get(numTitle - 1);
					String previousAttTitle = previousTitle.getAttributes()
							.get(numAtt).getTitle();
					newRow = !previousAttTitle.equals(att.getTitle());
				} else {
					newRow = true;
				}

				if (newRow) {

					int numRow = fromRow + numTitle + numRowsForColsTitles;
					int numCol = numAtt + fromCol;

					Row row = getRow(sheet, numRow);
					org.apache.poi.ss.usermodel.Cell cell = getCell(row, numCol);

					cell.setCellValue(att.getTitle());

					sheet.autoSizeColumn(numCol);

					// Determinar el tamaño de la celda a fusionar
					int i;
					for (i = numTitle + 1; i < table.getRowTitles().size(); i++) {
						Title nextTitle = table.getRowTitles().get(i);
						String nextAttTitle = nextTitle.getAttributes()
								.get(numAtt).getTitle();
						if (!nextAttTitle.equals(att.getTitle())) {
							break;
						}
					}

					// Fusionador de celdas
					if (i - 1 > numTitle) {
						int lastMergedRow = numRow + i - numTitle - 1;
						sheet.addMergedRegion(new CellRangeAddress(numRow,
								lastMergedRow, numCol, numCol));

						int toCol = numColsForRowsTitles
								+ table.getNumColumns() + fromCol;

						setHorizontalBorder(sheet, numRow, lastMergedRow,
								fromCol, toCol);

					}

					// Formato
					CellStyle style = getCellStyleOf(cell);
					style.setFont(boldFont);
					style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

				}
				numAtt++;
			}
			numTitle++;
		}
	}

	private void setHorizontalBorder(Sheet sheet, int fromRow, int toRow,
			int fromColumn, int toColumn) {

		for (int numColLines = fromColumn; numColLines < toColumn; numColLines++) {

			org.apache.poi.ss.usermodel.Cell topCell = getCell(sheet, fromRow,
					numColLines);

			CellStyle style = getCellStyleOf(topCell);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());

			org.apache.poi.ss.usermodel.Cell bottomCell = getCell(sheet, toRow,
					numColLines);

			style = getCellStyleOf(bottomCell);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		}
	}

	private org.apache.poi.ss.usermodel.Cell getCell(Row row, int numCell) {
		org.apache.poi.ss.usermodel.Cell cell = row.getCell(numCell);
		if (cell == null) {
			cell = row.createCell(numCell);
			if (defaultStyle == null) {
				defaultStyle = cell.getCellStyle();
			}
		}
		return cell;
	}

	private Row getRow(Sheet sheet, int i) {
		Row row = sheet.getRow((short) i);
		if (row == null) {
			row = sheet.createRow((short) i);
		}
		return row;
	}

	private CellStyle getCellStyleOf(org.apache.poi.ss.usermodel.Cell cell) {
		CellStyle cellStyle = cell.getCellStyle();

		// This is a hack because in the JavaDoc of POI Cell
		// [http://poi.apache.org/apidocs/org/apache/poi/hssf/usermodel/HSSFCell.html#getCellStyle()]
		// it is said that default cellStyle has index 0 and you can retrieve it
		// with workbook.getCellStyleAt(0). But it doesn't work for me (today)

		if (cellStyle.getIndex() == defaultStyle.getIndex()) {
			cellStyle = wb.createCellStyle();
			cell.setCellStyle(cellStyle);
		}
		return cellStyle;
	}

	private void saveToExcelFile(File file) throws IOException {

		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();

	}

}
