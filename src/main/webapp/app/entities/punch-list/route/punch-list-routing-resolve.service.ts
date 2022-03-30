import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPunchList, PunchList } from '../punch-list.model';
import { PunchListService } from '../service/punch-list.service';

@Injectable({ providedIn: 'root' })
export class PunchListRoutingResolveService implements Resolve<IPunchList> {
  constructor(protected service: PunchListService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPunchList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((punchList: HttpResponse<PunchList>) => {
          if (punchList.body) {
            return of(punchList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PunchList());
  }
}
