package es.optsicom.lib.experiment;


public class ExperimentMethodStopCriteria {

	private static MethodStopChecker checker = null;
	private static long timeStart;
	private static ExperimentExecution experimentExecution;

	public static void checkIfStop(Object procedure) {

		if (checker != null) {
			
			checker.checkIfStop(procedure);
			
		} else if (experimentExecution != null) {

			// TODO: This is not compatible with providing different time limits for each instance group
			if (experimentExecution.getTimeLimit() != -1) {
				if (System.currentTimeMillis() - timeStart > experimentExecution
						.getTimeLimit()) {
					throw new StopMethodException();
				}
			}
			
		}

	}

	public static void setExperimentExecution(ExperimentExecution experimentConf) {
		ExperimentMethodStopCriteria.experimentExecution = experimentConf;
	}

	public static void experimentStarted() {
		ExperimentMethodStopCriteria.timeStart = System.currentTimeMillis();
	}

	public static void setChecker(MethodStopChecker checker) {
		ExperimentMethodStopCriteria.checker = checker;
	}
	
	public static long getTimeStart() {
		return timeStart;
	}

}
