import { Injectable } from '@angular/core';
import { BreadcrumbService } from '../common-components/breadcrumb/breadcrumb.service';
import { Title } from '@angular/platform-browser';
import { Experiment } from '../classes/experiment';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {
  constructor(
    private breadcrumbService: BreadcrumbService,
    private titleService: Title
  ) {
    this.breadcrumbService.addFriendlyNameForRoute("/experiments", "Experiments");
    this.breadcrumbService.addFriendlyNameForRoute("/login", "Login Page");
    this.breadcrumbService.addFriendlyNameForRouteRegex(".*/report", "Report");
    this.breadcrumbService.addFriendlyNameForRouteRegex(".*/comparereport", "Comparative Report");
  }

  public setHeadTitle(title: string) {
    this.titleService.setTitle(title);
  }

  public addExperimentName(experiment: Experiment) {
    this.breadcrumbService.addFriendlyNameForRouteRegex('.*/experiments/' + experiment.id + '$', experiment.name);
  }
  
}
