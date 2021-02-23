import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Experiment, ExperimentRest } from '../classes/experiment-clasess';

const baseAPI = environment.baseAPI;

@Injectable({
  providedIn: 'root'
})
export class ExperimentsService {

  private experimentRest: ExperimentRest;

  constructor(private http: HttpClient) { }

  public getLoadExperimentRest(): ExperimentRest {
    return this.experimentRest;
  }

  public setLoadExperimentRest(experimentRest: ExperimentRest) {
    this.experimentRest = experimentRest;
  }

  public getExperiments(): Observable<Array<Experiment>> {
    return this.http.get<Experiment[]>(baseAPI + "experiments", { withCredentials: true });
  }

  public getExperiment(experimentId: number): Observable<ExperimentRest> {
    return this.http.get<ExperimentRest>(baseAPI + "experiments/" + experimentId, { withCredentials: true });
  }

  public deleteExperiment(experimentId: number) {
    return this.http.delete(baseAPI + "experiments/" + experimentId, { withCredentials: true });
  }
}
