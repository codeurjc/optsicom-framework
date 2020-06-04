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
package es.optsicom.lib.analyzer.tablecreator.pr;

import java.util.Arrays;
import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.AnalysisException;
import es.optsicom.lib.expresults.model.Event;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.util.SummarizeMode;

public class LastEventRP extends SummarizeRawProcessor {

	public enum Source {
		VALUE, TIMESTAMP
	}

	private String eventName;
	private long timelimit = -1;
	private Source source = Source.VALUE;
	private Double defaultValueIfNull = null;

	public LastEventRP(String eventName, SummarizeMode summarizeMode, long timelimit, Source source) {
		this.eventName = eventName;
		this.summarizeMode = summarizeMode;
		this.timelimit = timelimit;
		this.source = source;
	}

	public LastEventRP(String eventName, long timelimit) {
		this.eventName = eventName;
		this.timelimit = timelimit;
	}

	public LastEventRP(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public List<String> getCookedEventsNames() {
		return Arrays.asList(eventName + " " + source);
	}

	@Override
	public Double[] cookEvents(Execution exec) {

		Double value = defaultValueIfNull;

		Event event = exec.getLastEvent(eventName, timelimit);

		if (event != null) {

			switch (source) {
			case TIMESTAMP:
				value = ((double) event.getTimestamp()) / 1000;
				break;
			case VALUE:
				Object valueAsObj = event.getValue();
				if (valueAsObj instanceof Number) {
					value = ((Number) valueAsObj).doubleValue();
				} else {
					try {
						value = Double.parseDouble(valueAsObj.toString());
					} catch (NumberFormatException e) {
						throw new AnalysisException("The event " + eventName + " is not a number");
					}
				}
				break;
			default:
				throw new AnalysisException("Option \"" + source + "\"not supported");
			}
		}

		// System.out.println("Result: "+value);
		return new Double[] { value };

	}

	@Override
	public LastEventRP setSummarizeMode(SummarizeMode summarizeMode) {
		this.summarizeMode = summarizeMode;
		return this;
	}

	public long getTimelimit() {
		return timelimit;
	}

	public LastEventRP setTimelimit(long timelimit) {
		this.timelimit = timelimit;
		return this;
	}

	public Source getSource() {
		return source;
	}

	public LastEventRP setSource(Source source) {
		this.source = source;
		return this;
	}

	public double getDefaultValueIfNull() {
		return defaultValueIfNull;
	}

	public LastEventRP setDefaultValueIfNull(double defaultValueIfNull) {
		this.defaultValueIfNull = defaultValueIfNull;
		return this;
	}

}
