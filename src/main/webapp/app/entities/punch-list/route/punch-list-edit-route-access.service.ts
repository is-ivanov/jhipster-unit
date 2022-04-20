import { Injectable, isDevMode } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountService } from '../../../core/auth/account.service';
import { StateStorageService } from '../../../core/auth/state-storage.service';
import { PunchListService } from '../service/punch-list.service';

@Injectable({
	providedIn: 'root'
})
export class PunchListEditRouteAccessService implements CanActivate {
	constructor(
		private router: Router,
		private accountService: AccountService,
		private stateStorageService: StateStorageService,
		private punchListService: PunchListService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
		const id = route.params['id'];
		let companyId = 0;
		if (id) {
			this.punchListService.find(id).subscribe((response) => {
				companyId = response.body?.author?.company?.id ?? -2;
				alert(companyId);
			})
			// const observablePunchList = this.punchListService.find(id).pipe(
			// 	mergeMap((punchList: HttpResponse<IPunchList>) => {
			// 		if (punchList.body) {
			// 			return of(punchList.body);
			// 		} else {
			// 			this.router.navigate(['404']);
			// 			return EMPTY;
			// 		}
			// 	})
			// )
			// observablePunchList.subscribe((punchList) => {
			// 	companyId = punchList.author?.company?.id ?? -2;
			// });
		}

		return this.accountService.identity().pipe(
			map(account => {


				if (account) {
					const authorities = route.data['authorities'];
					if (!authorities || authorities.length === 0 ||
						this.accountService.hasAnyAuthorityAndEqualCompany(authorities, companyId)) {
							return true;
					}

					if (isDevMode()) {
						console.error('User has not any of required authorities: ', authorities);
					}
					this.router.navigate(['accessdenied']);
					return false;
				}

				this.stateStorageService.storeUrl(state.url);
				this.router.navigate(['/login']);
				return false;
			})
		);
	}

}
