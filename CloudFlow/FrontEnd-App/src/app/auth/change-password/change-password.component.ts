import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { StringValidators, FormUtils,  MessageDisplayer, AppError, TranslateUtils } from "@app/shared";
import { Message } from "primeng/components/common/message";
import { AuthManager } from "@app/shared/auth";
import { LoginRequestModel } from "@app/shared/model/auth";
import { RouteConstants } from "@app/shared/constants/route.constants";
import { AuthService } from "@app/shared/service/auth.service";
import { PasswordChangeModel } from "@app/shared/model/organization/password-change.model";
import { ApplicationInfoManager } from "@app/shared/application-info-manager";
import { environment } from "./../../../environments/environment";

@Component({
	selector: "app-change-password",
	templateUrl: "./change-password.component.html",
	styleUrls: ["./change-password.component.css"]
})
export class ChangePasswordComponent implements OnInit {

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private authService: AuthService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private applicationInfoManager: ApplicationInfoManager;

	private router: Router;

	public loading: boolean;
	public errorVisible: boolean = false;
	public errorCode: string;
	
	public homeRouterLink: string = RouteConstants.HOME;

	constructor(formBuilder: FormBuilder, router: Router, 
		authService: AuthService, messageDisplayer: MessageDisplayer,
			translateUtils: TranslateUtils, applicationInfoManager: ApplicationInfoManager) {
				
		this.formBuilder = formBuilder;
		this.router = router;
		this.authService = authService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.applicationInfoManager = applicationInfoManager;

		this.formGroup = this.formBuilder.group([]);
	}

	ngOnInit() {
		this.formGroup.addControl("currentPassword", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("newPassword", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("confirmNewPassword", new FormControl(null, [Validators.required]));
	}

	private lock() {
		this.loading = true;
	}

	private unlock() {
		this.loading = false;
	}

	public onChangePasswordAction(event: any): void {
		
		FormUtils.validateAllFormFields(this.formGroup);
		if (this.formGroup.invalid) {
			return;
		}
		if (this.newPasswordFormControl.value !== this.confirmNewPasswordFormControl.value) {
			this.errorCode =  "PASSWORD_CONFIRMATION_DOESNT_MATCH";
			this.errorVisible = true;
			return;
		}

		let passwordChangeModel: PasswordChangeModel = new PasswordChangeModel();
		passwordChangeModel.currentPassword = this.currentPasswordFormControl.value;
		passwordChangeModel.newPassword = this.newPasswordFormControl.value;

		this.lock();
		this.authService.changePassword(passwordChangeModel, {
			onSuccess: () => {
				this.router.navigate([RouteConstants.HOME])
					.then(() => {
						this.unlock();
						this.messageDisplayer.displaySuccess("PASSWORD_CHANGED", true);
					})
					.catch(() => {
						this.messageDisplayer.displayError("Redirect failed. Contact your admin.", false);
					});				
			},
			onFailure: (error: AppError) => {
				this.unlock();
				this.errorCode = error.errorCode;
				this.errorVisible = true;
			}
		});		
	}

	public get currentPasswordFormControl(): FormControl {
		return <FormControl> this.formGroup.get("currentPassword");
	}

	public get newPasswordFormControl(): FormControl {
		return <FormControl> this.formGroup.get("newPassword");
	}

	public get confirmNewPasswordFormControl(): FormControl {
		return <FormControl> this.formGroup.get("confirmNewPassword");
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
