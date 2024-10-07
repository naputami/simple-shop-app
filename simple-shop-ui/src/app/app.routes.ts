import { Routes } from '@angular/router';
import { CustomerListComponent } from './feature/customer/customer-list/customer-list.component';
import { AddCustomerComponent } from './feature/customer/add-customer/add-customer.component';
import { CustomerDetailComponent } from './feature/customer/customer-detail/customer-detail.component';
import { UpdateCustomerComponent } from './feature/customer/update-customer/update-customer.component';

export const routes: Routes = [
  { path: 'customer', component: CustomerListComponent },
  { path: 'add-customer', component: AddCustomerComponent },
  { path: 'customer/:customerId', component: CustomerDetailComponent },
  { path: 'update-customer/:customerId', component: UpdateCustomerComponent },
];
