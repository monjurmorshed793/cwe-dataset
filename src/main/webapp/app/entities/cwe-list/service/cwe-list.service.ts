import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICweList } from '../cwe-list.model';

type EntityResponseType = HttpResponse<ICweList>;
type EntityArrayResponseType = HttpResponse<ICweList[]>;

@Injectable({ providedIn: 'root' })
export class CweListService {
  public resourceUrl = SERVER_API_URL + 'api/cwe-lists';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/cwe-lists';

  constructor(protected http: HttpClient) {}

  create(cweList: ICweList): Observable<EntityResponseType> {
    return this.http.post<ICweList>(this.resourceUrl, cweList, { observe: 'response' });
  }

  update(cweList: ICweList): Observable<EntityResponseType> {
    return this.http.put<ICweList>(this.resourceUrl, cweList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICweList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICweList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICweList[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
