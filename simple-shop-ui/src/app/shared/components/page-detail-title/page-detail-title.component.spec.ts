import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageDetailTitleComponent } from './page-detail-title.component';

describe('PageDetailTitleComponent', () => {
  let component: PageDetailTitleComponent;
  let fixture: ComponentFixture<PageDetailTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageDetailTitleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageDetailTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
