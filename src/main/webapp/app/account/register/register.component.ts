import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';
import { CompanyService, EntityArrayResponseType } from '../../entities/company/service/company.service';
import { ICompany } from '../../entities/company/company.model';

@Component({
	selector: 'jhi-register',
	templateUrl: './register.component.html',
})
export class RegisterComponent implements AfterViewInit, OnInit {
	@ViewChild('login', { static: false })
	login?: ElementRef;
	companies?: ICompany[];

	doNotMatch = false;
	error = false;
	errorEmailExists = false;
	errorUserExists = false;
	success = false;

	registerForm = this.fb.group({
		firstName: ['', [Validators.required, Validators.maxLength(50)]],
		lastName: ['', [Validators.required, Validators.maxLength(50)]],
		login: [
			'',
			[
				Validators.required,
				Validators.minLength(1),
				Validators.maxLength(50),
				Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
			],
		],
		email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
		password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
		confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
		company: ['', Validators.required],
	});

	constructor(
		private translateService: TranslateService,
		private registerService: RegisterService,
		private companyService: CompanyService,
		private fb: FormBuilder
	) {}

	ngOnInit(): void {
		this.companyService.query().subscribe((data: EntityArrayResponseType) => {
			this.companies = data.body ?? [];
		});
	}

	ngAfterViewInit(): void {
		if (this.login) {
			this.login.nativeElement.focus();
		}
	}

	register(): void {
		this.doNotMatch = false;
		this.error = false;
		this.errorEmailExists = false;
		this.errorUserExists = false;

		const password = this.registerForm.get(['password'])!.value;
		if (password !== this.registerForm.get(['confirmPassword'])!.value) {
			this.doNotMatch = true;
		} else {
			const firstName = this.registerForm.get(['firstName'])!.value;
			const lastName = this.registerForm.get(['lastName'])!.value;
			const login = this.registerForm.get(['login'])!.value;
			const email = this.registerForm.get(['email'])!.value;
			const companyId = +this.registerForm.get(['company'])!.value;
			this.registerService
				.save({ firstName, lastName, login, email, password, companyId, langKey: this.translateService.currentLang })
				.subscribe({
					next: () => (this.success = true),
					error: (response) => this.processError(response),
				});
		}
	}

	private processError(response: HttpErrorResponse): void {
		if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
			this.errorUserExists = true;
		} else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
			this.errorEmailExists = true;
		} else {
			this.error = true;
		}
	}
}
