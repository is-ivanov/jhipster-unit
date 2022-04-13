import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPunchItem, getPunchItemIdentifier } from '../punch-item.model';

export type EntityResponseType = HttpResponse<IPunchItem>;
export type EntityArrayResponseType = HttpResponse<IPunchItem[]>;

@Injectable({ providedIn: 'root' })
export class PunchItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/punch-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(punchItem: IPunchItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(punchItem);
    return this.http
      .post<IPunchItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(punchItem: IPunchItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(punchItem);
    return this.http
      .put<IPunchItem>(`${this.resourceUrl}/${getPunchItemIdentifier(punchItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(punchItem: IPunchItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(punchItem);
    return this.http
      .patch<IPunchItem>(`${this.resourceUrl}/${getPunchItemIdentifier(punchItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPunchItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPunchItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPunchItemToCollectionIfMissing(
    punchItemCollection: IPunchItem[],
    ...punchItemsToCheck: (IPunchItem | null | undefined)[]
  ): IPunchItem[] {
    const punchItems: IPunchItem[] = punchItemsToCheck.filter(isPresent);
    if (punchItems.length > 0) {
      const punchItemCollectionIdentifiers = punchItemCollection.map(punchItemItem => getPunchItemIdentifier(punchItemItem)!);
      const punchItemsToAdd = punchItems.filter(punchItemItem => {
        const punchItemIdentifier = getPunchItemIdentifier(punchItemItem);
        if (punchItemIdentifier == null || punchItemCollectionIdentifiers.includes(punchItemIdentifier)) {
          return false;
        }
        punchItemCollectionIdentifiers.push(punchItemIdentifier);
        return true;
      });
      return [...punchItemsToAdd, ...punchItemCollection];
    }
    return punchItemCollection;
  }

  protected convertDateFromClient(punchItem: IPunchItem): IPunchItem {
    return Object.assign({}, punchItem, {
      closedDate: punchItem.closedDate?.isValid() ? punchItem.closedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.closedDate = res.body.closedDate ? dayjs(res.body.closedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((punchItem: IPunchItem) => {
        punchItem.closedDate = punchItem.closedDate ? dayjs(punchItem.closedDate) : undefined;
      });
    }
    return res;
  }
}
