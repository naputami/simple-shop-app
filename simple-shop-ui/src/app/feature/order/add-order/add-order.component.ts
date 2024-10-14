import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { OptionService } from '../../../services/option.service';
import { OrderService } from '../../../services/order.service';
import { CustomerOption, ItemOption } from '../../../model/option.model';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PageDetailTitleComponent } from '../../../shared/components/page-detail-title/page-detail-title.component';

@Component({
  selector: 'app-add-order',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    PageDetailTitleComponent,
  ],
  templateUrl: './add-order.component.html',
  styleUrl: './add-order.component.css',
})
export class AddOrderComponent implements OnInit {
  orderForm: FormGroup;
  isSuccess: boolean = false;
  isError: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isShowingNotif: boolean = false;
  customerOptions: CustomerOption[] | null = [];
  itemOptions: ItemOption[] | null = [];
  totalPrice: number = 0;
  qty: number = 0; 
  selectedItem: ItemOption | null = null;

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private optionService: OptionService
  ) {
    this.orderForm = this.fb.group({
      customerId: new FormControl('', Validators.required),
      itemId: new FormControl('', Validators.required),
      qty: new FormControl('', Validators.required),
      orderDate: new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
    this.fetchCustomerOptions();
    this.fetchItemOptions();
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

  hideMessageAfterDelay() {
    setTimeout(() => {
      this.isShowingNotif = false;
    }, 3000);
  }

  OnSubmit(): void {
    if (this.orderForm.valid) {
      const formData = new FormData();
      formData.append('customerId', this.orderForm.get('customerId')!.value);
      formData.append('itemId', this.orderForm.get('itemId')!.value);
      formData.append('qty', this.orderForm.get('qty')!.value);
      formData.append('orderDate', this.orderForm.get('orderDate')!.value);
      this.orderService.addOrder(formData).subscribe({
        next: (response) => {
          this.isShowingNotif = true;
          this.successMessage = 'New order is successfully added';
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

  onQtyChange(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.qty = Number(value);
    if(this.selectedItem){
      this.totalPrice = this.qty * this.selectedItem?.price;
    }
  }

  onItemChange(e: Event): void{
    const target = e.target as HTMLInputElement;
    const value = target.value;
    const selectedItemOption = this.itemOptions?.filter(item => item.id === value)[0];
    if(selectedItemOption){
      this.selectedItem = selectedItemOption;
      this.totalPrice = this.qty * selectedItemOption?.price
    }
  }
}
