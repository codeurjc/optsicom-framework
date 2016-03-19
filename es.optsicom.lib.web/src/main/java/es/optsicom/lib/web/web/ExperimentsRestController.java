package es.optsicom.lib.web.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.report.ReportBlock;
import es.optsicom.lib.analyzer.report.ReportElement;
import es.optsicom.lib.analyzer.report.ReportPage;
import es.optsicom.lib.analyzer.report.table.Table;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator.ExperimentMethodConf;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.web.model.ExperimentMethodName;
import es.optsicom.lib.web.model.ReportConfiguration;
import es.optsicom.lib.web.model.ReportRest;
import es.optsicom.lib.web.model.ReportTable;
import es.optsicom.lib.web.model.builder.ReportTableBuilder;
import es.optsicom.lib.web.service.ExperimentService;

@Controller
@RequestMapping("/api")
public class ExperimentsRestController {
	private ExperimentService experimentService;
	private static final Log LOG = LogFactory.getLog(ExperimentsRestController.class);

	@Autowired
	public ExperimentsRestController(ExperimentService experimentservice) {
		this.experimentService = experimentservice;
	}

	public long convertStringToLong(String string) {
		if ((string != null) && (string != "")) {
			return Long.valueOf(string).longValue();
		}
		return 0;
	}

	public List<Long> convertListStringToListLong(List<String> listString) {
		List<Long> listLong = new ArrayList<Long>();
		for (int index = 0; index < listString.size(); index++) {
			listLong.add(convertStringToLong(listString.get(index)));
		}
		return listLong;
	}

	@RequestMapping(value = "/{expId}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody ExperimentManager getExperimentById(@PathVariable String expId) {
		LOG.info("Recovering experiment: " + expId);
		return this.experimentService.findExperimentManagerById(convertStringToLong(expId));
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Experiment> getExperiments() {
		LOG.info("Recovering experiments (/)");
		return this.experimentService.findExperiments();
	}

	@RequestMapping(value = "/experiments", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Experiment> getExperimentsExplicit() {
		LOG.info("Recovering experiments (/experiments)");
		return this.experimentService.findExperiments();
	}

	@RequestMapping(value = "/{expId}", method = RequestMethod.DELETE, produces = { "application/json" })
	public @ResponseBody void deleteExperimentById(@PathVariable String expId) {
		LOG.info("Removing experiment: " + expId);
		experimentService.removeExperiment(convertStringToLong(expId));
	}

	@RequestMapping(value = "/{expIds}/experimentNameMethod", method = RequestMethod.GET, produces = {
			"application/json" })
	public @ResponseBody List<ExperimentMethodName> getMethodNameById(
			@PathVariable("expIds") final List<String> expIds) {
		LOG.info("Starting /{expIds}/experimentNameMethod ...");
		List<ExperimentMethodName> methodNames = new ArrayList<ExperimentMethodName>();
		for (String expId : expIds) {
			LOG.info("Recovering methods name from expId: " + expId);
			long expIdLong = convertStringToLong(expId);
			ExperimentManager expManager = this.experimentService.findExperimentManagerById(expIdLong);
			for (MethodDescription method : expManager.getMethods()) {
				String experimentMethodName = expManager.getExperimentMethodName(method);
				Long methodId = method.getId();
				methodNames.add(new ExperimentMethodName(methodId, experimentMethodName));
			}
		}
		LOG.info(methodNames.size() + " methodNames exist in the list");
		return methodNames;
	}

	@RequestMapping(value = "/report/exp-ids/{expIds}/method-ids/{methodIds}/best-values/{bestValues}/configuration/{configuration}", method = RequestMethod.GET, produces = {
			"application/json" })
	public @ResponseBody ReportRest report(@PathVariable("expIds") final List<String> expIdsAux,
			@PathVariable("methodIds") final List<String> methodIdsAux,
			@PathVariable("bestValues") final boolean bestValues,
			@PathVariable("configuration") final boolean configuration) {
		LOG.info("Report: " + expIdsAux);
		final List<Long> expIds = convertListStringToListLong(expIdsAux);
		final Long expId = expIds.get(0); // report only needs one expId
		final List<Long> methodIds = convertListStringToListLong(methodIdsAux);
		ReportConfiguration reportConfiguration = new ReportConfiguration(expIds, methodIds, bestValues, configuration);

		Experiment experiment = this.experimentService.findExperimentManagerById(expId).getExperiment();
		FusionerReportCreator reportCreator = new FusionerReportCreator(experiment.getProblemName(), "",
				experimentService.getDBManager());
		ExperimentManager expManager = this.experimentService.findExperimentManagerById(expId);
		if (!reportConfiguration.isConfiguration()) {
			reportConfiguration.setMethods(new ArrayList<Long>());
			for (MethodDescription method : expManager.getMethods()) {
				String experimentMethodName = expManager.getExperimentMethodName(method);
				reportCreator.addExperimentMethod(expId, experimentMethodName);
				reportConfiguration.addMethod(method.getId());
			}
		} else {
			for (MethodDescription method : expManager.getMethods()) {
				String experimentMethodName = expManager.getExperimentMethodName(method);
				if ((reportConfiguration.getMethods()).contains(method.getId())) {
					reportCreator.addExperimentMethod(expId, experimentMethodName);
				}
			}
		}
		if (reportConfiguration.isBestValues()) {
			reportCreator.addExperimentMethods(Arrays.asList(new ExperimentMethodConf("predefined", "best_values")));
		}
		Report report = reportCreator.createReportObject();
		List<ReportTable> rTables = generateReportTables(report);
		ReportRest rWeb = new ReportRest(reportConfiguration, rTables);
		LOG.info("Report created");
		return rWeb;
	}

	@RequestMapping(value = "/merge/exp-ids/{expIds}/method-ids/{methodIds}/best-values/{bestValues}/configuration/{configuration}", method = RequestMethod.GET, produces = {
			"application/json" })
	public @ResponseBody ReportRest merge(@PathVariable("expIds") final List<String> expIdsAux,
			@PathVariable("methodIds") final List<String> methodIdsAux,
			@PathVariable("bestValues") final boolean bestValues,
			@PathVariable("configuration") final boolean configuration) {
		LOG.info("Merging experiments (/merge) : ");
		if (expIdsAux.isEmpty()) {
			LOG.info("No experiments selected");
			throw new RuntimeException("No experiments selected");
		}
		final List<Long> expIds = convertListStringToListLong(expIdsAux);
		final List<Long> methodIds = convertListStringToListLong(methodIdsAux);
		ReportConfiguration reportConfiguration = new ReportConfiguration(expIds, methodIds, bestValues, configuration);
		final Long expId = expIds.get(0);
		Experiment experiment = this.experimentService.findExperimentManagerById(expId).getExperiment();
		FusionerReportCreator reportCreator = new FusionerReportCreator(experiment.getProblemName(), "",
				experimentService.getDBManager());

		if (!reportConfiguration.isConfiguration()) {
			reportConfiguration.setMethods(new ArrayList<Long>());
			for (Long experimentId : expIds) {
				ExperimentManager expManager = this.experimentService.findExperimentManagerById(experimentId);

				for (MethodDescription method : expManager.getMethods()) {
					String experimentMethodName = expManager.getExperimentMethodName(method);
					reportCreator.addExperimentMethod(experimentId, experimentMethodName);
					reportConfiguration.addMethod(method.getId());
				}
			}
		} else {
			for (Long experimentId : expIds) {
				ExperimentManager expManager = this.experimentService.findExperimentManagerById(experimentId);

				for (MethodDescription method : expManager.getMethods()) {
					String experimentMethodName = expManager.getExperimentMethodName(method);
					if ((reportConfiguration.getMethods()).contains(method.getId())) {
						reportCreator.addExperimentMethod(experimentId, experimentMethodName);
					}
				}
			}
		}

		if (reportConfiguration.isBestValues()) {
			reportCreator.addExperimentMethods(Arrays.asList(new ExperimentMethodConf("predefined", "best_values")));
		}

		Report report = reportCreator.createReportObject();
		List<ReportTable> rTables = generateReportTables(report);
		ReportRest rWeb = new ReportRest(reportConfiguration, rTables);
		LOG.info("Merge(Report) created");
		return rWeb;
	}

	private List<ReportTable> generateReportTables(Report report) {
		ReportTableBuilder reportTableBuilder = new ReportTableBuilder();
		List<ReportTable> rTables = new ArrayList<ReportTable>();
		for (ReportBlock reportblock : report.getReportBlocks()) {
			for (ReportPage reportpage : reportblock.getReportPages()) {
				for (ReportElement reportElement : reportpage.getReportElements()) {
					Table table = (Table) reportElement;
					rTables.add(reportTableBuilder.build(table));
				}
			}
		}
		return rTables;
	}

}
