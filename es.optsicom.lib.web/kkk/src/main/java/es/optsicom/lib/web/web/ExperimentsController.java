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

import es.optsicom.lib.web.service.ExperimentService;

@Controller
public class ExperimentsController {

	@Autowired
	private ExperimentService experimentService;

	@RequestMapping("/experiments")
	public String showExperiments(ModelMap model) {
		
		model.addAttribute("exps", this.experimentService.findExperiments());
		
		return "experiments";
	}
	
	@RequestMapping("/experiment")
	public String showExperiment(@RequestParam("expId") long experimentId, ModelMap model) {
		
		model.addAttribute("exp", this.experimentService.findExperimentManagerById(experimentId));
		
		return "experiment";
	}
}
