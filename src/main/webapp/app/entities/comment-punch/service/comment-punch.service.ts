import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommentPunch, getCommentPunchIdentifier } from '../comment-punch.model';

export type EntityResponseType = HttpResponse<ICommentPunch>;
export type EntityArrayResponseType = HttpResponse<ICommentPunch[]>;

@Injectable({ providedIn: 'root' })
export class CommentPunchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comment-punches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commentPunch: ICommentPunch): Observable<EntityResponseType> {
    return this.http.post<ICommentPunch>(this.resourceUrl, commentPunch, { observe: 'response' });
  }

  update(commentPunch: ICommentPunch): Observable<EntityResponseType> {
    return this.http.put<ICommentPunch>(`${this.resourceUrl}/${getCommentPunchIdentifier(commentPunch) as number}`, commentPunch, {
      observe: 'response',
    });
  }

  partialUpdate(commentPunch: ICommentPunch): Observable<EntityResponseType> {
    return this.http.patch<ICommentPunch>(`${this.resourceUrl}/${getCommentPunchIdentifier(commentPunch) as number}`, commentPunch, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommentPunch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommentPunch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommentPunchToCollectionIfMissing(
    commentPunchCollection: ICommentPunch[],
    ...commentPunchesToCheck: (ICommentPunch | null | undefined)[]
  ): ICommentPunch[] {
    const commentPunches: ICommentPunch[] = commentPunchesToCheck.filter(isPresent);
    if (commentPunches.length > 0) {
      const commentPunchCollectionIdentifiers = commentPunchCollection.map(
        commentPunchItem => getCommentPunchIdentifier(commentPunchItem)!
      );
      const commentPunchesToAdd = commentPunches.filter(commentPunchItem => {
        const commentPunchIdentifier = getCommentPunchIdentifier(commentPunchItem);
        if (commentPunchIdentifier == null || commentPunchCollectionIdentifiers.includes(commentPunchIdentifier)) {
          return false;
        }
        commentPunchCollectionIdentifiers.push(commentPunchIdentifier);
        return true;
      });
      return [...commentPunchesToAdd, ...commentPunchCollection];
    }
    return commentPunchCollection;
  }
}
