import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { StatusLine } from 'app/entities/enumerations/status-line.model';
import { ILine, Line } from '../line.model';

import { LineService } from './line.service';

describe('Line Service', () => {
  let service: LineService;
  let httpMock: HttpTestingController;
  let elemDefault: ILine;
  let expectedResult: ILine | ILine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LineService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      tag: 'AAAAAAA',
      revision: 'AAAAAAA',
      status: StatusLine.NEW,
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

    it('should create a Line', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Line()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Line', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tag: 'BBBBBB',
          revision: 'BBBBBB',
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Line', () => {
      const patchObject = Object.assign(
        {
          tag: 'BBBBBB',
          status: 'BBBBBB',
        },
        new Line()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Line', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          tag: 'BBBBBB',
          revision: 'BBBBBB',
          status: 'BBBBBB',
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

    it('should delete a Line', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLineToCollectionIfMissing', () => {
      it('should add a Line to an empty array', () => {
        const line: ILine = { id: 123 };
        expectedResult = service.addLineToCollectionIfMissing([], line);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(line);
      });

      it('should not add a Line to an array that contains it', () => {
        const line: ILine = { id: 123 };
        const lineCollection: ILine[] = [
          {
            ...line,
          },
          { id: 456 },
        ];
        expectedResult = service.addLineToCollectionIfMissing(lineCollection, line);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Line to an array that doesn't contain it", () => {
        const line: ILine = { id: 123 };
        const lineCollection: ILine[] = [{ id: 456 }];
        expectedResult = service.addLineToCollectionIfMissing(lineCollection, line);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(line);
      });

      it('should add only unique Line to an array', () => {
        const lineArray: ILine[] = [{ id: 123 }, { id: 456 }, { id: 61524 }];
        const lineCollection: ILine[] = [{ id: 123 }];
        expectedResult = service.addLineToCollectionIfMissing(lineCollection, ...lineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const line: ILine = { id: 123 };
        const line2: ILine = { id: 456 };
        expectedResult = service.addLineToCollectionIfMissing([], line, line2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(line);
        expect(expectedResult).toContain(line2);
      });

      it('should accept null and undefined values', () => {
        const line: ILine = { id: 123 };
        expectedResult = service.addLineToCollectionIfMissing([], null, line, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(line);
      });

      it('should return initial array if no Line is added', () => {
        const lineCollection: ILine[] = [{ id: 123 }];
        expectedResult = service.addLineToCollectionIfMissing(lineCollection, undefined, null);
        expect(expectedResult).toEqual(lineCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
