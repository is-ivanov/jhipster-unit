import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPunchList, getPunchListIdentifier } from '../punch-list.model';

export type EntityResponseType = HttpResponse<IPunchList>;
export type EntityArrayResponseType = HttpResponse<IPunchList[]>;

@Injectable({ providedIn: 'root' })
export class PunchListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/punch-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(punchList: IPunchList): Observable<EntityResponseType> {
    return this.http.post<IPunchList>(this.resourceUrl, punchList, { observe: 'response' });
  }

  update(punchList: IPunchList): Observable<EntityResponseType> {
    return this.http.put<IPunchList>(`${this.resourceUrl}/${getPunchListIdentifier(punchList) as number}`, punchList, {
      observe: 'response',
    });
  }

  partialUpdate(punchList: IPunchList): Observable<EntityResponseType> {
    return this.http.patch<IPunchList>(`${this.resourceUrl}/${getPunchListIdentifier(punchList) as number}`, punchList, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPunchList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPunchList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPunchListToCollectionIfMissing(
    punchListCollection: IPunchList[],
    ...punchListsToCheck: (IPunchList | null | undefined)[]
  ): IPunchList[] {
    const punchLists: IPunchList[] = punchListsToCheck.filter(isPresent);
    if (punchLists.length > 0) {
      const punchListCollectionIdentifiers = punchListCollection.map(punchListItem => getPunchListIdentifier(punchListItem)!);
      const punchListsToAdd = punchLists.filter(punchListItem => {
        const punchListIdentifier = getPunchListIdentifier(punchListItem);
        if (punchListIdentifier == null || punchListCollectionIdentifiers.includes(punchListIdentifier)) {
          return false;
        }
        punchListCollectionIdentifiers.push(punchListIdentifier);
        return true;
      });
      return [...punchListsToAdd, ...punchListCollection];
    }
    return punchListCollection;
  }
}
