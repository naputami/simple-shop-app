import { Routes } from '@angular/router';
import { CustomerListComponent } from './feature/customer/customer-list/customer-list.component';
import { AddCustomerComponent } from './feature/customer/add-customer/add-customer.component';
import { CustomerDetailComponent } from './feature/customer/customer-detail/customer-detail.component';
import { UpdateCustomerComponent } from './feature/customer/update-customer/update-customer.component';
import { ItemListComponent } from './feature/item/item-list/item-list.component';
import { AddItemComponent } from './feature/item/add-item/add-item.component';
import { ItemDetailComponent } from './feature/item/item-detail/item-detail.component';
import { UpdateItemComponent } from './feature/item/update-item/update-item.component';

export const routes: Routes = [
  { path: 'customer', component: CustomerListComponent },
  { path: 'add-customer', component: AddCustomerComponent },
  { path: 'customer/:customerId', component: CustomerDetailComponent },
  { path: 'update-customer/:customerId', component: UpdateCustomerComponent },
  { path: 'item', component: ItemListComponent },
  { path: 'add-item', component: AddItemComponent },
  { path: 'item/:itemId', component: ItemDetailComponent },
  { path: 'update-item/:itemId', component: UpdateItemComponent },
];
