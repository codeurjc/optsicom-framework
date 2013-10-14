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
package es.optsicom.lib.analyzer.tablecreator.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.expresults.model.ElementDescription;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.Properties;

public class ExplicitElementsFilter extends ElementFilter {

	private List<String> allowedElements;

	public ExplicitElementsFilter(List<String> allowedElements) {
		this.allowedElements = allowedElements;
	}

	public ExplicitElementsFilter(String... allowedElements) {
		this(Arrays.asList(allowedElements));
	}

	public static ExplicitElementsFilter createWithElementDescriptions(List<? extends ElementDescription> elements){
		List<String> allowedElements = new ArrayList<String>();
		for(ElementDescription element : elements){
			allowedElements.add(element.getProperties().toString());
		}
		return new ExplicitElementsFilter(allowedElements);
	}
	
	@Override
	public boolean isAllowed(Properties properties) {
		//String id = getId(properties);
		String id = properties.toString();
		//System.out.println("Prop:" + id);
		for (String element : allowedElements) {
			if (element.equals(id)) {
				return true;
			}
		}
		return false;
		//return allowedElements.contains(getId(properties));
	}

}
