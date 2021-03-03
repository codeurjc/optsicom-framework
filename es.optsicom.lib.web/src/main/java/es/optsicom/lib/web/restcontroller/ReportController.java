package es.optsicom.lib.web.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator.ExperimentMethodConf;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.web.dto.ExperimentMethodBasicResponseDTO;
import es.optsicom.lib.web.dto.InstanceBasicResponseDTO;
import es.optsicom.lib.web.dto.ReportConfResponseDTO;
import es.optsicom.lib.web.dto.ReportResponseDTO;
import es.optsicom.lib.web.filter.InstancesFilter;
import es.optsicom.lib.web.service.ExperimentService;
import es.optsicom.lib.web.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private static final Logger log = LoggerFactory.getLogger(ReportController.class);

	private ExperimentService experimentService;
	private ReportService reportService;

	@Autowired
	public ReportController(ExperimentService experimentService,
	        ReportService reportService) {
		this.experimentService = experimentService;
		this.reportService = reportService;
	}

	@GetMapping
	public ResponseEntity<ReportResponseDTO> getExperimentsExplicit(
	        @RequestParam List<Long> expIds,
	        @RequestParam Optional<List<Long>> methodIds,
	        @RequestParam Optional<List<Long>> instanceIds) {

		log.info("==> Recovering Report (/api/reports)");
		log.info("\t==> Experiments ids: {}", expIds.toString());
		log.info("\t==> Methods ids: {}", (methodIds.isPresent() ? methodIds.get().toString() : "All methods"));
		log.info("\t==> Intances: {}", (instanceIds.isPresent() ? instanceIds.get().toString() : "All instances"));

		try {

			Experiment firstExp = this.experimentService.findExperimentManagerById(expIds.get(0)).getExperiment();
			Experiment expWithFewestInstances = null;

			List<Long> selectedMethods = new ArrayList<>();
			List<ExperimentMethodBasicResponseDTO> expMethodNames = new ArrayList<>();
			List<ExperimentMethodConf> expMethods = new ArrayList<>();

			for (Long expId : expIds) {
				ExperimentManager expManager = this.experimentService.findExperimentManagerById(expId);

				// Keep the experiment containing the fewest instances
				if (expWithFewestInstances == null
				        || expManager.getInstances().size() < expWithFewestInstances.getInstances().size()) {
					expWithFewestInstances = expManager.getExperiment();
				}

				expMethodNames.add(new ExperimentMethodBasicResponseDTO(expManager));

				for (MethodDescription method : expManager.getMethods()) {
					if (!methodIds.isPresent() ||
					        (methodIds.isPresent() && methodIds.get().contains(method.getId()))) {
						selectedMethods.add(method.getId());
						expMethods.add(new ExperimentMethodConf(expId, expManager.getExperimentMethodName(method)));
					}
				}
			}

			// Get Instances
			List<InstanceBasicResponseDTO> instances = new ArrayList<>(expWithFewestInstances.getInstances())
			        .stream()
			        .map(instance -> new InstanceBasicResponseDTO(
			                instance.getId(),
			                instance.getName()))
			        .collect(Collectors.toList());

			List<Long> selectedInstances = null;
			List<String> filterInstances = null;

			if (instanceIds.isPresent() && instanceIds.get().size() > 0) {
				selectedInstances = new ArrayList<>(instanceIds.get());

				filterInstances = instances.stream()
				        .filter(instance -> instanceIds.get().contains(instance.getId()))
				        .map(instance -> instance.getName())
				        .collect(Collectors.toList());
			} else {
				selectedInstances = instances.stream()
				        .map(instance -> instance.getId())
				        .collect(Collectors.toList());
			}

			// Configure Report
			FusionerReportCreator reportCreator = new FusionerReportCreator(
			        firstExp.getName(),
			        "",
			        experimentService.getDBManager());
			reportCreator.addExperimentMethods(expMethods);

			// Filter instances
			if (filterInstances != null) {
				reportCreator.setInstacesFilter(new InstancesFilter(filterInstances));
			}

			// Create report
			ReportResponseDTO reportResponseDTO = reportService.createReportRest(
			        new ReportConfResponseDTO(
			                expIds,
			                selectedMethods,
			                expMethodNames,
			                selectedInstances,
			                instances),
			        reportCreator.createReportObject());

			return ResponseEntity.ok().body(reportResponseDTO);

		} catch (NoResultException noResultException) {
			log.error("\t==> Experiment not found");

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
