package es.optsicom.lib.web.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.web.dto.ExperimentBasicResponseDTO;
import es.optsicom.lib.web.dto.ExperimentExtendResponseDTO;
import es.optsicom.lib.web.service.ExperimentService;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {

	private static final Logger log = LoggerFactory.getLogger(ExperimentController.class);

	private ExperimentService experimentService;

	@Autowired
	public ExperimentController(ExperimentService experimentService) {
		this.experimentService = experimentService;
	}

	@GetMapping("/")
	public List<ExperimentBasicResponseDTO> getExperimentsExplicit() {
		log.info("==> Recovering experiments (/api/experiments/)");

		return this.experimentService.findExperiments()
		        .stream()
		        .map(exp -> new ExperimentBasicResponseDTO(exp))
		        .collect(Collectors.toList());
	}

	@GetMapping(value = "/{expId}")
	public ResponseEntity<ExperimentExtendResponseDTO> getExperimentById(@PathVariable Long expId) {
		log.info("==> Recovering experiment {} (/api/experiments/{}/)", expId, expId);

		try {
			return ResponseEntity.ok().body(
			        new ExperimentExtendResponseDTO(this.experimentService.findExperimentManagerById(expId)));

		} catch (NoResultException noResultException) {
			log.error("\t==> Experiment not found");

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/{expId}")
	public ResponseEntity<ExperimentBasicResponseDTO> deleteExperimentById(@PathVariable Long expId) {
		log.info("==> Removing experiment {} (/api/experiments/{}/)", expId, expId);

		try {
			Experiment exp = this.experimentService.findExperimentById(expId);
			experimentService.removeExperiment(exp.getId());

			return ResponseEntity.ok().body(new ExperimentBasicResponseDTO(exp));

		} catch (NoResultException noResultException) {
			log.error("\t==> Experiment not found");

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
