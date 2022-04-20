import { Directive, Input, OnDestroy, TemplateRef, ViewContainerRef } from '@angular/core';
import { Subject } from 'rxjs';
import { AccountService } from '../../core/auth/account.service';
import { takeUntil } from 'rxjs/operators';

@Directive({
  selector: '[jhiHasAnyAuthorityAndEqualCompany]'
})
export class HasAnyAuthorityAndEqualCompanyDirective implements OnDestroy{
	private authorities!: string | string[];
	private companyId!: number;

	private readonly destroy$ = new Subject<void>();

	constructor(
		private accountService: AccountService,
	  private templateRef: TemplateRef<any>,
		private viewContainerRef: ViewContainerRef
	) {}

	@Input()
	set jhiHasAnyAuthorityAndEqualCompanyCompanyId(value: number) {
		this.companyId = value;
		this.updateView();
		this.getNotifyAuthenticationStateChange();
	}

	@Input()
	set jhiHasAnyAuthorityAndEqualCompany(value: string | string[]) {
		this.authorities = value;
		this.updateView();
		// Get notified each time authentication state changes.
		this.getNotifyAuthenticationStateChange();
	}

	ngOnDestroy(): void {
		this.destroy$.next();
		this.destroy$.complete();
	}

	private updateView(): void {
		const hasAnyAuthority = this.accountService.hasAnyAuthorityAndEqualCompany(this.authorities, this.companyId);
		this.viewContainerRef.clear();
		if (hasAnyAuthority) {
			this.viewContainerRef.createEmbeddedView(this.templateRef);
		}
	}

	private getNotifyAuthenticationStateChange(): void {
		this.accountService
			.getAuthenticationState()
			.pipe(takeUntil(this.destroy$))
			.subscribe(() => {
				this.updateView();
			});
	}
}
