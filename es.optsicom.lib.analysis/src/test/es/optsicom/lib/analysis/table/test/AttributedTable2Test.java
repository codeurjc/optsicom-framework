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

import es.optsicom.lib.analyzer.report.table.SheetTable;
import es.optsicom.lib.analyzer.report.table.TextSpaceSheetFormatter;
import es.optsicom.lib.analyzer.tablecreator.atttable.Attribute;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTable;
import es.optsicom.lib.analyzer.tablecreator.atttable.AttributedTableSheetCreator;
import es.optsicom.lib.analyzer.tablecreator.atttable.TableValue;

public class AttributedTable2Test {

	public static void main(String[] args) {

		AttributedTable attributedTable = new AttributedTable();

		int[] times = { 3, 30, 300 };
		String[] methods = { "Alg1", "Alg2", "Alg3" };
		String[] instances = { "Inst1", "Inst2", "Inst3" };
		String[] statistics = { "Dev", "#Best", "Rank" };

		for (int i = 0; i < times.length; i++) {
			for (int j = 0; j < methods.length; j++) {
				for (int k = 0; k < instances.length; k++) {
					for (int l = 0; l < statistics.length; l++) {
						attributedTable.addValue(new TableValue((double)i + j + k), null,
								new Attribute("time", times[i]), new Attribute(
										"method", methods[j]), new Attribute(
										"instance", instances[k]),
								new Attribute("statistic", statistics[l]));
					}
				}
			}
		}

		showTable(attributedTable,
				new String[] { "time", "method", "statistic" },
				new String[] { "instance" });
		showTable(attributedTable, new String[] { "time", "statistic" },
				new String[] { "instance", "method" });
		showTable(attributedTable, new String[] { "method" }, new String[] {
				"instance", "time", "statistic" });

	}

	private static void showTable(AttributedTable attributedTable,
			String[] rows, String[] cols) {
		AttributedTableSheetCreator atsc = new AttributedTableSheetCreator();
		atsc.setColsAttributes(rows);
		atsc.setRowsAttributes(cols);

		SheetTable sheet = atsc.createSheet(attributedTable);
		TextSpaceSheetFormatter sf = new TextSpaceSheetFormatter();
		sf.format(sheet);
		System.out.println(sf.getFormattedTable());
	}

}
