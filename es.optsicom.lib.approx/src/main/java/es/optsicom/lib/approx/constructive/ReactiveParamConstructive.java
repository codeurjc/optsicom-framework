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
package es.optsicom.lib.approx.constructive;

import java.util.Random;

import es.optsicom.lib.Instance;
import es.optsicom.lib.Solution;
import es.optsicom.lib.util.MathUtil;
import es.optsicom.lib.util.RandomManager;
import es.optsicom.lib.util.description.Properties;

public abstract class ReactiveParamConstructive<S extends Solution<I>, I extends Instance> extends NonIsoIntervalConstructive<S, I> {

	private double[] paramValues;
	private double[] paramScores;
	private double closeToBest;
	private double bestSolutionWeight = 0;
	private boolean scoredParams;

	public ReactiveParamConstructive(double fromValue, double toValue, int numIntervals, double closeToBest) {
		this(createValues(fromValue, toValue, numIntervals), closeToBest);
	}

	public ReactiveParamConstructive(double[] paramValues, double closeToBest) {
		super(new float[] { 0.2f, 0.8f });
		this.paramValues = paramValues;
		this.closeToBest = closeToBest;
		this.paramScores = new double[paramValues.length];
	}

	private static double[] createValues(double fromValue, double toValue, int numIntervals) {
		double[] paramValues = new double[numIntervals];
		double range = toValue - fromValue;
		double inc = range / (numIntervals - 1);
		for (int i = 0; i < numIntervals - 1; i++) {
			paramValues[i] = fromValue + inc * i;
		}
		paramValues[numIntervals - 1] = toValue;
		return paramValues;
	}

	protected abstract S createSolution(double paramValue);

	//	@Override
	//	public void initSolutionCreationByNum(int numSolutions) {
	//		super.initSolutionCreationByNum(numSolutions);
	//		System.out.println("ParamValues: " + Arrays.toString(paramValues));
	//		System.out.println("CloseToBest: " + closeToBest);
	//	}

	@Override
	protected S createSolution(int numInterval) {

		//System.out.print("Create Solution. " + Arrays.toString(this.paramScores));

		int paramIndex;
		if (scoredParams) {
			//System.out.print(" SC ");
			paramIndex = MathUtil.selectIndex(paramScores);
		} else {
			//System.out.print("NSC ");
			paramIndex = RandomManager.nextInt(paramValues.length);
		}

		//System.out.print("paramIndex: " + paramIndex);

		double paramValue = paramValues[paramIndex];

		//System.out.format(" paramValue: %.2f", paramValue);

		S s = createSolution(paramValue);

		//System.out.format(" W: %.2f Best:%.2f ", s.getWeight(), bestSolutionWeight);

		if (s.getWeight() > bestSolutionWeight) {
			//System.out.print("Better than best.");
			bestSolutionWeight = s.getWeight();
			paramScores[paramIndex]++;
		} else if (s.getWeight() > closeToBest * bestSolutionWeight) {
			//System.out.print("Close to best.");
			paramScores[paramIndex]++;
		} else {
			//System.out.print("Not close to bestSolution.");
		}

		//System.out.println();

		return s;

	}

	@Override
	protected void initInterval(int interval) {
		switch (interval) {
			case 0:
				this.bestSolutionWeight = 0;
				this.scoredParams = false;
				this.paramScores = new double[paramValues.length];
				break;
			default:
				this.scoredParams = true;
		}

	}

	public boolean isDeterminist() {
		return false;
	}

	@Override
	public Properties getProperties() {
		Properties description = super.getProperties();
		description.put("closeToBest", closeToBest);
		return description;
	}
}
