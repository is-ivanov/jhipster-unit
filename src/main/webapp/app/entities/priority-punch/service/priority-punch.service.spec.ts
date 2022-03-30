import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPriorityPunch, PriorityPunch } from '../priority-punch.model';

import { PriorityPunchService } from './priority-punch.service';

describe('PriorityPunch Service', () => {
  let service: PriorityPunchService;
  let httpMock: HttpTestingController;
  let elemDefault: IPriorityPunch;
  let expectedResult: IPriorityPunch | IPriorityPunch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PriorityPunchService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      priority: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PriorityPunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PriorityPunch()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PriorityPunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          priority: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PriorityPunch', () => {
      const patchObject = Object.assign(
        {
          priority: 1,
        },
        new PriorityPunch()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PriorityPunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          priority: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PriorityPunch', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPriorityPunchToCollectionIfMissing', () => {
      it('should add a PriorityPunch to an empty array', () => {
        const priorityPunch: IPriorityPunch = { id: 123 };
        expectedResult = service.addPriorityPunchToCollectionIfMissing([], priorityPunch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priorityPunch);
      });

      it('should not add a PriorityPunch to an array that contains it', () => {
        const priorityPunch: IPriorityPunch = { id: 123 };
        const priorityPunchCollection: IPriorityPunch[] = [
          {
            ...priorityPunch,
          },
          { id: 456 },
        ];
        expectedResult = service.addPriorityPunchToCollectionIfMissing(priorityPunchCollection, priorityPunch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PriorityPunch to an array that doesn't contain it", () => {
        const priorityPunch: IPriorityPunch = { id: 123 };
        const priorityPunchCollection: IPriorityPunch[] = [{ id: 456 }];
        expectedResult = service.addPriorityPunchToCollectionIfMissing(priorityPunchCollection, priorityPunch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priorityPunch);
      });

      it('should add only unique PriorityPunch to an array', () => {
        const priorityPunchArray: IPriorityPunch[] = [{ id: 123 }, { id: 456 }, { id: 83886 }];
        const priorityPunchCollection: IPriorityPunch[] = [{ id: 123 }];
        expectedResult = service.addPriorityPunchToCollectionIfMissing(priorityPunchCollection, ...priorityPunchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const priorityPunch: IPriorityPunch = { id: 123 };
        const priorityPunch2: IPriorityPunch = { id: 456 };
        expectedResult = service.addPriorityPunchToCollectionIfMissing([], priorityPunch, priorityPunch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priorityPunch);
        expect(expectedResult).toContain(priorityPunch2);
      });

      it('should accept null and undefined values', () => {
        const priorityPunch: IPriorityPunch = { id: 123 };
        expectedResult = service.addPriorityPunchToCollectionIfMissing([], null, priorityPunch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priorityPunch);
      });

      it('should return initial array if no PriorityPunch is added', () => {
        const priorityPunchCollection: IPriorityPunch[] = [{ id: 123 }];
        expectedResult = service.addPriorityPunchToCollectionIfMissing(priorityPunchCollection, undefined, null);
        expect(expectedResult).toEqual(priorityPunchCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
