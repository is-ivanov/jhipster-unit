import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypePunch, TypePunch } from '../type-punch.model';

import { TypePunchService } from './type-punch.service';

describe('TypePunch Service', () => {
  let service: TypePunchService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypePunch;
  let expectedResult: ITypePunch | ITypePunch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypePunchService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a TypePunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TypePunch()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypePunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a TypePunch', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new TypePunch()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypePunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a TypePunch', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTypePunchToCollectionIfMissing', () => {
      it('should add a TypePunch to an empty array', () => {
        const typePunch: ITypePunch = { id: 123 };
        expectedResult = service.addTypePunchToCollectionIfMissing([], typePunch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePunch);
      });

      it('should not add a TypePunch to an array that contains it', () => {
        const typePunch: ITypePunch = { id: 123 };
        const typePunchCollection: ITypePunch[] = [
          {
            ...typePunch,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypePunchToCollectionIfMissing(typePunchCollection, typePunch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypePunch to an array that doesn't contain it", () => {
        const typePunch: ITypePunch = { id: 123 };
        const typePunchCollection: ITypePunch[] = [{ id: 456 }];
        expectedResult = service.addTypePunchToCollectionIfMissing(typePunchCollection, typePunch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePunch);
      });

      it('should add only unique TypePunch to an array', () => {
        const typePunchArray: ITypePunch[] = [{ id: 123 }, { id: 456 }, { id: 70932 }];
        const typePunchCollection: ITypePunch[] = [{ id: 123 }];
        expectedResult = service.addTypePunchToCollectionIfMissing(typePunchCollection, ...typePunchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typePunch: ITypePunch = { id: 123 };
        const typePunch2: ITypePunch = { id: 456 };
        expectedResult = service.addTypePunchToCollectionIfMissing([], typePunch, typePunch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typePunch);
        expect(expectedResult).toContain(typePunch2);
      });

      it('should accept null and undefined values', () => {
        const typePunch: ITypePunch = { id: 123 };
        expectedResult = service.addTypePunchToCollectionIfMissing([], null, typePunch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typePunch);
      });

      it('should return initial array if no TypePunch is added', () => {
        const typePunchCollection: ITypePunch[] = [{ id: 123 }];
        expectedResult = service.addTypePunchToCollectionIfMissing(typePunchCollection, undefined, null);
        expect(expectedResult).toEqual(typePunchCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
