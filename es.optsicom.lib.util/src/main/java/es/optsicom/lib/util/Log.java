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
package es.optsicom.lib.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static FileWriter fWriter = null;
	private static boolean toScreen = true;

	public static void debugFile(File file) {
		try {
			Log.fWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void debug(double value) {
		debug(Double.toString(value));
	}

	public static void debug(String msg) {
		if (fWriter != null) {
			try {
				fWriter.append(msg);
				fWriter.flush();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		if (toScreen) {
			System.out.print(msg);
		}
	}

	public static void debugln(String msg) {
		debug(msg + LINE_SEPARATOR);
	}

	public static void debugln(Object obj) {
		debugln(obj.toString());
	}

	public static void closeDebug() {
		if (fWriter != null) {
			try {
				fWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void flush() {
		if (fWriter != null) {
			try {
				fWriter.flush();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	public static boolean isToScreen() {
		return toScreen;
	}

	public static void setToScreen(boolean toScreen) {
		Log.toScreen = toScreen;
	}

	public static void debugException(String message, Exception e) {
		debugln(message);
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		debugln(sw.toString());
	}
}
