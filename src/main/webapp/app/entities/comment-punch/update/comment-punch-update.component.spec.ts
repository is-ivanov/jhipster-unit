import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommentPunchService } from '../service/comment-punch.service';
import { ICommentPunch, CommentPunch } from '../comment-punch.model';
import { IPunchItem } from 'app/entities/punch-item/punch-item.model';
import { PunchItemService } from 'app/entities/punch-item/service/punch-item.service';

import { CommentPunchUpdateComponent } from './comment-punch-update.component';

describe('CommentPunch Management Update Component', () => {
  let comp: CommentPunchUpdateComponent;
  let fixture: ComponentFixture<CommentPunchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commentPunchService: CommentPunchService;
  let punchItemService: PunchItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommentPunchUpdateComponent],
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
      .overrideTemplate(CommentPunchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommentPunchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commentPunchService = TestBed.inject(CommentPunchService);
    punchItemService = TestBed.inject(PunchItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PunchItem query and add missing value', () => {
      const commentPunch: ICommentPunch = { id: 456 };
      const punchItem: IPunchItem = { id: 21781 };
      commentPunch.punchItem = punchItem;

      const punchItemCollection: IPunchItem[] = [{ id: 29341 }];
      jest.spyOn(punchItemService, 'query').mockReturnValue(of(new HttpResponse({ body: punchItemCollection })));
      const additionalPunchItems = [punchItem];
      const expectedCollection: IPunchItem[] = [...additionalPunchItems, ...punchItemCollection];
      jest.spyOn(punchItemService, 'addPunchItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commentPunch });
      comp.ngOnInit();

      expect(punchItemService.query).toHaveBeenCalled();
      expect(punchItemService.addPunchItemToCollectionIfMissing).toHaveBeenCalledWith(punchItemCollection, ...additionalPunchItems);
      expect(comp.punchItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commentPunch: ICommentPunch = { id: 456 };
      const punchItem: IPunchItem = { id: 77780 };
      commentPunch.punchItem = punchItem;

      activatedRoute.data = of({ commentPunch });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commentPunch));
      expect(comp.punchItemsSharedCollection).toContain(punchItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommentPunch>>();
      const commentPunch = { id: 123 };
      jest.spyOn(commentPunchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentPunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentPunch }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commentPunchService.update).toHaveBeenCalledWith(commentPunch);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommentPunch>>();
      const commentPunch = new CommentPunch();
      jest.spyOn(commentPunchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentPunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentPunch }));
      saveSubject.complete();

      // THEN
      expect(commentPunchService.create).toHaveBeenCalledWith(commentPunch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommentPunch>>();
      const commentPunch = { id: 123 };
      jest.spyOn(commentPunchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentPunch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commentPunchService.update).toHaveBeenCalledWith(commentPunch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPunchItemById', () => {
      it('Should return tracked PunchItem primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPunchItemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
