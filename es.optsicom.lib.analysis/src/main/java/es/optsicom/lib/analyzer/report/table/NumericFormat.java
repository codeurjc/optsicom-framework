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
package es.optsicom.lib.analyzer.report.table;

import java.util.Locale;

public class NumericFormat extends CellFormat {

	public enum NumberType {
		INTEGER, DECIMAL, PERCENT, TIME
	}

	private NumberType type;
	private int numDecimals;

	public NumericFormat(NumberType type, int numDecimals) {
		super();
		this.type = type;
		this.numDecimals = numDecimals;
	}

	public NumericFormat(NumberType type) {
		super();
		this.type = type;
		this.numDecimals = 5;
	}

	public NumberType getType() {
		return type;
	}

	public void setType(NumberType type) {
		this.type = type;
	}

	public int getNumDecimals() {
		return numDecimals;
	}

	public void setNumDecimals(int numDecimals) {
		this.numDecimals = numDecimals;
	}

	@Override
	public String format(Object value, Locale locale) {

		if (value == null) {
			return "";
		}

		if (type == null) {
			return String.format(locale, "%." + numDecimals + "f", value);
		} else {
			switch (type) {
			case INTEGER:
				return String.format(locale, "%d", ((Number) value).intValue());
			case DECIMAL:
				return String.format(locale, "%." + numDecimals + "f", value);
			case PERCENT:
				return String.format(locale, "%." + numDecimals + "f%%", ((Number) value).doubleValue() * 100);
			// return String.format(locale, "%." + numDecimals + "f", ((Number)
			// value).doubleValue());
			// return String.format(locale, "%." + numDecimals + "f", ((Number)
			// value).doubleValue() * 100);
			case TIME:

				// long time = ((Number) value).longValue();
				//
				// long numMin = time / (1000 * 60);
				//
				// long numSecs = time / 1000;
				//
				// if (numMin == 0) {
				// if (numSecs == 0) {
				// return String.format(locale, (time % 1000) + "ms");
				// } else {
				// return String.format(locale, "%1$tSs", new Date(time));
				// }
				// } else {
				// return String.format(locale, "%1$tMm %1$tSs", new
				// Date(time));
				// }

				long time = ((Number) value).longValue();
				return String.format(locale, "%." + numDecimals + "f", ((float) time) / 1000);

			default:
				return String.format(locale, "%." + numDecimals + "f", value);
			}
		}
	}

	@Override
	public String toString() {
		return "NumberFormat [type=" + type + ", numDecimals=" + numDecimals + "]";
	}

}
