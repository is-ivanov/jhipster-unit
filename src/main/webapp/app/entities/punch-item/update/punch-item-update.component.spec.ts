import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PunchItemService } from '../service/punch-item.service';
import { IPunchItem, PunchItem } from '../punch-item.model';
import { ITypePunch } from 'app/entities/type-punch/type-punch.model';
import { TypePunchService } from 'app/entities/type-punch/service/type-punch.service';
import { ILine } from 'app/entities/line/line.model';
import { LineService } from 'app/entities/line/service/line.service';
import { IPunchList } from 'app/entities/punch-list/punch-list.model';
import { PunchListService } from 'app/entities/punch-list/service/punch-list.service';
import { IPriorityPunch } from 'app/entities/priority-punch/priority-punch.model';
import { PriorityPunchService } from 'app/entities/priority-punch/service/priority-punch.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PunchItemUpdateComponent } from './punch-item-update.component';

describe('PunchItem Management Update Component', () => {
  let comp: PunchItemUpdateComponent;
  let fixture: ComponentFixture<PunchItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let punchItemService: PunchItemService;
  let typePunchService: TypePunchService;
  let lineService: LineService;
  let punchListService: PunchListService;
  let priorityPunchService: PriorityPunchService;
  let companyService: CompanyService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PunchItemUpdateComponent],
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
      .overrideTemplate(PunchItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PunchItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    punchItemService = TestBed.inject(PunchItemService);
    typePunchService = TestBed.inject(TypePunchService);
    lineService = TestBed.inject(LineService);
    punchListService = TestBed.inject(PunchListService);
    priorityPunchService = TestBed.inject(PriorityPunchService);
    companyService = TestBed.inject(CompanyService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypePunch query and add missing value', () => {
      const punchItem: IPunchItem = { id: 456 };
      const type: ITypePunch = { id: 92332 };
      punchItem.type = type;

      const typePunchCollection: ITypePunch[] = [{ id: 50426 }];
      jest.spyOn(typePunchService, 'query').mockReturnValue(of(new HttpResponse({ body: typePunchCollection })));
      const additionalTypePunches = [type];
      const expectedCollection: ITypePunch[] = [...additionalTypePunches, ...typePunchCollection];
      jest.spyOn(typePunchService, 'addTypePunchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(typePunchService.query).toHaveBeenCalled();
      expect(typePunchService.addTypePunchToCollectionIfMissing).toHaveBeenCalledWith(typePunchCollection, ...additionalTypePunches);
      expect(comp.typePunchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Line query and add missing value', () => {
      const punchItem: IPunchItem = { id: 456 };
      const line: ILine = { id: 93317 };
      punchItem.line = line;

      const lineCollection: ILine[] = [{ id: 57803 }];
      jest.spyOn(lineService, 'query').mockReturnValue(of(new HttpResponse({ body: lineCollection })));
      const additionalLines = [line];
      const expectedCollection: ILine[] = [...additionalLines, ...lineCollection];
      jest.spyOn(lineService, 'addLineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(lineService.query).toHaveBeenCalled();
      expect(lineService.addLineToCollectionIfMissing).toHaveBeenCalledWith(lineCollection, ...additionalLines);
      expect(comp.linesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PunchList query and add missing value', () => {
      const punchItem: IPunchItem = { id: 456 };
      const punchList: IPunchList = { id: 12565 };
      punchItem.punchList = punchList;

      const punchListCollection: IPunchList[] = [{ id: 87476 }];
      jest.spyOn(punchListService, 'query').mockReturnValue(of(new HttpResponse({ body: punchListCollection })));
      const additionalPunchLists = [punchList];
      const expectedCollection: IPunchList[] = [...additionalPunchLists, ...punchListCollection];
      jest.spyOn(punchListService, 'addPunchListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(punchListService.query).toHaveBeenCalled();
      expect(punchListService.addPunchListToCollectionIfMissing).toHaveBeenCalledWith(punchListCollection, ...additionalPunchLists);
      expect(comp.punchListsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PriorityPunch query and add missing value', () => {
      const punchItem: IPunchItem = { id: 456 };
      const priority: IPriorityPunch = { id: 49911 };
      punchItem.priority = priority;

      const priorityPunchCollection: IPriorityPunch[] = [{ id: 6342 }];
      jest.spyOn(priorityPunchService, 'query').mockReturnValue(of(new HttpResponse({ body: priorityPunchCollection })));
      const additionalPriorityPunches = [priority];
      const expectedCollection: IPriorityPunch[] = [...additionalPriorityPunches, ...priorityPunchCollection];
      jest.spyOn(priorityPunchService, 'addPriorityPunchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(priorityPunchService.query).toHaveBeenCalled();
      expect(priorityPunchService.addPriorityPunchToCollectionIfMissing).toHaveBeenCalledWith(
        priorityPunchCollection,
        ...additionalPriorityPunches
      );
      expect(comp.priorityPunchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const punchItem: IPunchItem = { id: 456 };
      const executor: ICompany = { id: 32076 };
      punchItem.executor = executor;

      const companyCollection: ICompany[] = [{ id: 22196 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [executor];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const punchItem: IPunchItem = { id: 456 };
      const author: IUser = { id: 41319 };
      punchItem.author = author;

      const userCollection: IUser[] = [{ id: 69994 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [author];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const punchItem: IPunchItem = { id: 456 };
      const type: ITypePunch = { id: 30005 };
      punchItem.type = type;
      const line: ILine = { id: 85219 };
      punchItem.line = line;
      const punchList: IPunchList = { id: 44708 };
      punchItem.punchList = punchList;
      const priority: IPriorityPunch = { id: 50923 };
      punchItem.priority = priority;
      const executor: ICompany = { id: 78663 };
      punchItem.executor = executor;
      const author: IUser = { id: 87218 };
      punchItem.author = author;

      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(punchItem));
      expect(comp.typePunchesSharedCollection).toContain(type);
      expect(comp.linesSharedCollection).toContain(line);
      expect(comp.punchListsSharedCollection).toContain(punchList);
      expect(comp.priorityPunchesSharedCollection).toContain(priority);
      expect(comp.companiesSharedCollection).toContain(executor);
      expect(comp.usersSharedCollection).toContain(author);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PunchItem>>();
      const punchItem = { id: 123 };
      jest.spyOn(punchItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: punchItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(punchItemService.update).toHaveBeenCalledWith(punchItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PunchItem>>();
      const punchItem = new PunchItem();
      jest.spyOn(punchItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: punchItem }));
      saveSubject.complete();

      // THEN
      expect(punchItemService.create).toHaveBeenCalledWith(punchItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PunchItem>>();
      const punchItem = { id: 123 };
      jest.spyOn(punchItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ punchItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(punchItemService.update).toHaveBeenCalledWith(punchItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTypePunchById', () => {
      it('Should return tracked TypePunch primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTypePunchById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLineById', () => {
      it('Should return tracked Line primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLineById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPunchListById', () => {
      it('Should return tracked PunchList primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPunchListById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPriorityPunchById', () => {
      it('Should return tracked PriorityPunch primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPriorityPunchById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompanyById', () => {
      it('Should return tracked Company primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
