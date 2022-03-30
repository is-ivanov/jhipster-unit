import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILine, Line } from '../line.model';
import { LineService } from '../service/line.service';

@Injectable({ providedIn: 'root' })
export class LineRoutingResolveService implements Resolve<ILine> {
  constructor(protected service: LineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILine> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((line: HttpResponse<Line>) => {
          if (line.body) {
            return of(line.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Line());
  }
}
