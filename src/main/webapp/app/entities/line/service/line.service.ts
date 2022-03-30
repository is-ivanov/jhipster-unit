import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILine, getLineIdentifier } from '../line.model';

export type EntityResponseType = HttpResponse<ILine>;
export type EntityArrayResponseType = HttpResponse<ILine[]>;

@Injectable({ providedIn: 'root' })
export class LineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(line: ILine): Observable<EntityResponseType> {
    return this.http.post<ILine>(this.resourceUrl, line, { observe: 'response' });
  }

  update(line: ILine): Observable<EntityResponseType> {
    return this.http.put<ILine>(`${this.resourceUrl}/${getLineIdentifier(line) as number}`, line, { observe: 'response' });
  }

  partialUpdate(line: ILine): Observable<EntityResponseType> {
    return this.http.patch<ILine>(`${this.resourceUrl}/${getLineIdentifier(line) as number}`, line, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLineToCollectionIfMissing(lineCollection: ILine[], ...linesToCheck: (ILine | null | undefined)[]): ILine[] {
    const lines: ILine[] = linesToCheck.filter(isPresent);
    if (lines.length > 0) {
      const lineCollectionIdentifiers = lineCollection.map(lineItem => getLineIdentifier(lineItem)!);
      const linesToAdd = lines.filter(lineItem => {
        const lineIdentifier = getLineIdentifier(lineItem);
        if (lineIdentifier == null || lineCollectionIdentifiers.includes(lineIdentifier)) {
          return false;
        }
        lineCollectionIdentifiers.push(lineIdentifier);
        return true;
      });
      return [...linesToAdd, ...lineCollection];
    }
    return lineCollection;
  }
}
