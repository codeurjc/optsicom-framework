package es.optsicom.lib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleCommand {

	public static String execCommand(File workDir, String command) {
		Runtime r = Runtime.getRuntime();

		try {
			Process p;
			
			if(workDir == null){
				p = r.exec(command);
			} else {
				p = r.exec(command, null, workDir.getAbsoluteFile());
			}

			InputStream out = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(out));

			StringBuilder sb = new StringBuilder();
			String linea;
			while ((linea = br.readLine()) != null) {
				sb.append(linea).append("\r\n");
			}

			return sb.toString();

		} catch (Exception e) {
			throw new RuntimeException("Exception processing command", e);
		}
	}

	public static String execCommand(String command) {
		return execCommand(null, command);
	}

}
