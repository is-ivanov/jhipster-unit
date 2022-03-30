import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPunchList, PunchList } from '../punch-list.model';

import { PunchListService } from './punch-list.service';

describe('PunchList Service', () => {
  let service: PunchListService;
  let httpMock: HttpTestingController;
  let elemDefault: IPunchList;
  let expectedResult: IPunchList | IPunchList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PunchListService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      number: 0,
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

    it('should create a PunchList', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PunchList()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PunchList', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          number: 1,
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

    it('should partial update a PunchList', () => {
      const patchObject = Object.assign({}, new PunchList());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PunchList', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          number: 1,
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

    it('should delete a PunchList', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPunchListToCollectionIfMissing', () => {
      it('should add a PunchList to an empty array', () => {
        const punchList: IPunchList = { id: 123 };
        expectedResult = service.addPunchListToCollectionIfMissing([], punchList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(punchList);
      });

      it('should not add a PunchList to an array that contains it', () => {
        const punchList: IPunchList = { id: 123 };
        const punchListCollection: IPunchList[] = [
          {
            ...punchList,
          },
          { id: 456 },
        ];
        expectedResult = service.addPunchListToCollectionIfMissing(punchListCollection, punchList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PunchList to an array that doesn't contain it", () => {
        const punchList: IPunchList = { id: 123 };
        const punchListCollection: IPunchList[] = [{ id: 456 }];
        expectedResult = service.addPunchListToCollectionIfMissing(punchListCollection, punchList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(punchList);
      });

      it('should add only unique PunchList to an array', () => {
        const punchListArray: IPunchList[] = [{ id: 123 }, { id: 456 }, { id: 70019 }];
        const punchListCollection: IPunchList[] = [{ id: 123 }];
        expectedResult = service.addPunchListToCollectionIfMissing(punchListCollection, ...punchListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const punchList: IPunchList = { id: 123 };
        const punchList2: IPunchList = { id: 456 };
        expectedResult = service.addPunchListToCollectionIfMissing([], punchList, punchList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(punchList);
        expect(expectedResult).toContain(punchList2);
      });

      it('should accept null and undefined values', () => {
        const punchList: IPunchList = { id: 123 };
        expectedResult = service.addPunchListToCollectionIfMissing([], null, punchList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(punchList);
      });

      it('should return initial array if no PunchList is added', () => {
        const punchListCollection: IPunchList[] = [{ id: 123 }];
        expectedResult = service.addPunchListToCollectionIfMissing(punchListCollection, undefined, null);
        expect(expectedResult).toEqual(punchListCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
