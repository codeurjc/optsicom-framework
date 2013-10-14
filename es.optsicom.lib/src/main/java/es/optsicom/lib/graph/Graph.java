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
package es.optsicom.lib.graph;

import java.util.List;

/**
 * @author mica
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface Graph {

	List<Node> getNodes();

	Node getNode(String identifier);

	Node getNode(int index);

	float getWeight(int i, int j);

	float getWeight(Node v, Node w);

	List<Arc> getArcs();

	int getNumNodes();

	String getAdditionalInfo();

}
