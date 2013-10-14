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

import java.awt.Color;
import java.util.Locale;

import es.optsicom.lib.util.ArraysUtil;

public class Cell {

	private Object value;
	private CellFormat format;
	private Color color;

	public Cell() {
	}
	
	public Cell(Object value, CellFormat format) {
		super();
		this.value = value;
		this.format = format;
	}

	public Cell(Object value) {
		this(value, null);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public CellFormat getFormat() {
		return format;
	}

	public void setFormat(CellFormat format) {
		this.format = format;
	}

	public String format() {
		return format(Locale.getDefault());
	}

	public String format(Locale locale) {
		if (format != null) {
			return format.format(value, locale);
		} else {
			return ArraysUtil.toStringObj(value);
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Value:" + value + " Format:" + format;
	}
}
