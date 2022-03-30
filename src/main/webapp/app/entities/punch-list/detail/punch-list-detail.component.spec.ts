import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PunchListDetailComponent } from './punch-list-detail.component';

describe('PunchList Management Detail Component', () => {
  let comp: PunchListDetailComponent;
  let fixture: ComponentFixture<PunchListDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PunchListDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ punchList: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PunchListDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PunchListDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load punchList on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.punchList).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
