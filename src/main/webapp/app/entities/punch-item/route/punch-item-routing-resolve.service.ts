import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPunchItem, PunchItem } from '../punch-item.model';
import { PunchItemService } from '../service/punch-item.service';

@Injectable({ providedIn: 'root' })
export class PunchItemRoutingResolveService implements Resolve<IPunchItem> {
  constructor(protected service: PunchItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPunchItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((punchItem: HttpResponse<PunchItem>) => {
          if (punchItem.body) {
            return of(punchItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PunchItem());
  }
}
