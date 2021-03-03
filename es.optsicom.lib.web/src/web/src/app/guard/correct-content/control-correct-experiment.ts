import { Injectable } from '@angular/core';
import { of as observableOf } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Router, ActivatedRouteSnapshot, CanActivate } from '@angular/router';
import { ExperimentsService } from 'src/app/services/experiments.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NavigationService } from 'src/app/services/navigation.service';

@Injectable({
  providedIn: 'root'
})
export class ControlCorrectExperimentGuard implements CanActivate {

  constructor(private router: Router, private experimentsService: ExperimentsService, private authenticationService: AuthenticationService, private navigationService: NavigationService) { }

  canActivate(route: ActivatedRouteSnapshot) {
    let experimentId = route.params["expId"];

    if (experimentId == undefined) {
      this.router.navigate(['/error']);
      return observableOf(false);
    }

    return this.experimentsService.getExperiment(experimentId).pipe(
      map(experiment => {
        this.experimentsService.setLoadExperiment(experiment);
        this.navigationService.addExperimentName(experiment);

        return true;
      }), catchError(error => {
        if (error.status === 401 || error.status === 0) {
          if (this.authenticationService.isLogged()) {
            this.router.navigate(['/errorpage']);
          } else {
            this.router.navigate(['/login']);
          }
        } else {
          this.router.navigate(['/errorpage']);
        }

        return observableOf(false);
      })
    );
  }
}
