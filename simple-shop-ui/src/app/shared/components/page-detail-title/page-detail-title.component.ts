import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-page-detail-title',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './page-detail-title.component.html',
  styleUrl: './page-detail-title.component.css'
})
export class PageDetailTitleComponent {
  @Input() title = '';
  @Input() link = '';
}
