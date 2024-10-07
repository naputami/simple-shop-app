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
import { imgTypeValidator } from '../../../shared/validators/img-type.validator';

@Component({
  selector: 'app-add-customer',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css',
})
export class AddCustomerComponent {
  customerForm: FormGroup;
  imagePreview: string | ArrayBuffer | null = null;
  isSuccess: boolean = false;
  isError: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isShowingNotif : boolean = false;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService
  ) {
    this.customerForm = this.fb.group({
      name: new FormControl('', Validators.required),
      address: new FormControl('', Validators.required),
      phoneNumber: new FormControl('', Validators.required),
      imgFile: new FormControl(null,[Validators.required, imgTypeValidator()]),
    });
  }

  hideMessageAfterDelay() {
    setTimeout(() => {
      this.isShowingNotif = false;
    }, 3000); 
  }

  onFileSelect(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.customerForm.patchValue({
        imgFile: file,
      });
      this.customerForm.get('imgFile')?.updateValueAndValidity();
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
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
        next: (response) => {
          this.isShowingNotif = true;
          this.successMessage= 'New Customer is successfully added';
          this.hideMessageAfterDelay();
        },
        error: (error) => {
          this.isShowingNotif = true;
          this.errorMessage= 'Something wrong! please try again later.'
          this.hideMessageAfterDelay();
        }
      });
    } else {
      this.customerForm.markAllAsTouched();
    }
  }
}
