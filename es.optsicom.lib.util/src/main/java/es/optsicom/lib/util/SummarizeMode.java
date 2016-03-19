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

import java.util.List;

public enum SummarizeMode {

	AVERAGE {
		@Override
		public Double summarizeValues(Double[] values) {
			return ArraysUtil.average(values);
		}

		@Override
		public Double summarizeValues(List<? extends Number> values) {
			return CollectionsUtil.average(values);
		}
	},

	MAX {
		@Override
		public Double summarizeValues(Double[] values) {
			return ArraysUtil.max(values);
		}

		@Override
		public Double summarizeValues(List<? extends Number> values) {
			return CollectionsUtil.max(values);
		}
	},

	MIN {
		@Override
		public Double summarizeValues(Double[] values) {
			return ArraysUtil.min(values);
		}

		@Override
		public Double summarizeValues(List<? extends Number> values) {
			return CollectionsUtil.min(values);
		}
	},

	SUM {
		@Override
		public Double summarizeValues(Double[] values) {
			return ArraysUtil.sum(values);
		}

		@Override
		public Double summarizeValues(List<? extends Number> values) {
			return CollectionsUtil.sum(values);
		}
	},

	FIRST {

		@Override
		public Double summarizeValues(Double[] values) {
			return values[0];
		}

		@Override
		public Double summarizeValues(List<? extends Number> values) {
			return values.get(0).doubleValue();
		}
	},

	MEDIAN {

		@Override
		public Double summarizeValues(Double[] values) {
			return values[(int) Math.floor(values.length / 2)];
		}

		@Override
		public Double summarizeValues(List<? extends Number> values) {
			return values.get((int) Math.floor(values.size() / 2)).doubleValue();
		}
	};

	public abstract Double summarizeValues(Double[] values);

	public abstract Double summarizeValues(List<? extends Number> values);

}