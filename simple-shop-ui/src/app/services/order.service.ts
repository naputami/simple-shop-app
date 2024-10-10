import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginatedApiResponse } from '../model/paginated-response.model';
import { Order } from '../model/order.model';
import { StandardResponse } from '../model/standard-response.model';
import { OrderDetail } from '../model/order-detail.moodel';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8081/api/orders';

  constructor(private http: HttpClient) {}

  getOrders(
    page: number,
    size: number,
    code: string,
    sort: string
  ):Observable<PaginatedApiResponse<Order>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('code', code)
      .set('sort', sort);

    return this.http.get<PaginatedApiResponse<Order>>(this.apiUrl, {
      params,
    });
  }

  addOrder(formData: FormData): Observable<StandardResponse  <null>> {
    return this.http.post<StandardResponse<null>>(this.apiUrl, formData);
  }

  getDetailOrder(
    orderId: string | null
  ): Observable<StandardResponse<OrderDetail>> {
    return this.http.get<StandardResponse<OrderDetail>>(
      `${this.apiUrl}/${orderId}`
    );
  }

  deleteOrder(orderId: string | null): Observable<StandardResponse<null>> {
    return this.http.delete<StandardResponse<null>>(`${this.apiUrl}/${orderId}`);
  }

  updateOrder(
    orderId: string | null,
    formData: FormData
  ): Observable<StandardResponse<null>> {
    return this.http.put<StandardResponse<null>>(
      `${this.apiUrl}/${orderId}`,
      formData
    );
  }
}
