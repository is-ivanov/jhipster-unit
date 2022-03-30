import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommentPunch, CommentPunch } from '../comment-punch.model';

import { CommentPunchService } from './comment-punch.service';

describe('CommentPunch Service', () => {
  let service: CommentPunchService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommentPunch;
  let expectedResult: ICommentPunch | ICommentPunch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommentPunchService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      comment: 'AAAAAAA',
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

    it('should create a CommentPunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CommentPunch()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommentPunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          comment: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommentPunch', () => {
      const patchObject = Object.assign({}, new CommentPunch());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommentPunch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          comment: 'BBBBBB',
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

    it('should delete a CommentPunch', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommentPunchToCollectionIfMissing', () => {
      it('should add a CommentPunch to an empty array', () => {
        const commentPunch: ICommentPunch = { id: 123 };
        expectedResult = service.addCommentPunchToCollectionIfMissing([], commentPunch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commentPunch);
      });

      it('should not add a CommentPunch to an array that contains it', () => {
        const commentPunch: ICommentPunch = { id: 123 };
        const commentPunchCollection: ICommentPunch[] = [
          {
            ...commentPunch,
          },
          { id: 456 },
        ];
        expectedResult = service.addCommentPunchToCollectionIfMissing(commentPunchCollection, commentPunch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommentPunch to an array that doesn't contain it", () => {
        const commentPunch: ICommentPunch = { id: 123 };
        const commentPunchCollection: ICommentPunch[] = [{ id: 456 }];
        expectedResult = service.addCommentPunchToCollectionIfMissing(commentPunchCollection, commentPunch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commentPunch);
      });

      it('should add only unique CommentPunch to an array', () => {
        const commentPunchArray: ICommentPunch[] = [{ id: 123 }, { id: 456 }, { id: 16977 }];
        const commentPunchCollection: ICommentPunch[] = [{ id: 123 }];
        expectedResult = service.addCommentPunchToCollectionIfMissing(commentPunchCollection, ...commentPunchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commentPunch: ICommentPunch = { id: 123 };
        const commentPunch2: ICommentPunch = { id: 456 };
        expectedResult = service.addCommentPunchToCollectionIfMissing([], commentPunch, commentPunch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commentPunch);
        expect(expectedResult).toContain(commentPunch2);
      });

      it('should accept null and undefined values', () => {
        const commentPunch: ICommentPunch = { id: 123 };
        expectedResult = service.addCommentPunchToCollectionIfMissing([], null, commentPunch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commentPunch);
      });

      it('should return initial array if no CommentPunch is added', () => {
        const commentPunchCollection: ICommentPunch[] = [{ id: 123 }];
        expectedResult = service.addCommentPunchToCollectionIfMissing(commentPunchCollection, undefined, null);
        expect(expectedResult).toEqual(commentPunchCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
