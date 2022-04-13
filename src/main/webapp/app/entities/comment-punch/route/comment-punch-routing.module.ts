import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommentPunchComponent } from '../list/comment-punch.component';
import { CommentPunchDetailComponent } from '../detail/comment-punch-detail.component';
import { CommentPunchUpdateComponent } from '../update/comment-punch-update.component';
import { CommentPunchRoutingResolveService } from './comment-punch-routing-resolve.service';

const commentPunchRoute: Routes = [
  {
    path: '',
    component: CommentPunchComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommentPunchDetailComponent,
    resolve: {
      commentPunch: CommentPunchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommentPunchUpdateComponent,
    resolve: {
      commentPunch: CommentPunchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommentPunchUpdateComponent,
    resolve: {
      commentPunch: CommentPunchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commentPunchRoute)],
  exports: [RouterModule],
})
export class CommentPunchRoutingModule {}
