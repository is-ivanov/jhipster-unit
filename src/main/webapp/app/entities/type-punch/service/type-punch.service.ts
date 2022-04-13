import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypePunch, getTypePunchIdentifier } from '../type-punch.model';

export type EntityResponseType = HttpResponse<ITypePunch>;
export type EntityArrayResponseType = HttpResponse<ITypePunch[]>;

@Injectable({ providedIn: 'root' })
export class TypePunchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-punches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typePunch: ITypePunch): Observable<EntityResponseType> {
    return this.http.post<ITypePunch>(this.resourceUrl, typePunch, { observe: 'response' });
  }

  update(typePunch: ITypePunch): Observable<EntityResponseType> {
    return this.http.put<ITypePunch>(`${this.resourceUrl}/${getTypePunchIdentifier(typePunch) as number}`, typePunch, {
      observe: 'response',
    });
  }

  partialUpdate(typePunch: ITypePunch): Observable<EntityResponseType> {
    return this.http.patch<ITypePunch>(`${this.resourceUrl}/${getTypePunchIdentifier(typePunch) as number}`, typePunch, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypePunch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypePunch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypePunchToCollectionIfMissing(
    typePunchCollection: ITypePunch[],
    ...typePunchesToCheck: (ITypePunch | null | undefined)[]
  ): ITypePunch[] {
    const typePunches: ITypePunch[] = typePunchesToCheck.filter(isPresent);
    if (typePunches.length > 0) {
      const typePunchCollectionIdentifiers = typePunchCollection.map(typePunchItem => getTypePunchIdentifier(typePunchItem)!);
      const typePunchesToAdd = typePunches.filter(typePunchItem => {
        const typePunchIdentifier = getTypePunchIdentifier(typePunchItem);
        if (typePunchIdentifier == null || typePunchCollectionIdentifiers.includes(typePunchIdentifier)) {
          return false;
        }
        typePunchCollectionIdentifiers.push(typePunchIdentifier);
        return true;
      });
      return [...typePunchesToAdd, ...typePunchCollection];
    }
    return typePunchCollection;
  }
}
