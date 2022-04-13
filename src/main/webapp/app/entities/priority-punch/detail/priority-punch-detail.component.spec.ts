import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PriorityPunchDetailComponent } from './priority-punch-detail.component';

describe('PriorityPunch Management Detail Component', () => {
  let comp: PriorityPunchDetailComponent;
  let fixture: ComponentFixture<PriorityPunchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PriorityPunchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ priorityPunch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PriorityPunchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PriorityPunchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load priorityPunch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.priorityPunch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
