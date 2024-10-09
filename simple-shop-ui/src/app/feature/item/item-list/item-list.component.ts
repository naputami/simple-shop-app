import { Component, OnDestroy, OnInit } from '@angular/core';
import { Item } from '../../../model/item.model';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { ItemService } from '../../../services/item.service';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-item-list',
  standalone: true,
  imports: [NavbarComponent, RouterLink, CurrencyPipe],
  templateUrl: './item-list.component.html',
  styleUrl: './item-list.component.css',
})
export class ItemListComponent implements OnInit, OnDestroy {
  items: Item[] = [];
  pageNo: number = 1;
  pageSize: number = 10;
  name: string = '';
  totalElements: number = 0;
  totalPages: number = 0;
  isLoading: boolean = false;
  errorMessage: string = '';
  isLoadingDelete: boolean = false;
  selectedItem: Item | null = null;
  isDeleteSuccess: boolean = false;
  isShowModal: boolean = false;
  keywordInput = new Subject<string>();
  sort: string = '';

  constructor(private itemService: ItemService) {}

  ngOnInit(): void {
    this.fetchItems();
    this.keywordInput
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((kw: string) => {
        this.name = kw;
        this.pageNo = 1;
        this.fetchItems();
      });
  }

  ngOnDestroy(): void {
    this.keywordInput.complete();
  }

  fetchItems(): void {
    this.isLoading = true;
    this.itemService
      .getItems(this.pageNo - 1, this.pageSize, this.name, this.sort)
      .subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.items = response.data;
            this.pageNo = response.pageNo;
            this.pageSize = response.pageSize;
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
          } else {
            this.errorMessage = response.message;
            this.hideErrorAfterDelay();
          }
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Failed to load customers.';
          this.isLoading = false;
          this.hideErrorAfterDelay();
        },
      });
  }

  deleteItem(): void {
    if (this.selectedItem) {
      this.isLoadingDelete = true;
      this.itemService.deleteItem(this.selectedItem.id).subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.isLoadingDelete = false;
            this.isDeleteSuccess = true;
            this.items = this.items.filter(
              (item) => item.id !== this.selectedItem?.id
            );
            this.selectedItem = null;
            this.hideSuccessdeleteAfterDelay();
          } else {
            this.isLoadingDelete = false;
            this.isDeleteSuccess = false;
            this.selectedItem = null;
            this.errorMessage = 'Something wrong please try again';
            this.hideErrorAfterDelay();
          }
        },
        error: (error) => {
          this.isLoadingDelete = false;
          this.isDeleteSuccess = false;
          this.selectedItem = null;
          this.errorMessage = 'Something wrong please try again';
          this.hideErrorAfterDelay();
        },
      });
    }
  }

  openDeleteDialog(selectedItem: Item): void {
    this.selectedItem = selectedItem;
  }

  closeDeleteDialog(): void {
    this.selectedItem = null;
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.pageNo = page;
    this.fetchItems();
  }

  nextPage(): void {
    if (this.pageNo < this.totalPages) {
      this.pageNo += 1;
      this.fetchItems();
    }
  }

  prevPage(): void {
    if (this.pageNo > 1) {
      this.pageNo -= 1;
      this.fetchItems();
    }
  }

  hideErrorAfterDelay() {
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }

  hideSuccessdeleteAfterDelay() {
    setTimeout(() => {
      this.isDeleteSuccess = false;
    }, 3000);
  }

  onSearchName(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.keywordInput.next(value);
  }

  onPageSizeChange(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.pageSize = Number(value);
    this.pageNo = 1;
    this.fetchItems();
  }

  onSortByChange(e: Event): void {
    const target = e.target as HTMLInputElement;
    const value = target.value;
    this.sort = value;
    this.pageNo = 1;
    this.fetchItems();
  }
}
