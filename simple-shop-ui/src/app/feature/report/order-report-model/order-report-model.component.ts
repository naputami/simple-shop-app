import { Component } from '@angular/core';
import { ReportService } from '../../../services/report.service';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';

@Component({
  selector: 'app-order-report-model',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './order-report-model.component.html',
  styleUrl: './order-report-model.component.css',
})
export class OrderReportModelComponent {
  selectAllDates: boolean = false;
  isSubmitting: boolean = false;
  orderReportForm: FormGroup;

  constructor(private reportService: ReportService, private fb: FormBuilder) {
    this.orderReportForm = this.fb.group({
      startDate: new FormControl(),
      endDate: new FormControl(),
    });
  }

  onSelectAllDatesChange() {
    if (this.selectAllDates) {
      this.selectAllDates = false;
      this.orderReportForm.get('startDate')?.enable();
      this.orderReportForm.get('endDate')?.enable();
    } else {
      this.selectAllDates = true;
      this.orderReportForm.get('startDate')?.disable();
      this.orderReportForm.get('endDate')?.disable();
    }
  }

  onSubmit() {
    this.isSubmitting = true;
    let startDate = this.orderReportForm.get('startDate')?.value;
    let endDate = this.orderReportForm.get('endDate')?.value;
    console.log(startDate);
    console.log(endDate);
    if (!this.selectAllDates) {
      if (!startDate || !endDate) {
        alert('Please select both start and end dates.');
        this.isSubmitting = false;
        return;
      }
    }

    if (this.selectAllDates && !startDate && !endDate) {
      startDate = '';
      endDate = '';
    }

    this.reportService.getReport(startDate, endDate).subscribe({
      next: (blob) => {
        const downloadURL = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = 'order-report.pdf';
        link.click();
        window.URL.revokeObjectURL(downloadURL);
        this.isSubmitting = false;
        this.selectAllDates = false;
        const modalCheckbox = document.getElementById(
          'order-report-modal'
        ) as HTMLInputElement;
        if (modalCheckbox) {
          modalCheckbox.checked = false;
        }
      },
      error: (error) => {
        console.error('Error downloading the PDF', error);
        alert('Failed to download the report.');
        this.isSubmitting = false;
      },
    });
  }
}
