import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ReportRest } from '../classes/report-classes';
import { Observable } from 'rxjs';

const baseAPI = environment.baseAPI;

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) { }

  public getReport(expIds: Array<number>, methodIds: Array<number> = undefined): Observable<ReportRest> {
    let params = new HttpParams()
      .set('expIds', expIds.toString())

    if (methodIds !== undefined) {
      params = params.set('methodIds', methodIds.toString());
    }

    return this.http.get<ReportRest>(baseAPI + "reports", { params: params, withCredentials: true });
  }
}
