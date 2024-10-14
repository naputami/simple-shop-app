import { Component, OnInit } from '@angular/core';
import { PageDetailTitleComponent } from '../../../shared/components/page-detail-title/page-detail-title.component';
import { RouterLink } from '@angular/router';
import { ItemDetail } from '../../../model/item-detail.model';
import { ItemService } from '../../../services/item.service';
import { ActivatedRoute } from '@angular/router';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-item-detail',
  standalone: true,
  imports: [PageDetailTitleComponent, RouterLink, CurrencyPipe],
  templateUrl: './item-detail.component.html',
  styleUrl: './item-detail.component.css'
})
export class ItemDetailComponent implements OnInit {
  isLoading: boolean = true;
  isSuccess: boolean = false;
  errorMessage: string = '';
  isError: boolean = false;
  data: ItemDetail | null = null;
  itemId : string | null = '';

  constructor(private itemService: ItemService, private route: ActivatedRoute) {}

  ngOnInit(): void {
      this.route.paramMap.subscribe(params => {
        const itemId = params.get("itemId");
        this.itemId = itemId;
        this.fetchItemDetail(itemId);
      })
  }

  fetchItemDetail(itemId: string | null){
    this.itemService.getDetailItem(itemId).subscribe({
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
        this.isError = true;
        this.isLoading = false;
        this.errorMessage = error.message;
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
