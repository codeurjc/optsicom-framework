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

  public getReport(expIds: Array<number>, methodIds: Array<number> = undefined, bestValues: boolean = false): Observable<ReportRest> {
    let params = new HttpParams()
      .set('expIds', expIds.toString())

    if (methodIds !== undefined) {
      params = params.set('methodIds', methodIds.toString());
    }

    if (bestValues) {
      params = params.set('bestValues', String(bestValues));
    }

    return this.http.get<ReportRest>(baseAPI + "report", { params: params, withCredentials: true });
  }
}
