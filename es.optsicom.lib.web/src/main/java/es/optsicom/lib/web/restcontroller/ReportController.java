package es.optsicom.lib.web.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.web.model.experimentrest.MethodName;
import es.optsicom.lib.web.model.reportrest.ReportRest;
import es.optsicom.lib.web.model.reportrest.ReportRestBuilder;
import es.optsicom.lib.web.model.reportrest.ReportRestConfiguration;
import es.optsicom.lib.web.service.ExperimentService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private static final Logger log = LoggerFactory.getLogger(ReportController.class);

	private ExperimentService experimentService;

	@Autowired
	public ReportController(ExperimentService experimentService) {
		this.experimentService = experimentService;
	}

	@GetMapping
	public ResponseEntity<ReportRest> getExperimentsExplicit(
	        @RequestParam List<Long> expIds,
	        @RequestParam Optional<List<Long>> methodIds) {

		Experiment experiment = this.experimentService.findExperimentManagerById(expIds.get(0)).getExperiment();
		FusionerReportCreator reportCreator = new FusionerReportCreator(experiment.getProblemName(), "",
		        experimentService.getDBManager());

		List<Long> selectedMethods = new ArrayList<>();
		List<MethodName> methodNames = new ArrayList<>();

		log.info("Create Report width Experiments Ids: " + expIds.toString() + " and Methods Ids: "
		        + (methodIds.isPresent() ? methodIds.get().toString() : "All Methods"));

		for (Long expId : expIds) {

			ExperimentManager expManager = this.experimentService.findExperimentManagerById(expId);
			for (MethodDescription method : expManager.getMethods()) {
				String experimentMethodName = expManager.getExperimentMethodName(method);

				methodNames.add(new MethodName(expManager.getExperimentMethodName(method), method));

				if (!methodIds.isPresent() || (methodIds.isPresent() && methodIds.get().contains(method.getId()))) {
					selectedMethods.add(method.getId());
					reportCreator.addExperimentMethod(expId, experimentMethodName);
				}
			}
		}

		ReportRestBuilder reportRestBuilder = new ReportRestBuilder();
		ReportRestConfiguration configuration = new ReportRestConfiguration(expIds, selectedMethods, methodNames);
		ReportRest report = reportRestBuilder.buildReportRest(configuration, reportCreator.createReportObject());

		return ResponseEntity.ok().body(report);
	}

}
