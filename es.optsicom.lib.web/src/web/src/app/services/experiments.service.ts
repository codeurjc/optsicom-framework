import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ExperimentBasicResponseDTO, ExperimentExtendResponseDTO } from '../classes/experiment';

const experimentsAPI = environment.baseAPI + "experiments/";

@Injectable({
  providedIn: 'root'
})
export class ExperimentsService {

  private experiment: ExperimentExtendResponseDTO;

  constructor(private http: HttpClient) { }

  public getLoadExperiment(): ExperimentExtendResponseDTO {
    return this.experiment;
  }

  public setLoadExperiment(experiment: ExperimentExtendResponseDTO) {
    this.experiment = experiment;
  }

  public getExperiments(): Observable<Array<ExperimentBasicResponseDTO>> {
    return this.http.get<ExperimentBasicResponseDTO[]>(experimentsAPI, { withCredentials: true });
  }

  public getExperiment(experimentId: number): Observable<ExperimentExtendResponseDTO> {
    return this.http.get<ExperimentExtendResponseDTO>(experimentsAPI + experimentId, { withCredentials: true });
  }

  public deleteExperiment(experimentId: number) {
    return this.http.delete(experimentsAPI + experimentId, { withCredentials: true });
  }
}
