import { Injectable } from '@angular/core';
import { BreadcrumbService } from '../breadcrumb/breadcrumb.service';
import { Title } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {
  constructor(
    private breadcrumbService: BreadcrumbService,
    private titleService: Title
  ) {
    this.breadcrumbService.addFriendlyNameForRoute("/experiments", "Home / Experiments");
  }

  public setHeadTitle(title: string) {
    this.titleService.setTitle(title);
  }
  
}
