package es.optsicom.lib.analyzer.tablecreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class OptimumValueProvider {

	private File valuesFile;
	
	public OptimumValueProvider(File file) {
		this.valuesFile = file;
	}
	
	public Number getValue(String id) {
		if (!valuesFile.exists()) {
			throw new Error("File does not exist: " + valuesFile.getAbsolutePath());
		}

		String fileName = id.contains(File.separator) ? id.substring(id.lastIndexOf(File.separator) + 1) : id;
		String fileNameWithoutExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(valuesFile)));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(id) || line.contains(fileName) || line.contains(fileNameWithoutExt)) {

					return parseOptimum(line);

				}
			}
		} catch (FileNotFoundException e) {
			throw new AnalysisException("File not found: " + valuesFile, e);
		} catch (IOException e) {
			throw new AnalysisException(e);
		}

		return null;
	
	}
	
	protected Number parseOptimum(String line) {
		StringTokenizer st = new StringTokenizer(line);

		String instanceName = st.nextToken();
		String optimumValue = st.nextToken();

		return Double.parseDouble(optimumValue);
	}

}
