import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { StringValidators, FormUtils,  MessageDisplayer, AppError, TranslateUtils, StringUtils } from "@app/shared";
import { Message } from "primeng/components/common/message";
import { AuthManager, AUTH_ACCESS } from "@app/shared/auth";
import { LoginRequestModel } from "@app/shared/model/auth";
import { RouteConstants } from "@app/shared/constants/route.constants";
import { ApplicationInfoManager } from "@app/shared/application-info-manager";
import { environment } from "./../../../environments/environment";

@Component({
	selector: "app-login",
	templateUrl: "./login.component.html",
	styleUrls: ["./login.component.css"]
})
export class LoginComponent implements OnInit {

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private authManager: AuthManager;
	private applicationInfoManager: ApplicationInfoManager;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	private router: Router;

	public loading: boolean;
	public errorVisible: boolean = false;
	public errorCode: string;

	public homeRouterLink: string = RouteConstants.HOME;

	constructor(formBuilder: FormBuilder, router: Router, 
			authManager: AuthManager, messageDisplayer: MessageDisplayer,
			translateUtils: TranslateUtils, applicationInfoManager: ApplicationInfoManager) {
				
		this.formBuilder = formBuilder;
		this.router = router;
		this.authManager = authManager;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.applicationInfoManager = applicationInfoManager;

		this.formGroup = this.formBuilder.group([]);
	}

	ngOnInit() {
		this.formGroup.addControl("username", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.formGroup.addControl("password", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("rememberMe", new FormControl(true));
	}

	private lock() {
		this.loading = true;
	}

	private unlock() {
		this.loading = false;
	}

	public onLoginAction(event: any): void {
		
		FormUtils.validateAllFormFields(this.formGroup);
		if (this.formGroup.invalid) {
			return;
		}

		let loginRequest: LoginRequestModel = new LoginRequestModel();
		loginRequest.username = this.usernameFormControl.value;
		loginRequest.password = this.passwordFormControl.value;
		loginRequest.rememberMe = this.rememberFormControl.value;

		this.lock();
		this.authManager.login(loginRequest, {
			onSuccess: () => {
				let route: string = RouteConstants.HOME;
				
				if (this.authManager.hasOnlyOnePermission(AUTH_ACCESS.PREZENTA.COMPLETARE.permissions.toString())) {
					route = RouteConstants.PREZENTA_ONLINE;
				}
				
				this.router.navigate([route])
					.then(() => {
						this.unlock();
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

	public get rememberFormControl(): FormControl {
		return <FormControl> this.formGroup.get("rememberMe");
	}

	public get appVersion(): string {
		return environment.version;
	}

	public get isProduction(): boolean {
		return this.applicationInfoManager.isProduction();
	}

	public get appEnvironmentName(): string {
		return this.applicationInfoManager.getEnvironmentName();
	}
}
