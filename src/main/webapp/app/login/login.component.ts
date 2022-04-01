import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
	selector: 'jhi-login',
	templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit, AfterViewInit {
	@ViewChild('username', { static: false })
	username!: ElementRef;

	authenticationError = false;
	accountNotActivatedError = false;

	loginForm = this.fb.group({
		username: [null, [Validators.required]],
		password: [null, [Validators.required]],
		rememberMe: [false]
	});

	constructor(
		private accountService: AccountService,
		private loginService: LoginService,
		private router: Router,
		private fb: FormBuilder
	) {
	}

	ngOnInit(): void {
		// if already authenticated then navigate to home page
		this.accountService.identity().subscribe(() => {
			if (this.accountService.isAuthenticated()) {
				this.router.navigate(['']);
			}
		});
	}

	ngAfterViewInit(): void {
		this.username.nativeElement.focus();
	}

	login(): void {
		this.authenticationError = false;
		this.accountNotActivatedError = false;
		this.loginService
			.login({
				username: this.loginForm.get('username')!.value,
				password: this.loginForm.get('password')!.value,
				rememberMe: this.loginForm.get('rememberMe')!.value
			})
			.subscribe({
				next: () => {
					if (!this.router.getCurrentNavigation()) {
						// There were no routing during login (eg from navigationToStoredUrl)
						this.router.navigate(['']);
					}
				},
				error: (response) => this.processError(response)
			});
	}

	private processError(response: HttpErrorResponse): void {
		// eslint-disable-next-line no-console
		console.log(response.error.detail);
		if (response.status === 401 && response.error.detail.includes(' was not activated')) {
			this.accountNotActivatedError = true;
		} else {
			this.authenticationError = true;
		}
	}
}
