import { Injectable, isDevMode } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { EMPTY, mergeMap, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountService } from '../../../core/auth/account.service';
import { StateStorageService } from '../../../core/auth/state-storage.service';
import { PunchListService } from '../service/punch-list.service';
import { HttpResponse } from '@angular/common/http';
import { IPunchList } from '../punch-list.model';

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

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
		const id = route.params['id'];
		let companyId = 0;
		return this.punchListService.find(id).pipe(
				mergeMap((punchList: HttpResponse<IPunchList>) => {
					if (punchList.body) {
						companyId = punchList.body.author?.company?.id ?? -2;
					} else {
						this.router.navigate(['404']);
						return EMPTY;
					}
					return this.accountService.identity()
						.pipe(map(account => {
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
						}));
				})
			);
	}

}
