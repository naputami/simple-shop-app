import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { Order } from '../../../model/order.model';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { OrderService } from '../../../services/order.service';
import { OrderReportModelComponent } from '../../report/order-report-model/order-report-model.component';
import { AddButtonComponent } from '../../../shared/components/add-button/add-button.component';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    NavbarComponent,
    RouterLink,
    CurrencyPipe,
    OrderReportModelComponent,
    AddButtonComponent,
  ],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css',
})
export class OrderListComponent implements OnInit, OnDestroy {
  orders: Order[] = [];
  pageNo: number = 1;
  pageSize: number = 10;
  code: string = '';
  totalElements: number = 0;
  totalPages: number = 0;
  isLoading: boolean = false;
  errorMessage: string = '';
  isLoadingDelete: boolean = false;
  selectedOrder: Order | null = null;
  isDeleteSuccess: boolean = false;
  isShowModal: boolean = false;
  keywordInput = new Subject<string>();
  sort: string = '';

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.fetchOrders();
    this.keywordInput
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((kw: string) => {
        this.code = kw;
        this.pageNo = 1;
        this.fetchOrders();
      });
  }

  ngOnDestroy(): void {
    this.keywordInput.complete();
  }

  fetchOrders(): void {
    this.isLoading = true;
    this.orderService
      .getOrders(this.pageNo - 1, this.pageSize, this.code, this.sort)
      .subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.orders = response.data;
            this.pageNo = response.pageNo;
            this.pageSize = response.pageSize;
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
          } else {
            this.errorMessage = response.message;
            this.hideErrorAfterDelay();
          }
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoadingDelete = false;
          this.isDeleteSuccess = false;
          this.selectedOrder = null;
          this.errorMessage = 'Something wrong please try again';
          this.hideErrorAfterDelay();
        },
      });
  }

  deleteOrder(): void {
    if (this.selectedOrder) {
      this.isLoadingDelete = true;
      this.orderService.deleteOrder(this.selectedOrder.id).subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.isLoadingDelete = false;
            this.isDeleteSuccess = true;
            this.orders = this.orders.filter(
              (order) => order.id !== this.selectedOrder?.id
            );
            this.selectedOrder = null;
            this.hideSuccessdeleteAfterDelay();
          } else {
            this.isLoadingDelete = false;
            this.isDeleteSuccess = false;
            this.selectedOrder = null;
            this.errorMessage = 'Something wrong please try again';
            this.hideErrorAfterDelay();
          }
        },
        error: (error) => {
          this.isLoadingDelete = false;
          this.isDeleteSuccess = false;
          this.selectedOrder = null;
          this.errorMessage = 'Something wrong please try again';
          this.hideErrorAfterDelay();
        },
      });
    }
  }

  openDeleteDialog(selectedOrder: Order): void {
    this.selectedOrder = selectedOrder;
  }

  closeDeleteDialog(): void {
    this.selectedOrder = null;
  }
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.pageNo = page;
    this.fetchOrders();
  }

  nextPage(): void {
    if (this.pageNo < this.totalPages) {
      this.pageNo += 1;
      this.fetchOrders();
    }
  }

  prevPage(): void {
    if (this.pageNo > 1) {
      this.pageNo -= 1;
      this.fetchOrders();
    }
  }

  hideSuccessdeleteAfterDelay() {
    setTimeout(() => {
      this.isDeleteSuccess = false;
    }, 3000);
  }

  hideErrorAfterDelay() {
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }

  onSearchCode(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.keywordInput.next(value);
  }

  onPageSizeChange(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.pageSize = Number(value);
    this.pageNo = 1;
    this.fetchOrders();
  }

  onSortByChange(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.sort = value;
    this.pageNo = 1;
    this.fetchOrders();
  }
}
