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
package es.optsicom.lib;

import java.io.File;
import java.util.Comparator;

import es.optsicom.lib.instancefile.FileInstancesRepository;
import es.optsicom.lib.instancefile.InstancesRepository;
import es.optsicom.lib.util.BestMode;

/**
 * @author Patxi Gortázar
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class Problem {

	private BestMode mode;

	private Comparator<Solution> qualityComparator;
	private String name;

	private Comparator<Solution> MAX_IS_BETTER_COMPARATOR = new Comparator<Solution>() {

		public int compare(Solution solutionA, Solution solutionB) {
			return solutionA.getWeight() > solutionB.getWeight() ? 1
					: solutionA.getWeight() < solutionB.getWeight() ? -1 : 0;
		}
	};

	private Comparator<Solution> MIN_IS_BETTER_COMPARATOR = new Comparator<Solution>() {

		public int compare(Solution solutionA, Solution solutionB) {
			return solutionA.getWeight() < solutionB.getWeight() ? 1
					: solutionA.getWeight() > solutionB.getWeight() ? -1 : 0;
		}
	};

	public Problem(String name, BestMode mode) {
		this.mode = mode;
		this.name = name;
		if (mode == BestMode.MAX_IS_BEST) {
			qualityComparator = MAX_IS_BETTER_COMPARATOR;
		} else {
			qualityComparator = MIN_IS_BETTER_COMPARATOR;
		}
	}

	public BestMode getMode() {
		return mode;
	}

	public Comparator<Solution> getQualityComparator() {
		return qualityComparator;
	}

	public String getName() {
		return name;
	}

	public InstancesRepository getInstancesRepository() {
		return getInstancesRepository(InstancesRepository.getDefaultInstancesDirOrZip().toFile(),
				InstancesRepository.DEFAULT_USE_CASE);
	}

	public InstancesRepository getInstancesRepository(String useCase) {
		if (useCase == null) {
			useCase = InstancesRepository.DEFAULT_USE_CASE;
		}
		return getInstancesRepository(InstancesRepository.getDefaultInstancesDirOrZip().toFile(), useCase);
	}

	// TODO Cache this result to avoid creating new InstancesRepositories for
	// each call. Also think about InstancesRepository lifecycle. As InstancesRepository can be connected
	// to a zip file, this connection is maintained until close() is explicitely invoked in ZipFileSystem
	public abstract InstancesRepository getInstancesRepository(File instanceFileDir, String useCase);

	public SolutionFactory getDefaultFactory() {
		return null;
	}
}
