import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BlockComponent } from '../list/block.component';
import { BlockDetailComponent } from '../detail/block-detail.component';
import { BlockUpdateComponent } from '../update/block-update.component';
import { BlockRoutingResolveService } from './block-routing-resolve.service';

const blockRoute: Routes = [
  {
    path: '',
    component: BlockComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlockDetailComponent,
    resolve: {
      block: BlockRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlockUpdateComponent,
    resolve: {
      block: BlockRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlockUpdateComponent,
    resolve: {
      block: BlockRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(blockRoute)],
  exports: [RouterModule],
})
export class BlockRoutingModule {}
