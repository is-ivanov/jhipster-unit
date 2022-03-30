import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriorityPunch, getPriorityPunchIdentifier } from '../priority-punch.model';

export type EntityResponseType = HttpResponse<IPriorityPunch>;
export type EntityArrayResponseType = HttpResponse<IPriorityPunch[]>;

@Injectable({ providedIn: 'root' })
export class PriorityPunchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/priority-punches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(priorityPunch: IPriorityPunch): Observable<EntityResponseType> {
    return this.http.post<IPriorityPunch>(this.resourceUrl, priorityPunch, { observe: 'response' });
  }

  update(priorityPunch: IPriorityPunch): Observable<EntityResponseType> {
    return this.http.put<IPriorityPunch>(`${this.resourceUrl}/${getPriorityPunchIdentifier(priorityPunch) as number}`, priorityPunch, {
      observe: 'response',
    });
  }

  partialUpdate(priorityPunch: IPriorityPunch): Observable<EntityResponseType> {
    return this.http.patch<IPriorityPunch>(`${this.resourceUrl}/${getPriorityPunchIdentifier(priorityPunch) as number}`, priorityPunch, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPriorityPunch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriorityPunch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPriorityPunchToCollectionIfMissing(
    priorityPunchCollection: IPriorityPunch[],
    ...priorityPunchesToCheck: (IPriorityPunch | null | undefined)[]
  ): IPriorityPunch[] {
    const priorityPunches: IPriorityPunch[] = priorityPunchesToCheck.filter(isPresent);
    if (priorityPunches.length > 0) {
      const priorityPunchCollectionIdentifiers = priorityPunchCollection.map(
        priorityPunchItem => getPriorityPunchIdentifier(priorityPunchItem)!
      );
      const priorityPunchesToAdd = priorityPunches.filter(priorityPunchItem => {
        const priorityPunchIdentifier = getPriorityPunchIdentifier(priorityPunchItem);
        if (priorityPunchIdentifier == null || priorityPunchCollectionIdentifiers.includes(priorityPunchIdentifier)) {
          return false;
        }
        priorityPunchCollectionIdentifiers.push(priorityPunchIdentifier);
        return true;
      });
      return [...priorityPunchesToAdd, ...priorityPunchCollection];
    }
    return priorityPunchCollection;
  }
}
