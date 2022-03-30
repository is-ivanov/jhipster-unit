import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPriorityPunch, PriorityPunch } from '../priority-punch.model';
import { PriorityPunchService } from '../service/priority-punch.service';

@Injectable({ providedIn: 'root' })
export class PriorityPunchRoutingResolveService implements Resolve<IPriorityPunch> {
  constructor(protected service: PriorityPunchService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPriorityPunch> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((priorityPunch: HttpResponse<PriorityPunch>) => {
          if (priorityPunch.body) {
            return of(priorityPunch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PriorityPunch());
  }
}
