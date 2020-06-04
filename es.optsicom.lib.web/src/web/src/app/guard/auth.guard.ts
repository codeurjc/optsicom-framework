import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthenticationService } from '../services/authentication.service';
import { of as observableOf } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authenticationService: AuthenticationService) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let URL = state.url;

    return this.authenticationService.isConnected().pipe(
      map(() => {
        this.authenticationService.isLogin = true;
        
        return this.processLogin(true, URL);
      }), catchError(error => {
        return observableOf(this.processLogin(false, URL));
      })
    );
  }

  private processLogin(isLoginIn: boolean, actualURL: string) {
    if (isLoginIn) {
      if (actualURL == "/login") {
        this.router.navigate(['']);
      }
      return true;
    } else {
      if (actualURL == "/login") {
        return true;
      } else {
        this.router.navigate(['/login']);
        return false;
      }
    }
  }
}
