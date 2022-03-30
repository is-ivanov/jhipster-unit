import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlock, getBlockIdentifier } from '../block.model';

export type EntityResponseType = HttpResponse<IBlock>;
export type EntityArrayResponseType = HttpResponse<IBlock[]>;

@Injectable({ providedIn: 'root' })
export class BlockService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/blocks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(block: IBlock): Observable<EntityResponseType> {
    return this.http.post<IBlock>(this.resourceUrl, block, { observe: 'response' });
  }

  update(block: IBlock): Observable<EntityResponseType> {
    return this.http.put<IBlock>(`${this.resourceUrl}/${getBlockIdentifier(block) as number}`, block, { observe: 'response' });
  }

  partialUpdate(block: IBlock): Observable<EntityResponseType> {
    return this.http.patch<IBlock>(`${this.resourceUrl}/${getBlockIdentifier(block) as number}`, block, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBlock>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBlock[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBlockToCollectionIfMissing(blockCollection: IBlock[], ...blocksToCheck: (IBlock | null | undefined)[]): IBlock[] {
    const blocks: IBlock[] = blocksToCheck.filter(isPresent);
    if (blocks.length > 0) {
      const blockCollectionIdentifiers = blockCollection.map(blockItem => getBlockIdentifier(blockItem)!);
      const blocksToAdd = blocks.filter(blockItem => {
        const blockIdentifier = getBlockIdentifier(blockItem);
        if (blockIdentifier == null || blockCollectionIdentifiers.includes(blockIdentifier)) {
          return false;
        }
        blockCollectionIdentifiers.push(blockIdentifier);
        return true;
      });
      return [...blocksToAdd, ...blockCollection];
    }
    return blockCollection;
  }
}
