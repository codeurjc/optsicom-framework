
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class XMLRequestInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const modifiedReq = request.clone({
            headers: this.addExtraHeaders(request.headers)
        });

        return next.handle(modifiedReq);
    }

    private addExtraHeaders(headers: HttpHeaders): HttpHeaders {
        headers = headers.append('X-Requested-With', 'XMLHttpRequest');
        return headers;
    }
}