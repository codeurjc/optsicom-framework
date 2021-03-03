import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ReportResponseDTO } from '../classes/report';

const reportsAPI = environment.baseAPI + "reports";
@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) { }

  public getReport(expIds: Array<number>, 
    methodIds: Array<number> = undefined,
    instanceIds: Array<number> = undefined): Observable<ReportResponseDTO> {
    let params = new HttpParams()
      .set('expIds', expIds.toString())

    if (methodIds !== undefined) {
      params = params.set('methodIds', methodIds.toString());
    }

    if(instanceIds !== undefined) {
      params = params.set('instanceIds', instanceIds.toString());
    }

    return this.http.get<ReportResponseDTO>(reportsAPI, { params: params, withCredentials: true });
  }
}
