import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { StatusPunch } from 'app/entities/enumerations/status-punch.model';
import { IPunchItem, PunchItem } from '../punch-item.model';

import { PunchItemService } from './punch-item.service';

describe('PunchItem Service', () => {
  let service: PunchItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IPunchItem;
  let expectedResult: IPunchItem | IPunchItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PunchItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      number: 0,
      location: 'AAAAAAA',
      description: 'AAAAAAA',
      revisionDrawing: 'AAAAAAA',
      status: StatusPunch.INITIATED,
      closedDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          closedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PunchItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          closedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          closedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PunchItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PunchItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          number: 1,
          location: 'BBBBBB',
          description: 'BBBBBB',
          revisionDrawing: 'BBBBBB',
          status: 'BBBBBB',
          closedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          closedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PunchItem', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          revisionDrawing: 'BBBBBB',
        },
        new PunchItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          closedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PunchItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          number: 1,
          location: 'BBBBBB',
          description: 'BBBBBB',
          revisionDrawing: 'BBBBBB',
          status: 'BBBBBB',
          closedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          closedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PunchItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPunchItemToCollectionIfMissing', () => {
      it('should add a PunchItem to an empty array', () => {
        const punchItem: IPunchItem = { id: 123 };
        expectedResult = service.addPunchItemToCollectionIfMissing([], punchItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(punchItem);
      });

      it('should not add a PunchItem to an array that contains it', () => {
        const punchItem: IPunchItem = { id: 123 };
        const punchItemCollection: IPunchItem[] = [
          {
            ...punchItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addPunchItemToCollectionIfMissing(punchItemCollection, punchItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PunchItem to an array that doesn't contain it", () => {
        const punchItem: IPunchItem = { id: 123 };
        const punchItemCollection: IPunchItem[] = [{ id: 456 }];
        expectedResult = service.addPunchItemToCollectionIfMissing(punchItemCollection, punchItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(punchItem);
      });

      it('should add only unique PunchItem to an array', () => {
        const punchItemArray: IPunchItem[] = [{ id: 123 }, { id: 456 }, { id: 57923 }];
        const punchItemCollection: IPunchItem[] = [{ id: 123 }];
        expectedResult = service.addPunchItemToCollectionIfMissing(punchItemCollection, ...punchItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const punchItem: IPunchItem = { id: 123 };
        const punchItem2: IPunchItem = { id: 456 };
        expectedResult = service.addPunchItemToCollectionIfMissing([], punchItem, punchItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(punchItem);
        expect(expectedResult).toContain(punchItem2);
      });

      it('should accept null and undefined values', () => {
        const punchItem: IPunchItem = { id: 123 };
        expectedResult = service.addPunchItemToCollectionIfMissing([], null, punchItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(punchItem);
      });

      it('should return initial array if no PunchItem is added', () => {
        const punchItemCollection: IPunchItem[] = [{ id: 123 }];
        expectedResult = service.addPunchItemToCollectionIfMissing(punchItemCollection, undefined, null);
        expect(expectedResult).toEqual(punchItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
