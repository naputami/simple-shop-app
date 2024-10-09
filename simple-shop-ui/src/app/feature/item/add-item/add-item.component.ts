import { Component } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { ItemService } from '../../../services/item.service';
import { imgTypeValidator } from '../../../shared/validators/img-type.validator';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-add-item',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './add-item.component.html',
  styleUrl: './add-item.component.css',
})
export class AddItemComponent {
  itemForm: FormGroup;
  imagePreview: string | ArrayBuffer | null = null;
  isSuccess: boolean = false;
  isError: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isShowingNotif: boolean = false;

  constructor(private fb: FormBuilder, private itemService: ItemService) {
    this.itemForm = this.fb.group({
      name: new FormControl('', Validators.required),
      price: new FormControl('', Validators.required),
      stock: new FormControl('', Validators.required),
      desc: new FormControl('', Validators.required),
      lastRestockDate: new FormControl('', Validators.required),
      imgFile: new FormControl(null, [Validators.required, imgTypeValidator()]),
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
      formData.append('imgFile', this.itemForm.get('imgFile')!.value);

      this.itemService.addItem(formData).subscribe({
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
