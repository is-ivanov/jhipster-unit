import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PunchItemDetailComponent } from './punch-item-detail.component';

describe('PunchItem Management Detail Component', () => {
  let comp: PunchItemDetailComponent;
  let fixture: ComponentFixture<PunchItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PunchItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ punchItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PunchItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PunchItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load punchItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.punchItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
