/* ******************************************************************************
 * 
 * This file is part of Optsicom
 * 
 * License:
 *   EPL: http://www.eclipse.org/legal/epl-v10.html
 *   LGPL 3.0: http://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *   See the LICENSE file in the project's top-level directory for details.
 *
 * **************************************************************************** */
package es.optsicom.lib.analyzer.tablecreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import es.optsicom.lib.util.Id;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

/**
 * 
 * @author Patxi Gortázar
 * 
 */
public class ValueProvider implements Descriptive {

	private File valuesFile;
	
	private Map<String, Object[]> lineInfo = new HashMap<String, Object[]>();

	public ValueProvider(File file) {
		this.valuesFile = file;
	}

	public Number getValue(String id) {
		checkIfFileExists();

		Object[] info = getLineInfo(id);
		if(info != null) {
			return (Number) info[1];
		}
		
		List<String> sections = splitId(id);
		String fileName = sections.get(sections.size() - 1);
		String fileNameWithoutExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(valuesFile)));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(id) || line.contains(fileName) || line.contains(fileNameWithoutExt)) {

					try {
						lineInfo.put(id, parseLine(line));
						break;
					} catch (NumberFormatException e) {
						throw new AnalysisException("Line could not be parsed: " + line, e);
					}

				}
			}
		} catch (FileNotFoundException e) {
			throw new AnalysisException("File not found: " + valuesFile, e);
		} catch (IOException e) {
			throw new AnalysisException(e);
		}

		Object[] parsedInfo = lineInfo.get(id); 
		return (Number) parsedInfo[1];

	}
	
	protected Object[] getLineInfo(String id) {
		return lineInfo.get(id);
	}

	public Number getTime(String id) {
		checkIfFileExists();
	
		Object[] info = getLineInfo(id);
		if(info != null) {
			return (Number) info[2];
		}
		
		List<String> sections = splitId(id);
		String fileName = sections.get(sections.size() - 1);
		String fileNameWithoutExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(valuesFile)));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(id) || line.contains(fileName) || line.contains(fileNameWithoutExt)) {

					try {
						lineInfo.put(id, parseLine(line));
					} catch (NumberFormatException e) {
						throw new AnalysisException("Line could not be parsed: " + line, e);
					}

				}
			}
		} catch (FileNotFoundException e) {
			throw new AnalysisException("File not found: " + valuesFile, e);
		} catch (IOException e) {
			throw new AnalysisException(e);
		}

		Object[] parsedInfo = lineInfo.get(id); 
		return (Number) parsedInfo[2];
	}

	private void checkIfFileExists() throws Error {
		if (!valuesFile.exists()) {
			throw new Error("File does not exist: " + valuesFile.getAbsolutePath());
		}
	}

	protected Object[] parseLine(String line) {
		StringTokenizer st = new StringTokenizer(line);

		String instanceName = st.nextToken();
		
		String bestKnownValue = st.nextToken();
		double bkv;
		try{
			bkv = Double.parseDouble(bestKnownValue);
		} catch(NumberFormatException e) {
			throw new AnalysisException("Couldn't parse bestKnowValue: " + bestKnownValue, e);
		}
		
		String time = null;
		double t = 0.0;
		if(st.hasMoreElements()) {
			time = st.nextToken();
			try {
				t = Double.parseDouble(time);
			} catch(NumberFormatException e) {
				// Do nothing. We can still continue without the time
			}
		}

		// If no time information is found in the line, a value of 0 is used.
		Object[] info = new Object[] {instanceName, bkv, t};
		
		return info;
	}

	private List<String> splitId(String id) {
		StringTokenizer st = new StringTokenizer(id, File.separator);
		List<String> parts = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			parts.add(st.nextToken());
		}
		return parts;
	}

	/**
	 * @return the valuesFile
	 */
	@Id
	public File getValuesFile() {
		return valuesFile;
	}

	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

}
