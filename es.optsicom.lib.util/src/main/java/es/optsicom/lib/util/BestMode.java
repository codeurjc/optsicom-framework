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
	};

	public abstract boolean isBetterThan(double newWeight, double oldWeight);

	public abstract boolean isImprovement(double increment);
	
}