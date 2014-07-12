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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.optsicom.lib.analyzer.report.Report;
import es.optsicom.lib.analyzer.tool.FusionerReportCreator;
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
	public String showExperimentReport(@RequestParam("expId") long experimentId, ModelMap model) {

		FusionerReportCreator reportCreator = new FusionerReportCreator(experimentService.getDBManager());
		reportCreator.addExperimentMethod(experimentId);
		// reportCreator.addExperimentMethods(Arrays.asList(new ExperimentMethodConf("predefined", "best_values")));

		Report report = reportCreator.createReportObject();
		model.addAttribute("report", report);

		return "experimentreport";
	}

	@RequestMapping("/experimentreportnew")
	public String showExperimentReportNew(@RequestParam("expId") long experimentId, ModelMap model) {

		FusionerReportCreator reportCreator = new FusionerReportCreator(experimentService.getDBManager());
		reportCreator.addExperimentMethod(experimentId);
		// reportCreator.addExperimentMethods(Arrays.asList(new ExperimentMethodConf("predefined", "best_values")));

		Report report = reportCreator.createReportObject();
		model.addAttribute("report", report);
		model.addAttribute("experimentId", experimentId);
		model.addAttribute("exp", this.experimentService.findExperimentManagerById(experimentId));

		return "experimentreportnew";
	}
}
