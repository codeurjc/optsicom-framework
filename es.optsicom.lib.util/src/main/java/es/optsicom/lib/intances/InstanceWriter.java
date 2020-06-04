package es.optsicom.lib.intances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class InstanceWriter {
	private static final String INSTANCE_FILES = "./instance_files";
	private static final String UTF_8 = "UTF-8";

	public InstanceWriter() {
		File dir = new File(INSTANCE_FILES);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public void write(String folder, String fileName, double[][] instanceDistances)
			throws FileNotFoundException, UnsupportedEncodingException {

		File dir = new File(INSTANCE_FILES + "/" + folder);
		if (!dir.exists()) {
			dir.mkdir();
		}

		PrintWriter writer = new PrintWriter(dir.getPath() + "/" + fileName + ".txt", UTF_8);
		writer.println(instanceDistances.length);
		for (int i = 0; i < instanceDistances.length - 1; i++) {
			for (int j = i + 1; j < instanceDistances.length; j++) {
				writer.println(i + " " + j + " " + instanceDistances[i][j]);
			}
		}
		writer.close();
	}
}
