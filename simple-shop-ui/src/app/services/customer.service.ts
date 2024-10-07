import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedApiResponse } from '../model/paginated-response.model';
import { StandardResponse } from '../model/standard-response.model';
import { Customer } from '../model/customer.model';
import { CustomerDetail } from '../model/customer-detail.model';
import { Form } from '@angular/forms';

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

  addCustomer(formData : FormData): Observable<StandardResponse<null>> {
    return this.http.post<StandardResponse<null>>(this.apiUrl, formData);
  }

  getDetailCustomer(custId: string | null): Observable<StandardResponse<CustomerDetail>> {
    return this.http.get<StandardResponse<CustomerDetail>>(`${this.apiUrl}/${custId}`);
  }

  deleteCustomer(custId: string | null): Observable<StandardResponse<null>>{
    return this.http.delete<StandardResponse<null>>(`${this.apiUrl}/${custId}`);
  }

  updateCustomer(custId: string | null, formData: FormData): Observable<StandardResponse<null>>{
    formData.forEach((value,key) => {
      console.log(key+" "+value)
    });

    return this.http.put<StandardResponse<null>>(`${this.apiUrl}/${custId}`, formData);
  }
}
