import { Injectable } from '@angular/core';
import { BreadcrumbService } from '../common-components/breadcrumb/breadcrumb.service';
import { Title } from '@angular/platform-browser';
import { ExperimentExtendResponseDTO } from '../classes/experiment';

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
    this.breadcrumbService.addFriendlyNameForRoute("/errorpage", "Error Page");
    this.breadcrumbService.addFriendlyNameForRouteRegex(".*/report", "Report");
    this.breadcrumbService.addFriendlyNameForRouteRegex(".*/compare", "Comparative Report");
  }

  public setHeadTitle(title: string) {
    this.titleService.setTitle(title);
  }

  public addExperimentName(experiment: ExperimentExtendResponseDTO) {
    this.breadcrumbService.addFriendlyNameForRouteRegex('.*/experiments/' + experiment.id + '$', experiment.name);
  }
  
}
