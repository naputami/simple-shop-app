import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private apiUrl = 'http://localhost:8081/api/order-report';

  constructor(private http: HttpClient) {}

  getReport(startDate: string, endDate: string) {
    let params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    return this.http.get(this.apiUrl, { params: params, responseType: 'blob' });
  }
}
