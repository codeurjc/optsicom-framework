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
package es.optsicom.lib.experiment;

import es.optsicom.lib.expresults.saver.ExecutionSaver;

public class CurrentExperiment {

	private static ExecutionSaver execSaver;

	public static void addEvent(String eventName) {
		addEvent(eventName, null);
	}

	public static void addEvent(String eventName, Object value) {
		if (execSaver != null) {
			execSaver.addEvent(eventName, value);
		}
	}

	public static void finishExecution() {
		CurrentExperiment.execSaver = null;		
	}

	public static void startExecution(ExecutionSaver execSaver) {
		CurrentExperiment.execSaver = execSaver;		
	}

}
