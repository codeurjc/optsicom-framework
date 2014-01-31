package es.optsicom.lib.intances;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class InstanceWriter {
	private static final String UTF_8 = "UTF-8";

	public static void write(String fileName, double[][] distances) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("./" + fileName, UTF_8);

		writer.println(distances.length);
		for (int i = 0; i < distances.length - 1; i++) {
			for (int j = i + 1; j < distances.length; j++) {
				writer.println((i + 1) + " " + (j + 1) + " " + distances[i][j]);
			}
		}
		writer.close();
	}
}
