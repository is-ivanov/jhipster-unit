import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PriorityPunchComponent } from '../list/priority-punch.component';
import { PriorityPunchDetailComponent } from '../detail/priority-punch-detail.component';
import { PriorityPunchUpdateComponent } from '../update/priority-punch-update.component';
import { PriorityPunchRoutingResolveService } from './priority-punch-routing-resolve.service';

const priorityPunchRoute: Routes = [
	{
		path: '',
		component: PriorityPunchComponent
		// canActivate: [UserRouteAccessService],
	},
	{
		path: ':id/view',
		component: PriorityPunchDetailComponent,
		resolve: {
			priorityPunch: PriorityPunchRoutingResolveService
		}
		// canActivate: [UserRouteAccessService],
	},
	{
		path: 'new',
		component: PriorityPunchUpdateComponent,
		resolve: {
			priorityPunch: PriorityPunchRoutingResolveService
		},
		data: {
			authorities: ['ROLE_ADMIN']
		},
		canActivate: [UserRouteAccessService]
	},
	{
		path: ':id/edit',
		component: PriorityPunchUpdateComponent,
		resolve: {
			priorityPunch: PriorityPunchRoutingResolveService
		},
		data: {
			authorities: ['ROLE_ADMIN']
		},
		canActivate: [UserRouteAccessService]
	}
];

@NgModule({
	imports: [RouterModule.forChild(priorityPunchRoute)],
	exports: [RouterModule]
})
export class PriorityPunchRoutingModule {
}
