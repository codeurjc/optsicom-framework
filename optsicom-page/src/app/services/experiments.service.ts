import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Experiment } from '../classes/experiment';
import { ExperimentMethodName } from '../classes/experimentmethodname';
import { CompleteExperimentInfo } from '../classes/completeexperimentinfo';

const baseAPI = environment.baseAPI;

@Injectable({
  providedIn: 'root'
})
export class ExperimentsService {

  constructor(private http: HttpClient) { }

  public getExperiments(): Observable<Experiment[]> {
    return this.http.get<Experiment[]>(baseAPI + "experiments", { withCredentials: true });
  }

  public getExperiment(experimentId: number): Observable<CompleteExperimentInfo> {
    return this.http.get<CompleteExperimentInfo>(baseAPI + "/experiment/" + experimentId, { withCredentials: true });
  }

  public deleteExperiment(experimentId: number) {
    return this.http.delete(baseAPI + "/experiment/" + experimentId, { withCredentials: true });
  }
}
