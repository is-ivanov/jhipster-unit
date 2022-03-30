import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypePunchDetailComponent } from './type-punch-detail.component';

describe('TypePunch Management Detail Component', () => {
  let comp: TypePunchDetailComponent;
  let fixture: ComponentFixture<TypePunchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypePunchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ typePunch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypePunchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypePunchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typePunch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.typePunch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
