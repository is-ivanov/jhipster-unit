import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypePunchComponent } from '../list/type-punch.component';
import { TypePunchDetailComponent } from '../detail/type-punch-detail.component';
import { TypePunchUpdateComponent } from '../update/type-punch-update.component';
import { TypePunchRoutingResolveService } from './type-punch-routing-resolve.service';

const typePunchRoute: Routes = [
	{
		path: '',
		component: TypePunchComponent
	},
	{
		path: ':id/view',
		component: TypePunchDetailComponent,
		resolve: {
			typePunch: TypePunchRoutingResolveService
		}
	},
	{
		path: 'new',
		component: TypePunchUpdateComponent,
		resolve: {
			typePunch: TypePunchRoutingResolveService
		},
		data: {
			authorities: ['ROLE_ADMIN']
		},
		canActivate: [UserRouteAccessService]
	},
	{
		path: ':id/edit',
		component: TypePunchUpdateComponent,
		resolve: {
			typePunch: TypePunchRoutingResolveService
		},
		data: {
			authorities: ['ROLE_ADMIN']
		},
		canActivate: [UserRouteAccessService]
	}
];

@NgModule({
	imports: [RouterModule.forChild(typePunchRoute)],
	exports: [RouterModule]
})
export class TypePunchRoutingModule {
}
