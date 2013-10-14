package es.optsicom.lib.analyzer;

import java.util.List;

import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.tablecreator.Alias;
import es.optsicom.lib.analyzer.tablecreator.AttributedTableCreator;
import es.optsicom.lib.expresults.manager.ExperimentManager;

public abstract class BlockBuilder {

	private List<Alias> methodAliases;
	private List<Alias> instanceAliases;
	
	private ReportBlock block;
	
	public abstract void buildPages(ExperimentManager experimentResults);
	
	protected void configTableCreator(ExperimentManager experimentResults,
			AttributedTableCreator tableCreator) {
		
		tableCreator.setExperimentResults(experimentResults);
		tableCreator.setBestMode(experimentResults.getProblemBestMode());
		
		tableCreator.setInstanceAliases(instanceAliases);
		tableCreator.setMethodAliases(methodAliases);
		
	}
	
	public void setInstanceAliases(List<Alias> instanceAliases) {
		this.instanceAliases = instanceAliases;
	}
	
	public void setMethodAliases(List<Alias> methodAliases) {
		this.methodAliases = methodAliases;
	}

	protected void setBlock(ReportBlock block) {
		this.block = block;
	}
	
	public ReportBlock getBlock() {
		return this.block;
	}
}
