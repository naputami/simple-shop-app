import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CustomerService } from '../../../services/customer.service';
import { imgTypeValidator } from '../../../shared/validators/img-type.validator';
import { CustomerDetail } from '../../../model/customer-detail.model';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';


@Component({
  selector: 'app-update-customer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './update-customer.component.html',
  styleUrl: './update-customer.component.css'
})
export class UpdateCustomerComponent {
  customerForm: FormGroup;
  imagePreview: string | ArrayBuffer | null | undefined= null;
  isSuccess: boolean = false;
  isError: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isShowingNotif : boolean = false;
  isLoading: boolean = false;
  data: CustomerDetail | null = null;
  customerId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private route: ActivatedRoute
  ) {
    this.route.paramMap.subscribe(params => {
      const custId = params.get("customerId");
      this.customerId = custId;
      this.fetchDetailCustomer(custId);
    })
    this.customerForm = this.fb.group({
      name: new FormControl(this.data?.name, Validators.required),
      address: new FormControl(this.data?.address, Validators.required),
      phoneNumber: new FormControl(this.data?.phoneNumber, Validators.required),
      imgFile: new FormControl(null,[imgTypeValidator()]),
      isActive: new FormControl(true, Validators.required)
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
      if(this.customerForm.get('imgFile')?.value !== null){
        formData.append('imgFile', this.customerForm.get('imgFile')?.value);
      }
      formData.append('isActive', this.customerForm.value.isActive? "1" : "0");

      this.customerService.updateCustomer(this.customerId, formData).subscribe({
        next: (response) => {
          this.isShowingNotif = true;
          this.successMessage= 'Customer data is successfully updated';
          this.hideMessageAfterDelay();
        },
        error: (error) => {
          this.isShowingNotif = true;
          this.errorMessage= 'Something wrong! please try again later.'
          this.hideMessageAfterDelay();
          console.log(error);
        }
      });
    } else {
      this.customerForm.markAllAsTouched();
      alert("Tidak valid")
    }
  }

  fetchDetailCustomer(custId: string | null){
    this.customerService.getDetailCustomer(custId).subscribe({
      next: (response) => {
        if(response.status === 'OK'){
          this.isLoading = false;
          this.isSuccess = true;
          this.data = response.data;
          if(this.data){
            this.customerForm.patchValue(this.data);
            this.imagePreview = this.data?.imgUrl;
          } else {
            console.log("data cant be null")
          }
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
