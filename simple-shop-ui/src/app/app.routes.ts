import { Routes } from '@angular/router';
import { CustomerListComponent } from './feature/customer/customer-list/customer-list.component';
import { AddCustomerComponent } from './feature/customer/add-customer/add-customer.component';

export const routes: Routes = [
  { path: 'customer', component: CustomerListComponent },
  { path: 'add-customer', component: AddCustomerComponent },
];
