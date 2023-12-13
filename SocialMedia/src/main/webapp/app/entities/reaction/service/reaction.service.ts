import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReaction, NewReaction } from '../reaction.model';

export type PartialUpdateReaction = Partial<IReaction> & Pick<IReaction, 'id'>;

type RestOf<T extends IReaction | NewReaction> = Omit<T, 'time'> & {
  time?: string | null;
};

export type RestReaction = RestOf<IReaction>;

export type NewRestReaction = RestOf<NewReaction>;

export type PartialUpdateRestReaction = RestOf<PartialUpdateReaction>;

export type EntityResponseType = HttpResponse<IReaction>;
export type EntityArrayResponseType = HttpResponse<IReaction[]>;

@Injectable({ providedIn: 'root' })
export class ReactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reactions');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(reaction: NewReaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reaction);
    return this.http
      .post<RestReaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reaction: IReaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reaction);
    return this.http
      .put<RestReaction>(`${this.resourceUrl}/${this.getReactionIdentifier(reaction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reaction: PartialUpdateReaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reaction);
    return this.http
      .patch<RestReaction>(`${this.resourceUrl}/${this.getReactionIdentifier(reaction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReactionIdentifier(reaction: Pick<IReaction, 'id'>): number {
    return reaction.id;
  }

  compareReaction(o1: Pick<IReaction, 'id'> | null, o2: Pick<IReaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getReactionIdentifier(o1) === this.getReactionIdentifier(o2) : o1 === o2;
  }

  addReactionToCollectionIfMissing<Type extends Pick<IReaction, 'id'>>(
    reactionCollection: Type[],
    ...reactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reactions: Type[] = reactionsToCheck.filter(isPresent);
    if (reactions.length > 0) {
      const reactionCollectionIdentifiers = reactionCollection.map(reactionItem => this.getReactionIdentifier(reactionItem)!);
      const reactionsToAdd = reactions.filter(reactionItem => {
        const reactionIdentifier = this.getReactionIdentifier(reactionItem);
        if (reactionCollectionIdentifiers.includes(reactionIdentifier)) {
          return false;
        }
        reactionCollectionIdentifiers.push(reactionIdentifier);
        return true;
      });
      return [...reactionsToAdd, ...reactionCollection];
    }
    return reactionCollection;
  }

  protected convertDateFromClient<T extends IReaction | NewReaction | PartialUpdateReaction>(reaction: T): RestOf<T> {
    return {
      ...reaction,
      time: reaction.time?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restReaction: RestReaction): IReaction {
    return {
      ...restReaction,
      time: restReaction.time ? dayjs(restReaction.time) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReaction>): HttpResponse<IReaction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReaction[]>): HttpResponse<IReaction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
