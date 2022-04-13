import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypePunchService } from '../service/type-punch.service';
import { ITypePunch, TypePunch } from '../type-punch.model';

import { TypePunchUpdateComponent } from './type-punch-update.component';

describe('TypePunch Management Update Component', () => {
  let comp: TypePunchUpdateComponent;
  let fixture: ComponentFixture<TypePunchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typePunchService: TypePunchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypePunchUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TypePunchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypePunchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typePunchService = TestBed.inject(TypePunchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typePunch: ITypePunch = { id: 456 };

      activatedRoute.data = of({ typePunch });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(typePunch));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePunch>>();
      const typePunch = { id: 123 };
      jest.spyOn(typePunchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePunch }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(typePunchService.update).toHaveBeenCalledWith(typePunch);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePunch>>();
      const typePunch = new TypePunch();
      jest.spyOn(typePunchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typePunch }));
      saveSubject.complete();

      // THEN
      expect(typePunchService.create).toHaveBeenCalledWith(typePunch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TypePunch>>();
      const typePunch = { id: 123 };
      jest.spyOn(typePunchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typePunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typePunchService.update).toHaveBeenCalledWith(typePunch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
