import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PriorityPunchService } from '../service/priority-punch.service';
import { IPriorityPunch, PriorityPunch } from '../priority-punch.model';

import { PriorityPunchUpdateComponent } from './priority-punch-update.component';

describe('PriorityPunch Management Update Component', () => {
  let comp: PriorityPunchUpdateComponent;
  let fixture: ComponentFixture<PriorityPunchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let priorityPunchService: PriorityPunchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PriorityPunchUpdateComponent],
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
      .overrideTemplate(PriorityPunchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriorityPunchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    priorityPunchService = TestBed.inject(PriorityPunchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const priorityPunch: IPriorityPunch = { id: 456 };

      activatedRoute.data = of({ priorityPunch });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(priorityPunch));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PriorityPunch>>();
      const priorityPunch = { id: 123 };
      jest.spyOn(priorityPunchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priorityPunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priorityPunch }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(priorityPunchService.update).toHaveBeenCalledWith(priorityPunch);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PriorityPunch>>();
      const priorityPunch = new PriorityPunch();
      jest.spyOn(priorityPunchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priorityPunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priorityPunch }));
      saveSubject.complete();

      // THEN
      expect(priorityPunchService.create).toHaveBeenCalledWith(priorityPunch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PriorityPunch>>();
      const priorityPunch = { id: 123 };
      jest.spyOn(priorityPunchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priorityPunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(priorityPunchService.update).toHaveBeenCalledWith(priorityPunch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
