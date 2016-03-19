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
package es.optsicom.lib.instancefile;

import es.optsicom.lib.Instance;

/**
 * This class is used to transform from a Instance type to another Instance
 * type. This is usefull when we two problems that work with the same instances,
 * but we want create diferent objects Instance for each problem. For example,
 * with the MMDP problem, we want use the same instances than the MDP problem,
 * but we want create objects from MMDPInstance instead of MMDInstance.
 * 
 * @author mica
 *
 */
public abstract class InstanceTransformer<IS extends Instance, ID extends Instance> {

	public abstract ID transform(IS instanceSource);

}
