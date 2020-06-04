package es.optsicom.lib.analyzer.tablecreator.statisticcalc;

import java.util.List;

import es.optsicom.lib.analyzer.tablecreator.pr.RawProcessor;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Execution;
import es.optsicom.lib.expresults.model.InstanceDescription;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.util.ArraysUtil;
import es.optsicom.lib.util.SummarizeMode;

public class LastRPRelativeValueProvider implements RelativeValueProvider {

	private RawProcessor rawProcessor;
	private SummarizeMode summarizeMode;
	private int numValue;

	public LastRPRelativeValueProvider(RawProcessor rawProcessor, int numValue, SummarizeMode summarizeMode) {
		this.rawProcessor = rawProcessor;
		this.numValue = numValue;
		this.summarizeMode = summarizeMode;
	}

	public Number getValue(InstanceDescription instance, ExperimentManager experimentResults) {

		Double[] values = ArraysUtil.createDoubleArray(experimentResults.getMethods().size());

		int numMethod = 0;
		for (MethodDescription method : experimentResults.getMethods()) {
			List<Execution> execs = experimentResults.getExecutions(instance, method);
			values[numMethod] = rawProcessor.cookEvents(execs)[numValue];
			numMethod++;
		}

		return summarizeMode.summarizeValues(values);
	}

}
