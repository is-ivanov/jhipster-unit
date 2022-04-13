import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LineService } from '../service/line.service';
import { ILine, Line } from '../line.model';
import { IBlock } from 'app/entities/block/block.model';
import { BlockService } from 'app/entities/block/service/block.service';

import { LineUpdateComponent } from './line-update.component';

describe('Line Management Update Component', () => {
  let comp: LineUpdateComponent;
  let fixture: ComponentFixture<LineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lineService: LineService;
  let blockService: BlockService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LineUpdateComponent],
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
      .overrideTemplate(LineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lineService = TestBed.inject(LineService);
    blockService = TestBed.inject(BlockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Block query and add missing value', () => {
      const line: ILine = { id: 456 };
      const block: IBlock = { id: 14327 };
      line.block = block;

      const blockCollection: IBlock[] = [{ id: 36946 }];
      jest.spyOn(blockService, 'query').mockReturnValue(of(new HttpResponse({ body: blockCollection })));
      const additionalBlocks = [block];
      const expectedCollection: IBlock[] = [...additionalBlocks, ...blockCollection];
      jest.spyOn(blockService, 'addBlockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ line });
      comp.ngOnInit();

      expect(blockService.query).toHaveBeenCalled();
      expect(blockService.addBlockToCollectionIfMissing).toHaveBeenCalledWith(blockCollection, ...additionalBlocks);
      expect(comp.blocksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const line: ILine = { id: 456 };
      const block: IBlock = { id: 30130 };
      line.block = block;

      activatedRoute.data = of({ line });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(line));
      expect(comp.blocksSharedCollection).toContain(block);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Line>>();
      const line = { id: 123 };
      jest.spyOn(lineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ line });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: line }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lineService.update).toHaveBeenCalledWith(line);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Line>>();
      const line = new Line();
      jest.spyOn(lineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ line });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: line }));
      saveSubject.complete();

      // THEN
      expect(lineService.create).toHaveBeenCalledWith(line);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Line>>();
      const line = { id: 123 };
      jest.spyOn(lineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ line });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lineService.update).toHaveBeenCalledWith(line);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBlockById', () => {
      it('Should return tracked Block primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBlockById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
