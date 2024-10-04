import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedApiResponse } from '../model/paginated-response.model';
import { StandardResponse } from '../model/standard-response.model';
import { Customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl = 'http://localhost:8081/api/customers';

  constructor(private http: HttpClient) {}

  getCustomers(
    page: number,
    size: number,
    name: string
  ): Observable<PaginatedApiResponse<Customer>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('name', name);

    return this.http.get<PaginatedApiResponse<Customer>>(this.apiUrl, {
      params,
    });
  }

  addCustomer(formData : FormData): Observable<StandardResponse> {
    return this.http.post<StandardResponse>(this.apiUrl, formData);
  }
}
