import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AppError, FormUtils, RegisterRequestModel, MessageDisplayer, RouteConstants, StringUtils, StringValidators, TranslateUtils } from '@app/shared';
import { AuthManager } from '@app/shared/auth';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private authManager: AuthManager;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	private router: Router;

	public loading: boolean;
	public errorVisible: boolean = false;
	public errorCode: string;

	public homeRouterLink: string = RouteConstants.HOME;

	constructor(formBuilder: FormBuilder, router: Router, authManager: AuthManager, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
	
		this.formBuilder = formBuilder;
		this.router = router;
		this.authManager = authManager;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;

		this.formGroup = this.formBuilder.group([]);
	}

	ngOnInit() {
		this.prepareForm();
	}

	private prepareForm(): void {
    	this.formGroup.addControl("username", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.formGroup.addControl("password", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("firstName", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("lastName", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("email", new FormControl(null, [Validators.required, Validators.email]));
		this.formGroup.addControl("mobile", new FormControl(null, [Validators.required]));
  	}

	private lock() {
		this.loading = true;
	}

	private unlock() {
		this.loading = false;
	}

	public onRegisterAction(event: any): void {
		
		FormUtils.validateAllFormFields(this.formGroup);
		if (this.formGroup.invalid) {
			this.errorVisible = true;
			this.errorCode = "INVALID_FORM_FIELD";
			return;
		}

		this.errorVisible = false;

		let registerRequest: RegisterRequestModel = new RegisterRequestModel();
		registerRequest.username = this.usernameFormControl.value;
		registerRequest.password = this.passwordFormControl.value;
		registerRequest.firstName = this.firstNameFormControl.value;
		registerRequest.lastName = this.lastNameFormControl.value;
		registerRequest.email = this.emailFormControl.value;
		registerRequest.mobile = this.mobileFormControl.value;

		this.lock();
		this.authManager.register(registerRequest, {
			onSuccess: () => {
				let route: string = RouteConstants.AUTH_LOGIN;
				
				this.router.navigate([route])
					.then(() => {
						this.unlock();
						this.messageDisplayer.displaySuccess("REGISTER_SUCCESSFUL");
					})
					.catch(() => {
						this.messageDisplayer.displayError("Redirect failed. Contact your admin.", false);
					});	
			},
			onFailure: (error: AppError) => {
				this.unlock();
				if (StringUtils.isBlank(error.errorCode)) {
					this.errorCode = "UNRECOVERABLE_EXCEPTION";
				} else {
					this.errorCode = error.errorCode;
				}
				this.errorVisible = true;
			}
		});		
	}

	public get usernameFormControl(): FormControl {
		return <FormControl> this.formGroup.get("username");
	}

	public get passwordFormControl(): FormControl {
		return <FormControl> this.formGroup.get("password");
	}

	public get firstNameFormControl(): FormControl {
		return <FormControl> this.formGroup.get("firstName");
	}

	public get lastNameFormControl(): FormControl {
		return <FormControl> this.formGroup.get("lastName");
	}

	public get emailFormControl(): FormControl {
		return <FormControl> this.formGroup.get("email");
	}

	public get mobileFormControl(): FormControl {
		return <FormControl> this.formGroup.get("mobile");
	}

}
