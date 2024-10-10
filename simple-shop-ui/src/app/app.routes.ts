import { Routes } from '@angular/router';
import { CustomerListComponent } from './feature/customer/customer-list/customer-list.component';
import { AddCustomerComponent } from './feature/customer/add-customer/add-customer.component';
import { CustomerDetailComponent } from './feature/customer/customer-detail/customer-detail.component';
import { UpdateCustomerComponent } from './feature/customer/update-customer/update-customer.component';
import { ItemListComponent } from './feature/item/item-list/item-list.component';
import { AddItemComponent } from './feature/item/add-item/add-item.component';
import { ItemDetailComponent } from './feature/item/item-detail/item-detail.component';
import { UpdateItemComponent } from './feature/item/update-item/update-item.component';
import { OrderListComponent } from './feature/order/order-list/order-list.component';
import { AddOrderComponent } from './feature/order/add-order/add-order.component';
import { OrderDetailComponent } from './feature/order/order-detail/order-detail.component';
import { UpdateOrderComponent } from './feature/order/update-order/update-order.component';

export const routes: Routes = [
  { path: 'customer', component: CustomerListComponent },
  { path: 'add-customer', component: AddCustomerComponent },
  { path: 'customer/:customerId', component: CustomerDetailComponent },
  { path: 'update-customer/:customerId', component: UpdateCustomerComponent },
  { path: 'item', component: ItemListComponent },
  { path: 'add-item', component: AddItemComponent },
  { path: 'item/:itemId', component: ItemDetailComponent },
  { path: 'update-item/:itemId', component: UpdateItemComponent },
  { path: 'order', component: OrderListComponent },
  { path: 'add-order', component: AddOrderComponent },
  { path: 'order/:orderId', component: OrderDetailComponent },
  { path: 'update-order/:orderId', component: UpdateOrderComponent },
];
