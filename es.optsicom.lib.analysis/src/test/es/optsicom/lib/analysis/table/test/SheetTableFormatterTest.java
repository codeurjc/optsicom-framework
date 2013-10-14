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
package es.optsicom.lib.analysis.table.test;

import static es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType.DECIMAL;
import static es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType.INTEGER;
import static es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType.PERCENT;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import es.optsicom.lib.analyzer.report.table.NumericFormat;
import es.optsicom.lib.analyzer.report.table.SheetTable;
import es.optsicom.lib.analyzer.report.table.SimpleTable;
import es.optsicom.lib.analyzer.report.table.TextSpaceSheetFormatter;
import es.optsicom.lib.analyzer.report.table.TextTabSheetFormatter;
import es.optsicom.lib.util.RandomManager;

public class SheetTableFormatterTest {

	public static void main(String[] args) {

		double[][] values = new double[10][3];

		Random r = RandomManager.getRandom();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 3; j++) {
				values[i][j] = r.nextDouble() * 1000;
			}
		}

		SimpleTable t = new SimpleTable(Arrays.asList("1rAxxxxxx", "2rBxx", "3rCxxxxxxx", "4ss", "5opop", "6werwr",
		        "7sdfsdfsf", "8,,,dmmm", "9ioioio", "10xxxx"), Arrays.asList("cAxxxxx", "cBxxx", "cC"), values);
		t.orderByColumn(0);
		t.setNumberFormats(Arrays.asList(new NumericFormat(DECIMAL, 2), new NumericFormat(INTEGER), new NumericFormat(
		        PERCENT, 3)));

		SheetTable st = t.createSheetTable();

		TextTabSheetFormatter f = new TextTabSheetFormatter();
		f.setLocale(Locale.ENGLISH);
		f.format(st);

		System.out.println(f.getFormattedTable());

		TextSpaceSheetFormatter sf = new TextSpaceSheetFormatter();
		sf.setLocale(Locale.ENGLISH);
		sf.format(st);

		System.out.println(sf.getFormattedTable());

	}
}
