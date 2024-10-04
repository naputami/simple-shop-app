import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  Validators,
  FormGroup,
  ReactiveFormsModule,
  FormControl,
} from '@angular/forms';
import { CustomerService } from '../../../services/customer.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-customer',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css',
})
export class AddCustomerComponent {
  customerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService
  ) {
    this.customerForm = this.fb.group({
      name: new FormControl('', Validators.required),
      address: new FormControl('', Validators.required),
      phoneNumber: new FormControl('', Validators.required),
      imgFile: new FormControl(null, Validators.required),
    });
  }

  onFileSelect(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.customerForm.patchValue({
        imgFile: file,
      });
    }
  }

  onSubmit(): void {
    if (this.customerForm.valid) {
      const formData = new FormData();
      formData.append('name', this.customerForm.get('name')!.value);
      formData.append('address', this.customerForm.get('address')!.value);
      formData.append('phoneNumber', this.customerForm.get('phoneNumber')!.value);
      formData.append('imgFile', this.customerForm.get('imgFile')!.value);

      this.customerService.addCustomer(formData).subscribe({
        next: (response) => console.log('Customer added successfully!', response),
        error: (error) => console.error('Error adding customer:', error)
      });
    } else {
      console.error('Form is not valid!');
    }
  }
}
