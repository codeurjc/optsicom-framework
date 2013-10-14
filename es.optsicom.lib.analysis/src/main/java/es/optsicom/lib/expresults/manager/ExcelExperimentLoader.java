package es.optsicom.lib.expresults.manager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import es.optsicom.lib.analyzer.DefaultReportConf;
import es.optsicom.lib.analyzer.ReportConf;
import es.optsicom.lib.analyzer.tablecreator.filter.ElementFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.ExplicitElementsFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.OrElementsFilter;
import es.optsicom.lib.analyzer.tablecreator.filter.PropertiesFilter;
import es.optsicom.lib.expresults.model.ComputerDescription;
import es.optsicom.lib.expresults.model.DBProperties;
import es.optsicom.lib.expresults.model.DoubleEvent;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.expresults.model.NonValueEvent;
import es.optsicom.lib.expresults.model.Researcher;
import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.description.Properties;

public class ExcelExperimentLoader {

	private static final int MAX_ROWS = 1000;
	private static final int MAX_COLS = 1000;

	private Sheet sheet;

	private int dataRow;

	private int algCol;

	private List<String> instanceChars;
	private ArrayList<InstanceDescription> instances;

	private ArrayList<MethodDescription> methods;
	private List<String> methodNames;

	private Map<InstanceDescription, Map<MethodDescription, List<Execution>>> data;
	private File excelFile;
	private Experiment experiment;
	private Map<MethodDescription, String> memMethodNames;

	public static MemoryExperimentManager load(File excelFile, int numSheet)
			throws IOException {
		return new ExcelExperimentLoader().internalLoad(excelFile, numSheet);
	}

	public static MemoryExperimentManager load(File excelFile)
			throws IOException {
		return new ExcelExperimentLoader().internalLoad(excelFile, 0);
	}

	private MemoryExperimentManager internalLoad(File excelFile, int numSheet)
			throws IOException {

		this.excelFile = excelFile;

		loadExcelSheet(numSheet);

		calculateRowsAndCols();

		loadInstanceChars();

		// System.out.println("Chars:" + instanceChars);

		loadMethods();

		// System.out.println("MethodNames:" + methodNames);
		// System.out.println("Methods:" + methods);

		loadInstances();

		// System.out.println("Instances: " + instances);

		loadData();

		// System.out.println(data);

		return new MemoryExperimentManager(experiment, data, memMethodNames);

	}

	private void loadData() {

		data = new HashMap<InstanceDescription, Map<MethodDescription, List<Execution>>>();

		experiment = new Experiment(excelFile.getName(), new Researcher(
				"Researcher"), new Date(excelFile.lastModified()),
				new ComputerDescription("Computer"));
		experiment.setProblemBestMode(BestMode.MAX_IS_BEST);
		experiment.setMethods(methods);

		for (int numInstance = 0; numInstance < instances.size(); numInstance++) {

			int row = numInstance + dataRow;
			Map<MethodDescription, List<Execution>> methodsMap = new HashMap<MethodDescription, List<Execution>>();
			InstanceDescription instance = instances.get(numInstance);
			data.put(instance, methodsMap);

			// System.out.println("Instance" + instance.getName());

			for (int numMethod = 0; numMethod < methods.size(); numMethod++) {

				int col = numMethod + algCol;
				MethodDescription method = methods.get(numMethod);

				double value = getCellValueAsNumber(sheet, row, col);
				long time = (long) (getCellValueAsNumber(sheet, row, col + 1) * 1000);

				// System.out.println("   Method " +
				// method.getName()+" : "+value);
				// System.out.println("      Time : " + time);

				Execution execution = new Execution(experiment, method,
						instance);
				execution.addEvent(new DoubleEvent(execution, time,
						Event.OBJ_VALUE_EVENT, value));
				execution.addEvent(new NonValueEvent(execution, time,
						Event.FINISH_TIME_EVENT));

				methodsMap.put(method, Arrays.asList(execution));
			}

		}

	}

	private void loadInstances() {

		instances = new ArrayList<InstanceDescription>();

		for (int i = dataRow; i < MAX_ROWS; i++) {
			String cellValue = getCellValueAsString(sheet, i, 0);
			if (cellValue != null && !cellValue.isEmpty()) {

				Map<String, String> props = new HashMap<String, String>();
				for (int j = 0; j < this.instanceChars.size(); j++) {
					props.put(instanceChars.get(j),
							getCellValueAsString(sheet, i, j + 1));
				}
				DBProperties properties = new DBProperties(props);
				instances.add(new InstanceDescription(properties));
			} else {
				break;
			}
		}
	}

	private void loadMethods() {
		methodNames = new ArrayList<String>();
		methods = new ArrayList<MethodDescription>();
		memMethodNames = new HashMap<MethodDescription, String>();
		for (int i = algCol; i < MAX_COLS; i += 2) {
			String cellValue = getCellValueAsString(sheet, dataRow - 1, i);
			if (cellValue != null && !cellValue.isEmpty()) {
				methodNames.add(cellValue);
				MethodDescription methodDescription = new MethodDescription(
						cellValue);
				methods.add(methodDescription);
				memMethodNames.put(methodDescription, cellValue);
			} else {
				break;
			}
		}
	}

	private void loadInstanceChars() {
		int numInstanceChars = algCol - 1;

		instanceChars = new ArrayList<String>();
		for (int i = 0; i < numInstanceChars; i++) {
			instanceChars.add(getCellValueAsString(sheet, dataRow - 1, i + 1));
		}
	}

	private void calculateRowsAndCols() {

		int row = 0;
		int col = 0;

		for (; row < MAX_ROWS; row++) {
			if ("Instance".equals(getCellValueAsString(sheet, row, 0))) {
				break;
			}
		}

		if (row == MAX_ROWS) {
			throw new RuntimeException(
					"Max rows limit ("
							+ MAX_ROWS
							+ ") reached without found \"Instance\" cell value in column 0.");
		}

		dataRow = row + 2;

		for (; col < MAX_COLS; col++) {
			if ("Algorithms".equals(getCellValueAsString(sheet, row, col))) {
				break;
			}
		}

		algCol = col;
	}

	private void loadExcelSheet(int numSheet) throws FileNotFoundException,
			IOException {
		InputStream is = new FileInputStream(excelFile);
		HSSFWorkbook excel = new HSSFWorkbook(new POIFSFileSystem(is));

		sheet = excel.getSheetAt(numSheet);
	}

	private String getCellValueAsString(Sheet sheet, int row, int col) {
		Cell cell = getCell(sheet, row, col);
		if (cell != null) {
			// System.out.println("[" + row + "," + col + "] = "
			// + cell.getStringCellValue());
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				// Some parameters may be numbers
				double num = cell.getNumericCellValue();
				return Integer.toString((int) num);
			} else {
				return null;
			}
		} else {
			// System.out.println("[" + row + "," + col + "] = null");
			return null;
		}
	}

	private double getCellValueAsNumber(Sheet sheet, int row, int col) {
		Cell cell = getCell(sheet, row, col);
		if (cell != null) {
			// System.out.println("[" + row + "," + col + "] = "
			// + cell.getNumericCellValue());
			return cell.getNumericCellValue();
		} else {
			// System.out.println("[" + row + "," + col + "] = null");
			return 0;
		}
	}

	private Cell getCell(Sheet sheet, int row, int column) {
		Row excelRow = sheet.getRow(row);
		if (excelRow != null) {
			return excelRow.getCell(column);
		} else {
			return null;
		}
	}

}
