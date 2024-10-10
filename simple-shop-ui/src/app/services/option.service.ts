import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StandardResponse } from '../model/standard-response.model';
import { CustomerOption, ItemOption } from '../model/option.model';

@Injectable({
  providedIn: 'root',
})
export class OptionService {
  private apiUrl = 'http://localhost:8081/api/options';

  constructor(private http: HttpClient) {}

  getCustomerOptions(): Observable<StandardResponse<CustomerOption[]>> {
    return this.http.get<StandardResponse<CustomerOption[]>>(
      `${this.apiUrl}/customers`
    );
  }

  getItemOptions(): Observable<StandardResponse<ItemOption[]>> {
    return this.http.get<StandardResponse<ItemOption[]>>(
      `${this.apiUrl}/items`
    );
  }
}
