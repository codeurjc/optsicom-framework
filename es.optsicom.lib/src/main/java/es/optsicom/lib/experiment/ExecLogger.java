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

import es.optsicom.lib.Solution;
import es.optsicom.lib.experiment.ExecutionLogger.Event;

/**
 * This class is used to log execution events. Has several static methods to
 * allow clients to use its services quickly (without chaining "getInstance()"
 * or getLogger() static methods). The real class that perform the logging is
 * ExecutionLogger. It is possible that this system change in the future
 * inspired in a full featured logger like Log4J or Java logging facility.
 * 
 * @author Micael Gallego
 */
public class ExecLogger {

	private static final DummyExecLogger DUMMY_EXEC_LOGGER = new DummyExecLogger();

	private static ExecutionLogger execLogger = DUMMY_EXEC_LOGGER;

	public static void removeExecutionLogger() {
		ExecLogger.execLogger = DUMMY_EXEC_LOGGER;
	}

	public static void setExecutionLogger(ExecutionLogger execLogger) {
		ExecLogger.execLogger = execLogger;
	}

	public static void addEvent(String eventName, Object value) {
		addEvent(new Event(eventName, value));
	}

	public static void addEvent(String eventName) {
		addEvent(new Event(eventName));
	}

	public static void addEvent(Event event) {
		execLogger.addEvent(event);
	}

	public static void addEvents(Event... events) {
		execLogger.addEvents(events);
	}

	/**
	 * This method is used to log when a new solution is found and its value.
	 * The value will be logged only if recordEvolution is true in the
	 * experiment. This method does not receive a solution object because is
	 * designed to be used by improvement methods that not use the solution
	 * object during the improvement phase. If the new solution found is
	 * represented in a solution object, is better to use
	 * {@link ExecLogger#newSolutionFound(Solution)}
	 * 
	 * @param weight
	 *            new solution weight
	 */
	public static void newSolutionFound(double weight) {
		execLogger.newSolutionFound(weight);
	}

	/**
	 * This method is used to log the solution when a new solution is found. The
	 * solution will be logged only if recordEvolution is true in the
	 * experiment. It is guaranteed that solution value is recorded, but
	 * recording the elements of the solution can be a bit expensive to
	 * calculate in every call to this method. For this, it depends on the
	 * ExecutionLogger to save or not this information. This method is designed
	 * to be used when the method manage the solution as a solution object. If
	 * the method calculates the solution value by other means, is better to use
	 * {@link ExecLogger#newSolutionFound(Solution)}
	 * 
	 * @param weight
	 *            new solution
	 */
	public static void newSolutionFound(Solution<?> solution) {
		execLogger.newSolutionFound(solution);
	}

	/**
	 * This method allows to disable temporally the logger. This functionality
	 * is useful when using infeasible solutions because it is likely that you
	 * don't want the value of the infeasible will be recorded like any other
	 * feasible solution. For example, it is used in strategic oscillation
	 * methods.
	 * 
	 * @param scopeName
	 *            the name of this "disabling scope". It is mainly used for
	 *            debug proposes.
	 * @see ExecLogger#enableLogging(String)
	 */
	public static void disableLogging(String scopeName) {
		execLogger.disableLogging(scopeName);
	}

	/**
	 * This method allows to enable again the logger. This functionality is used
	 * in conjunction with {@link ExecLogger#disableLogging(String)}. The logger
	 * can be enabled only when it has been previously disabled.
	 * 
	 * @param scopeName
	 *            the name of this "disabling scope". It is mainly used for
	 *            debug proposes.
	 * @see ExecLogger#disableLogging(String)
	 */
	public static void enableLogging(String scopeName) {
		execLogger.enableLogging(scopeName);
	}
}
