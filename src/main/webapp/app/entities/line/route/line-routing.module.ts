import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LineComponent } from '../list/line.component';
import { LineDetailComponent } from '../detail/line-detail.component';
import { LineUpdateComponent } from '../update/line-update.component';
import { LineRoutingResolveService } from './line-routing-resolve.service';

const lineRoute: Routes = [
	{
		path: '',
		component: LineComponent,
		data: {
			defaultSort: 'id,asc',
		},
		// canActivate: [UserRouteAccessService],
	},
	{
		path: ':id/view',
		component: LineDetailComponent,
		resolve: {
			line: LineRoutingResolveService,
		},
		// canActivate: [UserRouteAccessService],
	},
	{
		path: 'new',
		component: LineUpdateComponent,
		resolve: {
			line: LineRoutingResolveService,
		},
		data: {
			authorities: ['ROLE_ADMIN'],
		},
		canActivate: [UserRouteAccessService],
	},
	{
		path: ':id/edit',
		component: LineUpdateComponent,
		resolve: {
			line: LineRoutingResolveService,
		},
		data: {
			authorities: ['ROLE_ADMIN'],
		},
		canActivate: [UserRouteAccessService],
	},
];

@NgModule({
	imports: [RouterModule.forChild(lineRoute)],
	exports: [RouterModule],
})
export class LineRoutingModule {}
