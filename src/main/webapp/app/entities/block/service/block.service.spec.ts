import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBlock, Block } from '../block.model';

import { BlockService } from './block.service';

describe('Block Service', () => {
  let service: BlockService;
  let httpMock: HttpTestingController;
  let elemDefault: IBlock;
  let expectedResult: IBlock | IBlock[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BlockService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      number: 0,
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

    it('should create a Block', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Block()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Block', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          number: 1,
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

    it('should partial update a Block', () => {
      const patchObject = Object.assign({}, new Block());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Block', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          number: 1,
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

    it('should delete a Block', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBlockToCollectionIfMissing', () => {
      it('should add a Block to an empty array', () => {
        const block: IBlock = { id: 123 };
        expectedResult = service.addBlockToCollectionIfMissing([], block);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(block);
      });

      it('should not add a Block to an array that contains it', () => {
        const block: IBlock = { id: 123 };
        const blockCollection: IBlock[] = [
          {
            ...block,
          },
          { id: 456 },
        ];
        expectedResult = service.addBlockToCollectionIfMissing(blockCollection, block);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Block to an array that doesn't contain it", () => {
        const block: IBlock = { id: 123 };
        const blockCollection: IBlock[] = [{ id: 456 }];
        expectedResult = service.addBlockToCollectionIfMissing(blockCollection, block);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(block);
      });

      it('should add only unique Block to an array', () => {
        const blockArray: IBlock[] = [{ id: 123 }, { id: 456 }, { id: 70663 }];
        const blockCollection: IBlock[] = [{ id: 123 }];
        expectedResult = service.addBlockToCollectionIfMissing(blockCollection, ...blockArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const block: IBlock = { id: 123 };
        const block2: IBlock = { id: 456 };
        expectedResult = service.addBlockToCollectionIfMissing([], block, block2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(block);
        expect(expectedResult).toContain(block2);
      });

      it('should accept null and undefined values', () => {
        const block: IBlock = { id: 123 };
        expectedResult = service.addBlockToCollectionIfMissing([], null, block, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(block);
      });

      it('should return initial array if no Block is added', () => {
        const blockCollection: IBlock[] = [{ id: 123 }];
        expectedResult = service.addBlockToCollectionIfMissing(blockCollection, undefined, null);
        expect(expectedResult).toEqual(blockCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
