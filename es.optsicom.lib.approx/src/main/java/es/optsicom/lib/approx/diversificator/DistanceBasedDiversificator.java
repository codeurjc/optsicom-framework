/**
 * 
 */
package es.optsicom.lib.approx.diversificator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.approx.diversificator.Diversificator;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.DescriptiveHelper;
import es.optsicom.lib.util.description.Properties;

/**
 * @author patxi
 * 
 */
public class DistanceBasedDiversificator<D extends DistanceCalculator<S, I>, S extends Solution<I>,I extends Instance> extends AbstractDiversificator<S> {

	private D distanceCalculator;

	public DistanceBasedDiversificator(D distanceCalculator) {
		this.distanceCalculator = distanceCalculator;
	}

	@Override
	public Properties getProperties() {
		return DescriptiveHelper.createProperties(this);
	}

	@Override
	public List<S> getDiversity(int num, Collection<S> solutions,
			List<S> refSolutions) {
		List<S> refSetList = new ArrayList<S>(refSolutions);
		List<S> pullSolutions = new ArrayList<S>(solutions);
		List<S> moreDiverseSolutions = new ArrayList<S>();

		for (int i = 0; i < num; i++) {
			List<SolutionDistanceInfo<D,S,I>> distances = new ArrayList<SolutionDistanceInfo<D,S,I>>();
			for (S solution : pullSolutions) {
				if (!refSetList.contains(solution)) {
					SolutionDistanceInfo<D,S,I> sdi = new SolutionDistanceInfo<D,S,I>(solution, distanceCalculator);
					sdi.addDistanceToSolutions(refSetList);
					distances.add(sdi);
				}
			}

			Collections.sort(distances);

			if (distances.size() == 0) {
				return moreDiverseSolutions;
			}

			S selectedDiverseSolution = distances.get(distances.size() - 1).getSolution();
			moreDiverseSolutions.add(selectedDiverseSolution);
			refSetList.add(selectedDiverseSolution);
			while (pullSolutions.indexOf(selectedDiverseSolution) != -1) {
				pullSolutions.remove(selectedDiverseSolution);
			}
		}

		return moreDiverseSolutions;
	}
}
