import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
  ReactiveFormsModule,
} from '@angular/forms';
import { CustomerOption, ItemOption } from '../../../model/option.model';
import { OrderService } from '../../../services/order.service';
import { OptionService } from '../../../services/option.service';
import { OrderDetail } from '../../../model/order-detail.moodel';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { forkJoin, take } from 'rxjs';

@Component({
  selector: 'app-update-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './update-order.component.html',
  styleUrl: './update-order.component.css',
})
export class UpdateOrderComponent {
  orderForm: FormGroup;
  isSuccess: boolean = false;
  isError: boolean = false;
  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isShowingNotif: boolean = false;
  customerOptions: CustomerOption[] | null = [];
  itemOptions: ItemOption[] | null = [];
  data: OrderDetail | null = null;
  orderId: string | null = '';

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private optionService: OptionService,
    private route: ActivatedRoute
  ) {
    this.route.paramMap.subscribe((params) => {
      const orderId = params.get('orderId');
      this.orderId = orderId;
      this.fetchOrderDetail(orderId);
    });
    this.orderForm = this.fb.group({
      customerId: new FormControl('', Validators.required),
      itemId: new FormControl('', Validators.required),
      qty: new FormControl('', Validators.required),
      orderDate: new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
    forkJoin({
      customers: this.optionService.getCustomerOptions(),
      items: this.optionService.getItemOptions(),
      routeParams: this.route.paramMap.pipe(take(1)),
    }).subscribe({
      next: (results) => {
        if (results.customers.status === 'OK') {
          this.customerOptions = results.customers.data;
        } else {
          console.error(results.customers.message);
        }

        if (results.items.status === 'OK') {
          this.itemOptions = results.items.data;
        } else {
          console.error(results.items.message);
        }

        const orderId = results.routeParams.get('orderId');
        this.orderId = orderId;
        this.fetchOrderDetail(orderId);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  fetchCustomerOptions(): void {
    this.optionService.getCustomerOptions().subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.customerOptions = response.data;
        } else {
          console.error(response.message);
        }
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  fetchItemOptions(): void {
    this.optionService.getItemOptions().subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.itemOptions = response.data;
        } else {
          console.error(response.message);
        }
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  fetchOrderDetail(orderId: string | null) {
    this.orderService.getDetailOrder(orderId).subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.isLoading = false;
          this.isSuccess = true;
          this.data = response.data;
          if (this.data) {
            const selectedCustomer = this.customerOptions?.find(
              (customer) => customer.name === this.data?.customerName
            );
            const selectedItem = this.itemOptions?.find(
              (item) => item.name === this.data?.itemName
            );

            if (selectedCustomer && selectedItem) {
              this.orderForm.patchValue({
                customerId: selectedCustomer.id,
                itemId: selectedItem.id,
                qty: this.data.qty,
                orderDate: this.data.orderDate,
              });
            } else {
              this.orderForm.patchValue(this.data);
            }
          }
        } else {
          this.isLoading = false;
          this.isError = true;
          this.errorMessage = 'Something wrong, please try again';
          this.hideErrorAfterDelay();
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Something wrong, please try again';
        this.hideErrorAfterDelay();
      },
    });
  }

  hideMessageAfterDelay() {
    setTimeout(() => {
      this.isShowingNotif = false;
    }, 3000);
  }

  hideErrorAfterDelay() {
    setTimeout(() => {
      this.isError = false;
    }, 3000);
  }

  OnSubmit(): void {
    if (this.orderForm.valid) {
      const formData = new FormData();
      formData.append('customerId', this.orderForm.get('customerId')!.value);
      formData.append('itemId', this.orderForm.get('itemId')!.value);
      formData.append('qty', this.orderForm.get('qty')!.value);
      formData.append('orderDate', this.orderForm.get('orderDate')!.value);
      this.orderService.updateOrder(this.orderId, formData).subscribe({
        next: (response) => {
          this.isShowingNotif = true;
          this.successMessage = 'Order is successfully updated';
          this.hideMessageAfterDelay();
        },
        error: (error) => {
          this.isShowingNotif = true;
          this.errorMessage = 'Something wrong! please try again later.';
          this.hideMessageAfterDelay();
        },
      });
    } else {
      this.orderForm.markAllAsTouched();
    }
  }
}
