import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../../services/customer.service';
import { Customer } from '../../../model/customer.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css'],
  imports: [RouterLink],
  standalone: true
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  pageNo: number = 0;
  pageSize: number = 10;
  name: string = '';
  totalElements: number = 0;
  totalPages: number = 0;
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.fetchCustomers();
  }

  fetchCustomers(): void {
    this.isLoading = true;
    this.customerService
      .getCustomers(this.pageNo, this.pageSize, this.name)
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
            this.hideErrorAfterDelay()
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
    }, 3000); // Clears the error message after 3000 ms (3 seconds)
  }

  search(){
    
  }

}
