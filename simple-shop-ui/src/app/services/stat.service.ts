import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StandardResponse } from '../model/standard-response.model';
import { Stat } from '../model/stat.model';

@Injectable({
  providedIn: 'root'
})
export class StatService {
  private apiUrl = 'http://localhost:8081/api/stat';

  constructor(private http: HttpClient) { }

  getState(): Observable<StandardResponse<Stat>>{
    return this.http.get<StandardResponse<Stat>>(this.apiUrl);
  }
}
