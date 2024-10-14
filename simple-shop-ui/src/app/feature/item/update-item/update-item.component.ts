import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { imgTypeValidator } from '../../../shared/validators/img-type.validator';
import { ItemService } from '../../../services/item.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ItemDetail } from '../../../model/item-detail.model';
import { CommonModule } from '@angular/common';
import { PageDetailTitleComponent } from '../../../shared/components/page-detail-title/page-detail-title.component';

@Component({
  selector: 'app-update-item',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PageDetailTitleComponent,
  ],
  templateUrl: './update-item.component.html',
  styleUrl: './update-item.component.css',
})
export class UpdateItemComponent {
  itemForm: FormGroup;
  imagePreview: string | ArrayBuffer | null = null;
  isSuccess: boolean = false;
  isError: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isShowingNotif: boolean = false;
  itemId: string | null = '';
  data: ItemDetail | null = null;
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private itemService: ItemService,
    private route: ActivatedRoute
  ) {
    this.route.paramMap.subscribe((params) => {
      const itemId = params.get('itemId');
      this.itemId = itemId;
      this.fetchItemDetail(itemId);
    });
    this.itemForm = this.fb.group({
      name: new FormControl('', Validators.required),
      price: new FormControl('', Validators.required),
      stock: new FormControl('', Validators.required),
      desc: new FormControl('', Validators.required),
      lastRestockDate: new FormControl('', Validators.required),
      imgFile: new FormControl(null, [imgTypeValidator()]),
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

  onFileSelect(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.itemForm.patchValue({
        imgFile: file,
      });
      this.itemForm.get('imgFile')?.updateValueAndValidity();
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  fetchItemDetail(itemId: string | null) {
    this.itemService.getDetailItem(itemId).subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.isLoading = false;
          this.isSuccess = true;
          this.data = response.data;
          if (this.data) {
            this.itemForm.patchValue(this.data);
            this.imagePreview = this.data?.imgUrl;
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

  onSubmit(): void {
    if (this.itemForm.valid) {
      const formData = new FormData();
      formData.append('name', this.itemForm.get('name')!.value);
      formData.append('price', this.itemForm.get('price')!.value);
      formData.append('stock', this.itemForm.get('stock')!.value);
      formData.append('desc', this.itemForm.get('desc')!.value);
      formData.append(
        'lastRestockDate',
        this.itemForm.get('lastRestockDate')!.value
      );
      if (this.itemForm.get('imgFile')?.value !== null) {
        formData.append('imgFile', this.itemForm.get('imgFile')?.value);
      }

      this.itemService.updateItem(this.itemId, formData).subscribe({
        next: (response) => {
          this.isShowingNotif = true;
          this.successMessage = 'New Item is successfully added';
          this.hideMessageAfterDelay();
        },
        error: (error) => {
          this.isShowingNotif = true;
          this.errorMessage = 'Something wrong! please try again later.';
          this.hideMessageAfterDelay();
        },
      });
    } else {
      this.itemForm.markAllAsTouched();
    }
  }
}
