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
package es.optsicom.lib.exact.bb;

import java.math.BigInteger;

public class SearchTreeNodeCalculator {

	public static long totalTreeNodes(int n, int m) {
		long valor = 0;
		BigInteger facDen2 = factorial(n - m);
		for (int i = 0; i < m; i++) {
			BigInteger facNum = factorial(n - i);
			BigInteger facDen1 = factorial(m - i);
			valor += facNum.divide(facDen1.multiply(facDen2)).longValue();
		}
		return valor;
	}

	private static BigInteger factorial(int number) {
		BigInteger valor = BigInteger.ONE;
		for (int i = number; i > 1; i--) {
			valor = valor.multiply(BigInteger.valueOf(i));
		}
		return valor;
	}

}
