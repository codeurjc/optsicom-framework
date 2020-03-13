import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LastPageVisitedService {

  private lastPage: string = undefined;

  constructor() { }

  setLastPage(page: string) {
    this.lastPage = page;
  }

  existLastPage() {
    if (this.lastPage != undefined) {
      return true;
    }
    return false;
  }

  getLastPage() {
    return this.lastPage;
  }
}
