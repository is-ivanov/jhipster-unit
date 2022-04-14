import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PunchListComponent } from '../list/punch-list.component';
import { PunchListDetailComponent } from '../detail/punch-list-detail.component';
import { PunchListUpdateComponent } from '../update/punch-list-update.component';
import { PunchListRoutingResolveService } from './punch-list-routing-resolve.service';
import { Authority } from '../../../config/authority.constants';

const punchListRoute: Routes = [
  {
    path: '',
    component: PunchListComponent,
    data: {
      defaultSort: 'id,asc',
    },
  },
  {
    path: ':id/view',
    component: PunchListDetailComponent,
    resolve: {
      punchList: PunchListRoutingResolveService,
    },
  },
  {
    path: 'new',
    component: PunchListUpdateComponent,
    resolve: {
      punchList: PunchListRoutingResolveService,
    },
	  data: {
		  authorities: [Authority.ADMIN, Authority.CUSTOMER, Authority.COMMISSIONER],
	  },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PunchListUpdateComponent,
    resolve: {
      punchList: PunchListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(punchListRoute)],
  exports: [RouterModule],
})
export class PunchListRoutingModule {}
