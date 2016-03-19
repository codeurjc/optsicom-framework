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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Problem;
import es.optsicom.lib.Solution;
import es.optsicom.lib.expresults.saver.ExperimentSaver;
import es.optsicom.lib.instancefile.InstanceFile;

public abstract class ExperimentExecution<S extends Solution<I>, I extends Instance> {

	private File resultsDir;
	private File instanceFilesDir;
	private String useCase;
	private Problem problem;
	private long seed;

	protected List<InstanceFile> instanceFiles;
	protected List<Long> instanceTimeLimits;
	protected long timeLimit = -1;

	protected boolean overrideExec = true;

	public ExperimentExecution() {
		this.resultsDir = new File(
				"Experiments/Test/" + String.format("%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS", new java.util.Date()));
	}

	public void setOverrideExec(boolean overrideExec) {
		this.overrideExec = overrideExec;
	}

	public List<InstanceFile> getInstanceFiles() {
		return instanceFiles;
	}

	public void setInstanceFiles(List<InstanceFile> instanceFiles) {
		this.instanceFiles = instanceFiles;
	}

	public void setInstanceTimeLimits(List<Long> instanceTimeLimits) {
		this.instanceTimeLimits = instanceTimeLimits;
	}

	public Problem getProblem() {
		return problem;
	}

	public void executeExperiment(ExperimentSaver saver) throws IOException {
		executeExperiment(saver, true);
	}

	/**
	 * Executes the experiment and save the result in a File
	 *
	 * @throws IOException
	 */
	public void executeExperiment(ExperimentSaver saver, boolean localExecution) throws IOException {

		System.out.println("Experiment Id = " + saver.getExperimentId());

		if (instanceFiles == null) {
			// We obtain the instance files from the repository
			if (problem == null) {
				throw new RuntimeException("The problem object must be set before executing the experiment");
			}

			if (instanceFilesDir == null) {
				setInstanceFiles(problem.getInstancesRepository(useCase).getAllInstanceFiles());
			} else {
				setInstanceFiles(problem.getInstancesRepository(instanceFilesDir, useCase).getAllInstanceFiles());
			}
		}

		experimentStarted(saver);

		ExperimentMethodStopCriteria.setExperimentExecution(this);

		int numInstance = 0;

		List<OptsicomException> thrownExceptions = new ArrayList<OptsicomException>();

		int instanceIndex = 0;
		for (InstanceFile instanceFile : instanceFiles) {

			long instanceTimeLimit = this.timeLimit;

			if (this.instanceTimeLimits != null) {
				long instTimeLimit = instanceTimeLimits.get(numInstance);

				if (instTimeLimit != -1) {
					instanceTimeLimit = instTimeLimit;
				}
			}

			try {

				System.out.println("(" + (numInstance + 1) + "/" + instanceFiles.size()
						+ ") Experimenting with Instance: " + instanceFile.getName());
				System.out.println("TimeLimit (millis): " + instanceTimeLimit);
				System.out.println("   " + instanceFile.getProperties());
				numInstance++;

				@SuppressWarnings("unchecked")
				I instance = (I) instanceFile.loadInstance();

				executeExperiment(saver, instance, instanceIndex, instanceTimeLimit, thrownExceptions, localExecution);

			} catch (Exception e) {
				// Exception not caught by executeExperimentMethod???
				// This should not happen
				thrownExceptions.add(new OptsicomException(instanceFile, null, e));
			}

			instanceIndex++;
		}

		experimentFinished(saver);

		// Print report:
		System.out.println("Ok runs: " + (instanceFiles.size() - thrownExceptions.size()));
		System.out.println("Error runs: " + thrownExceptions.size());
		if (!thrownExceptions.isEmpty()) {
			System.out.println("Exceptions thrown: ");
			for (OptsicomException oe : thrownExceptions) {
				System.out.println(oe.getInstanceFile().getName());
				oe.printStackTrace();
				System.out.println();
			}
		}
	}

	protected void experimentFinished(ExperimentSaver saver) {
	}

	protected void experimentStarted(ExperimentSaver saver) {
	}

	protected abstract void executeExperiment(ExperimentSaver saver, I instance, int instanceIndex, long timeLimit,
			List<OptsicomException> thrownExceptions, boolean localExecution);

	public File getResultsDir() {
		return resultsDir;
	}

	public void setResultsDir(File resultsDir) {
		this.resultsDir = resultsDir;
	}

	public File getInstanceFilesDir() {
		return instanceFilesDir;
	}

	public void setInstanceFilesDir(File instanceFilesDir) {
		this.instanceFilesDir = instanceFilesDir;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	/**
	 * @param seed
	 *            The seed to be used for each method
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}

	/**
	 * @return the seed
	 */
	public long getSeed() {
		return seed;
	}

	public String getUseCase() {
		return useCase;
	}

	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

}
