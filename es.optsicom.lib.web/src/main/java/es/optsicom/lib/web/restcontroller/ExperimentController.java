package es.optsicom.lib.web.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.web.model.experimentrest.ExperimentRest;
import es.optsicom.lib.web.model.experimentrest.MethodName;
import es.optsicom.lib.web.service.ExperimentService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {

	private static final Logger log = LoggerFactory.getLogger(ExperimentController.class);

	private ExperimentService experimentService;

	public ExperimentController(ExperimentService experimentService) {
		this.experimentService = experimentService;
	}

	@GetMapping
	public ResponseEntity<List<Experiment>> getExperimentsExplicit() {
		log.info("Recovering experiments (/experiments)");

		return ResponseEntity.ok().body(this.experimentService.findExperiments());
	}

	@GetMapping(value = "/{expId}")
	public ResponseEntity<ExperimentRest> getExperimentById(@PathVariable Long expId) {
		log.info("Recovering experiment: " + expId);

		ExperimentManager expManager = this.experimentService.findExperimentManagerById(expId);
		List<MethodName> methodNames = new ArrayList<MethodName>();

		for (MethodDescription method : expManager.getMethods()) {
			log.info("\t Recovering methods: " + method.getName());
			
			methodNames.add(new MethodName(expManager.getExperimentMethodName(method), method));
		}

		ExperimentRest expInfo = new ExperimentRest(expManager.getExperiment(), methodNames);

		return ResponseEntity.ok().body(expInfo);
	}

	@DeleteMapping(value = "/{expId}")
	public ResponseEntity<Void> deleteExperimentById(@PathVariable Long expId) {
		log.info("Removing experiment: " + expId);

		experimentService.removeExperiment(expId);

		return ResponseEntity.ok().build();
	}
}
