import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICweList, CweList } from '../cwe-list.model';
import { CweListService } from '../service/cwe-list.service';

@Injectable({ providedIn: 'root' })
export class CweListRoutingResolveService implements Resolve<ICweList> {
  constructor(private service: CweListService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICweList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cweList: HttpResponse<CweList>) => {
          if (cweList.body) {
            return of(cweList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CweList());
  }
}
