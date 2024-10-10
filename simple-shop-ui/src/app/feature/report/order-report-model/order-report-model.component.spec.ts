import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderReportModelComponent } from './order-report-model.component';

describe('OrderReportModelComponent', () => {
  let component: OrderReportModelComponent;
  let fixture: ComponentFixture<OrderReportModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderReportModelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderReportModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
