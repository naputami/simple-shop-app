import { Component, OnInit } from '@angular/core';
import { PageDetailTitleComponent } from '../../../shared/components/page-detail-title/page-detail-title.component';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { OrderDetail } from '../../../model/order-detail.moodel';
import { OrderService } from '../../../services/order.service';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [PageDetailTitleComponent, RouterLink, CurrencyPipe],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.css'
})
export class OrderDetailComponent implements OnInit {
  isLoading: boolean = true;
  isSuccess: boolean = false;
  errorMessage: string = '';
  isError: boolean = false;
  data: OrderDetail | null = null;
  orderId : string | null = '';

  constructor(private orderService: OrderService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const orderId = params.get("orderId");
      this.orderId = orderId;
      this.fetchOrderDetail(orderId);
    })
  }

  fetchOrderDetail(orderId: string | null){
    this.orderService.getDetailOrder(orderId).subscribe({
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
      },
    })
  }

  hideErrorAfterDelay() {
    setTimeout(() => {
      this.isError = false;
    }, 3000); 
  }

}
