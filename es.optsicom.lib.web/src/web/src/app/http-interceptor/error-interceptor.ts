
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { LastPageVisitedService } from '../services/last-page-visited.service';

@Injectable({
    providedIn: 'root'
})
export class ErrorInterceptor implements HttpInterceptor {

    constructor(private lastPageVisitedService: LastPageVisitedService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request)
            // add error handling
            .pipe(
                catchError(
                    (error: any, caught: Observable<HttpEvent<any>>) => {
                        if (error.status === 401 || error.status === 0) {
                            if (window.location.pathname !== "/"
                                && window.location.pathname !== "/login"
                                && window.location.pathname !== "/errorpage"
                                && window.location.pathname !== "/experiments") {
                                    this.lastPageVisitedService.setLastPage(window.location.pathname);
                            }
                        }
                        throw error;
                    }
                ),
            );
    }
}
