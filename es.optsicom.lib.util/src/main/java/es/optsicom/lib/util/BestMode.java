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

public enum BestMode {
	MIN_IS_BEST {
		@Override
		public String toString() {
			return "min";
		}

		@Override
		public boolean isBetterThan(double newWeight, double oldWeight) {
			return newWeight < oldWeight;
		}

		@Override
		public boolean isImprovement(double increment) {
			return increment < 0;
		}

		@Override
		public boolean isNoWorseThanPerc(double newWeight, double oldWeight, double percentage) {
			return newWeight * (1 - percentage) < oldWeight;
		}
	},
	MAX_IS_BEST {
		@Override
		public String toString() {
			return "max";
		}

		@Override
		public boolean isBetterThan(double newWeight, double oldWeight) {
			return newWeight > oldWeight;
		}

		@Override
		public boolean isImprovement(double increment) {
			return increment > 0;
		}

		@Override
		public boolean isNoWorseThanPerc(double newWeight, double oldWeight, double percentage) {
			return newWeight > oldWeight * (1 - percentage);
		}
	};

	public abstract boolean isBetterThan(double newWeight, double oldWeight);

	public abstract boolean isNoWorseThanPerc(double newWeight, double oldWeight, double percentage);

	public abstract boolean isImprovement(double increment);

}