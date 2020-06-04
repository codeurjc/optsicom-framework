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
package es.optsicom.lib.instancefile.test;

import java.io.File;

import es.optsicom.lib.instancefile.InstanceFile;

public class AnnotationsTest {

	public static void main(String[] args) {

		InstanceFile instance = new InstanceFile(null, new File("kk"), "useCase", "instanceSetId", "instanceName");
		System.out.println(instance.toString());

	}

}
