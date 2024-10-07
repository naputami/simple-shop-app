import { Component, OnInit } from '@angular/core';
import { PageDetailTitleComponent } from '../../../shared/components/page-detail-title/page-detail-title.component';
import { CustomerService } from '../../../services/customer.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CustomerDetail } from '../../../model/customer-detail.model';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [PageDetailTitleComponent, RouterLink],
  templateUrl: './customer-detail.component.html',
  styleUrl: './customer-detail.component.css'
})
export class CustomerDetailComponent implements OnInit {
  isLoading: boolean = true;
  isSuccess: boolean = false;
  errorMessage: string = '';
  isError: boolean = false;
  data: CustomerDetail | null = null;
  customerId : string | null = '';

  constructor(private customerService: CustomerService, private route: ActivatedRoute) {}

  ngOnInit(): void {
      this.route.paramMap.subscribe(params => {
        const custId = params.get("customerId");
        this.customerId = custId;
        this.fetchDetailCustomer(custId);
      })
  }

  fetchDetailCustomer(custId: string | null){
    this.customerService.getDetailCustomer(custId).subscribe({
      next: (response) => {
        if(response.status === 'OK'){
          this.isLoading = false;
          this.isSuccess = true;
          this.data = response.data;
        } else {
          this.isLoading = false;
          this.isError = true;
          this.errorMessage = "Something wrong, please try again"
          this.hideErrorAfterDelay()
        }

      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = "Something wrong, please try again"
        this.hideErrorAfterDelay()
      }
    })
  }

  hideErrorAfterDelay() {
    setTimeout(() => {
      this.isError = false;
    }, 3000); 
  }


}
