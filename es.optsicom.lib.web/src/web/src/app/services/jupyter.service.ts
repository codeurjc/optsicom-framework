import { JupyterDTO } from './../classes/jupyter';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const jupyterAPI = environment.baseAPI + "jupyter/"

@Injectable({
  providedIn: 'root'
})
export class JupyterService {
  constructor(private http: HttpClient) { }
  
  public getJupyterUrl(): Observable<JupyterDTO>{
    return this.http.get<JupyterDTO>(jupyterAPI + "info/", { withCredentials: true });
  }
}
