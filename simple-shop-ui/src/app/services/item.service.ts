import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginatedApiResponse } from '../model/paginated-response.model';
import { Item } from '../model/item.model';
import { StandardResponse } from '../model/standard-response.model';
import { ItemDetail } from '../model/item-detail.model';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private apiUrl = 'http://localhost:8081/api/items';
  constructor(private http: HttpClient) {}

  getItems(
    page: number,
    size: number,
    name: string,
    sort: string
  ): Observable<PaginatedApiResponse<Item>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('name', name)
      .set('sort', sort);

    return this.http.get<PaginatedApiResponse<Item>>(this.apiUrl, {
      params,
    });
  }

  addItem(formData: FormData): Observable<StandardResponse<null>> {
    return this.http.post<StandardResponse<null>>(this.apiUrl, formData);
  }

  getDetailItem(
    itemId: string | null
  ): Observable<StandardResponse<ItemDetail>> {
    return this.http.get<StandardResponse<ItemDetail>>(
      `${this.apiUrl}/${itemId}`
    );
  }

  deleteItem(itemId: string | null): Observable<StandardResponse<null>> {
    return this.http.delete<StandardResponse<null>>(`${this.apiUrl}/${itemId}`);
  }

  updateItem(
    itemId: string | null,
    formData: FormData
  ): Observable<StandardResponse<null>> {
    return this.http.put<StandardResponse<null>>(
      `${this.apiUrl}/${itemId}`,
      formData
    );
  }
}
