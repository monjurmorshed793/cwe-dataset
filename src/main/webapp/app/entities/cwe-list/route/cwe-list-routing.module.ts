import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CweListComponent } from '../list/cwe-list.component';
import { CweListDetailComponent } from '../detail/cwe-list-detail.component';
import { CweListUpdateComponent } from '../update/cwe-list-update.component';
import { CweListRoutingResolveService } from './cwe-list-routing-resolve.service';

const cweListRoute: Routes = [
  {
    path: '',
    component: CweListComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CweListDetailComponent,
    resolve: {
      cweList: CweListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CweListUpdateComponent,
    resolve: {
      cweList: CweListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CweListUpdateComponent,
    resolve: {
      cweList: CweListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cweListRoute)],
  exports: [RouterModule],
})
export class CweListRoutingModule {}
