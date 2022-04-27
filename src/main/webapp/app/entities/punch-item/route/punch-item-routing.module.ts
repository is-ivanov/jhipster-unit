import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PunchItemComponent } from '../list/punch-item.component';
import { PunchItemDetailComponent } from '../detail/punch-item-detail.component';
import { PunchItemUpdateComponent } from '../update/punch-item-update.component';
import { PunchItemRoutingResolveService } from './punch-item-routing-resolve.service';

const punchItemRoute: Routes = [
  {
    path: '',
    component: PunchItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
  },
  {
    path: ':id/view',
    component: PunchItemDetailComponent,
    resolve: {
      punchItem: PunchItemRoutingResolveService,
    },
  },
  {
    path: 'new',
    component: PunchItemUpdateComponent,
    resolve: {
      punchItem: PunchItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PunchItemUpdateComponent,
    resolve: {
      punchItem: PunchItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(punchItemRoute)],
  exports: [RouterModule],
})
export class PunchItemRoutingModule {}
