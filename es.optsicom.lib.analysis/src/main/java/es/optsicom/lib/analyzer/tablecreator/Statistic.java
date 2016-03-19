package es.optsicom.lib.analyzer.tablecreator;

import es.optsicom.lib.analyzer.report.table.NumericFormat.NumberType;
import es.optsicom.lib.analyzer.tablecreator.pr.RawProcessor;
import es.optsicom.lib.analyzer.tablecreator.statisticcalc.StatisticCalc;
import es.optsicom.lib.util.description.Descriptive;
import es.optsicom.lib.util.description.MemoryProperties;

public class Statistic implements Descriptive {

	private StatisticGroup statisticGroup;
	private StatisticCalc statisticCalc;
	private RawProcessor rawProcessor;
	private int cookedEventIndex;

	private MemoryProperties properties;
	private boolean propertiesFilled = false;

	public Statistic(StatisticGroup statisticGroup, RawProcessor rawProcessor, StatisticCalc statisticCalc,
			int cookedEventIndex, String name) {
		this.statisticGroup = statisticGroup;
		this.rawProcessor = rawProcessor;
		this.statisticCalc = statisticCalc;
		this.cookedEventIndex = cookedEventIndex;
		this.properties = new MemoryProperties(name);
	}

	public Statistic(StatisticCalc statisticCalc, String name) {
		this.statisticCalc = statisticCalc;
		this.properties = new MemoryProperties(name);
	}

	public void configureStatisticGroup(StatisticGroup statisticGroup, RawProcessor rawProcessor,
			int cookedEventIndex) {
		this.statisticGroup = statisticGroup;
		this.rawProcessor = rawProcessor;
		this.cookedEventIndex = cookedEventIndex;
	}

	public MemoryProperties getProperties() {

		if (!propertiesFilled) {

			propertiesFilled = true;

			String cookedEventName = rawProcessor.getCookedEventsNames().get(cookedEventIndex);

			properties.put("source", cookedEventName);
			properties.put("statistic", statisticCalc.getName());
		}

		return properties;
	}

	public NumberType getNumberType() {
		return statisticCalc.getNumberType();
	}

	public StatisticGroup getStatisticGroup() {
		return statisticGroup;
	}

	public StatisticCalc getStatisticCalc() {
		return statisticCalc;
	}

	public String getName() {
		return properties.getName();
	}

	@Override
	public String toString() {
		return getProperties().getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statistic other = (Statistic) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}

}
