import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypePunch, TypePunch } from '../type-punch.model';
import { TypePunchService } from '../service/type-punch.service';

@Injectable({ providedIn: 'root' })
export class TypePunchRoutingResolveService implements Resolve<ITypePunch> {
  constructor(protected service: TypePunchService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypePunch> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typePunch: HttpResponse<TypePunch>) => {
          if (typePunch.body) {
            return of(typePunch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypePunch());
  }
}
