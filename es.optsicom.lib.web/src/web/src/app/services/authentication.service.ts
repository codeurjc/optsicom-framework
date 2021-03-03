import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const loginAPI = environment.baseAPI + "logIn";
const logoutAPI = environment.baseAPI + "logOut";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private logged: boolean = false;

  constructor(private http: HttpClient) { }

  private getHeadersLogin(user: string, pass: string) {
    const userPass = user + ':' + pass;

    const headers = new HttpHeaders({
      'Authorization': 'Basic ' + utf8_to_b64(userPass)
    });

    return { withCredentials: true, headers };
  }

  public isLogged(): boolean {
    return this.logged;
  }

  public setLogged(logged: boolean) {
    this.logged = logged;
  }

  public login(name: string, pass: string) {
    return this.http.get(loginAPI, this.getHeadersLogin(name, pass));
  }

  public logout() {
    return this.http.get(logoutAPI, { withCredentials: true });
  }

  public isConnected() {
    return this.http.get(loginAPI, { withCredentials: true });
  }
}

function utf8_to_b64(str) {
  return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function (match, p1) {
    return String.fromCharCode(<any>'0x' + p1);
  }));
}