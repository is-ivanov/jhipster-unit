import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPriorityPunch, PriorityPunch } from '../priority-punch.model';
import { PriorityPunchService } from '../service/priority-punch.service';

import { PriorityPunchRoutingResolveService } from './priority-punch-routing-resolve.service';

describe('PriorityPunch routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PriorityPunchRoutingResolveService;
  let service: PriorityPunchService;
  let resultPriorityPunch: IPriorityPunch | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PriorityPunchRoutingResolveService);
    service = TestBed.inject(PriorityPunchService);
    resultPriorityPunch = undefined;
  });

  describe('resolve', () => {
    it('should return IPriorityPunch returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPriorityPunch = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPriorityPunch).toEqual({ id: 123 });
    });

    it('should return new IPriorityPunch if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPriorityPunch = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPriorityPunch).toEqual(new PriorityPunch());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PriorityPunch })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPriorityPunch = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPriorityPunch).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
