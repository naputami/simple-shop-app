import { Component, OnDestroy, OnInit } from '@angular/core';
import { CustomerService } from '../../../services/customer.service';
import { Customer } from '../../../model/customer.model';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { AddButtonComponent } from '../../../shared/components/add-button/add-button.component';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css'],
  imports: [RouterLink, NavbarComponent, AddButtonComponent],
  standalone: true,
})
export class CustomerListComponent implements OnInit, OnDestroy {
  customers: Customer[] = [];
  pageNo: number = 1;
  pageSize: number = 10;
  name: string = '';
  totalElements: number = 0;
  totalPages: number = 0;
  isLoading: boolean = false;
  errorMessage: string = '';
  isLoadingDelete: boolean = false;
  selectedCust: Customer | null = null;
  isDeleteSuccess: boolean = false;
  isShowModal: boolean = false;
  keywordInput = new Subject<string>();
  sort: string = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.fetchCustomers();
    this.keywordInput
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((kw: string) => {
        this.name = kw;
        this.pageNo = 1;
        this.fetchCustomers();
      });
  }

  ngOnDestroy(): void {
    this.keywordInput.complete();
  }

  fetchCustomers(): void {
    this.isLoading = true;
    this.customerService
      .getCustomers(this.pageNo - 1, this.pageSize, this.name, this.sort)
      .subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.customers = response.data;
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
        error: (err) => {
          this.errorMessage = 'Failed to load customers.';
          this.isLoading = false;
          this.hideErrorAfterDelay();
        },
      });
  }

  deleteCustomer(): void {
    if (this.selectedCust) {
      this.isLoadingDelete = true;
      this.customerService.deleteCustomer(this.selectedCust.id).subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.isLoadingDelete = false;
            this.isDeleteSuccess = true;
            this.customers = this.customers.filter(
              (customer) => customer.id !== this.selectedCust?.id
            );
            this.selectedCust = null;
            this.hideSuccessdeleteAfterDelay();
          } else {
            this.isLoadingDelete = false;
            this.isDeleteSuccess = false;
            this.selectedCust = null;
            this.errorMessage = 'Something wrong please try again';
            this.hideErrorAfterDelay();
          }
        },
        error: (error) => {
          this.isLoadingDelete = false;
          this.isDeleteSuccess = false;
          this.selectedCust = null;
          this.errorMessage = 'Something wrong please try again';
          this.hideErrorAfterDelay();
        },
      });
    }
  }

  openDeleteDialog(selectedCust: Customer): void {
    this.selectedCust = selectedCust;
  }

  closeDeleteDialog(): void {
    this.selectedCust = null;
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.pageNo = page;
    this.fetchCustomers();
  }

  nextPage(): void {
    if (this.pageNo < this.totalPages) {
      this.pageNo += 1;
      this.fetchCustomers();
    }
  }

  prevPage(): void {
    if (this.pageNo > 1) {
      this.pageNo -= 1;
      this.fetchCustomers();
    }
  }

  hideErrorAfterDelay() {
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }

  hideSuccessdeleteAfterDelay() {
    setTimeout(() => {
      this.isDeleteSuccess = false;
    }, 3000);
  }

  onSearchName(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.keywordInput.next(value);
  }

  onPageSizeChange(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.pageSize = Number(value);
    this.pageNo = 1; 
    this.fetchCustomers()
  }

  onSortByChange(e: Event) : void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.sort = value;
    this.pageNo = 1;
    this.fetchCustomers();
  }
}
