/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.optsicom.lib.web.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator.ExperimentMethodConf;
import es.optsicom.lib.expresults.manager.ExperimentManager;
import es.optsicom.lib.expresults.model.Experiment;
import es.optsicom.lib.expresults.model.MethodDescription;
import es.optsicom.lib.web.service.ExperimentService;

@Controller
public class ExperimentsController {

	@Autowired
	private ExperimentService experimentService;

	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}

	@RequestMapping("/experiments")
	public String showExperiments(ModelMap model) {

		model.addAttribute("exps", this.experimentService.findExperiments());

		return "experiments";
	}

	@RequestMapping("/")
	public String showIndex(ModelMap model) {

		model.addAttribute("exps", this.experimentService.findExperiments());

		return "experiments";
	}

	@RequestMapping("/experiment")
	public String showExperiment(@RequestParam("expId") long experimentId, ModelMap model) {

		model.addAttribute("expId", experimentId);
		model.addAttribute("exp", this.experimentService.findExperimentManagerById(experimentId));

		return "experiment";
	}

	@RequestMapping("/experimentreport")
	public String showExperimentReport(@RequestParam("expId") long experimentId,
			@RequestParam(value = "reportconf", required = false) String reportConf,
			@RequestParam(value = "bestValues", required = false) String bestValues,
			@RequestParam(value = "methods", required = false) List<String> methods, ModelMap model) {

		Experiment experiment = this.experimentService.findExperimentById(experimentId);

		ExperimentManager expManager = this.experimentService.findExperimentManagerById(experimentId);

		FusionerReportCreator reportCreator = new FusionerReportCreator(experiment.getProblemName(), "",
				experimentService.getDBManager());

		if (reportConf == null) {
			reportCreator.addExperimentMethod(experimentId);
			methods = new ArrayList<String>();
			for (MethodDescription method : expManager.getMethods()) {
				methods.add(Long.toString(method.getId()));
			}
		} else {
			Map<String, MethodDescription> methodsById = new HashMap<>();
			for (MethodDescription method : expManager.getMethods()) {
				methodsById.put(Long.toString(method.getId()), method);
			}

			for (String methodId : methods) {
				reportCreator.addExperimentMethod(experimentId,
						expManager.getExperimentMethodName(methodsById.get(methodId)));
			}
		}

		if (bestValues != null) {
			reportCreator.addExperimentMethods(Arrays.asList(new ExperimentMethodConf("predefined", "best_values")));
		}

		Report report = reportCreator.createReportObject();
		model.addAttribute("report", report);

		model.addAttribute("experimentId", experimentId);

		model.addAttribute("exp", expManager);
		model.addAttribute("methods", methods);
		model.addAttribute("bestValues", bestValues);

		return "experimentreport";
	}

	@RequestMapping("/removeexperiment")
	public String removeExperiment(@RequestParam("expId") long experimentId, ModelMap model) {

		experimentService.removeExperiment(experimentId);

		return "redirect:experiments";
	}

	@RequestMapping("/experimentreportnew")
	public String showExperimentReportNew(@RequestParam("expId") long experimentId, ModelMap model) {

		FusionerReportCreator reportCreator = new FusionerReportCreator(experimentService.getDBManager());
		reportCreator.addExperimentMethod(experimentId);
		// reportCreator.addExperimentMethods(Arrays.asList(new
		// ExperimentMethodConf("predefined", "best_values")));

		Report report = reportCreator.createReportObject();
		model.addAttribute("report", report);
		model.addAttribute("experimentId", experimentId);
		model.addAttribute("exp", this.experimentService.findExperimentManagerById(experimentId));

		return "experimentreportnew";
	}

	@RequestMapping("/fusionexperiments")
	public String showExperimentReport(@RequestParam("expIds") List<Long> expIds,
			@RequestParam(value = "reportconf", required = false) String reportConf,
			@RequestParam(value = "bestValues", required = false) String bestValues,
			@RequestParam(value = "methods", required = false) List<String> methods, ModelMap model) {

		if (expIds.isEmpty()) {
			throw new RuntimeException("No experiments selected");
		}

		Experiment experiment = this.experimentService.findExperimentById(expIds.get(0));

		FusionerReportCreator reportCreator = new FusionerReportCreator(experiment.getProblemName(), "",
				experimentService.getDBManager());

		List<MethodDescription> methodObjs = new ArrayList<>();
		List<String> methodNames = new ArrayList<>();

		if (reportConf == null) {

			methods = new ArrayList<>();
			for (Long experimentId : expIds) {

				ExperimentManager expManager = this.experimentService.findExperimentManagerById(experimentId);

				for (MethodDescription method : expManager.getMethods()) {

					String experimentMethodName = expManager.getExperimentMethodName(method);
					reportCreator.addExperimentMethod(experimentId, experimentMethodName);

					methods.add(Long.toString(method.getId()));
					methodObjs.add(method);
					methodNames.add(experimentMethodName);
				}
			}

		} else {

			for (Long experimentId : expIds) {

				ExperimentManager expManager = this.experimentService.findExperimentManagerById(experimentId);

				for (MethodDescription method : expManager.getMethods()) {

					String experimentMethodName = expManager.getExperimentMethodName(method);

					methodObjs.add(method);
					methodNames.add(experimentMethodName);

					if (methods.contains(Long.toString(method.getId()))) {
						reportCreator.addExperimentMethod(experimentId, experimentMethodName);
					}
				}
			}
		}

		if (bestValues != null) {
			reportCreator.addExperimentMethods(Arrays.asList(new ExperimentMethodConf("predefined", "best_values")));
		}

		Report report = reportCreator.createReportObject();
		model.addAttribute("report", report);

		model.addAttribute("methods", methods);
		model.addAttribute("methodObjs", methodObjs);
		model.addAttribute("methodNames", methodNames);
		model.addAttribute("bestValues", bestValues);
		model.addAttribute("expIds", expIds);

		return "fusionexperiments";
	}
}
