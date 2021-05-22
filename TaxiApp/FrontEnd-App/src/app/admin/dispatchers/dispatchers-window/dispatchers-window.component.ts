import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { MessageDisplayer, StringValidators, FormUtils, DateConstants, DateUtils, UiUtils, AppError, StringUtils, BaseWindow, OrganizationService, ObjectUtils } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { SelectItem, Dialog } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { UserModel } from "@app/shared/model/organization/user.model";

@Component({
  selector: 'app-dispatchers-window',
  templateUrl: './dispatchers-window.component.html',
  styleUrls: ['./dispatchers-window.component.css']
})
export class DispatchersWindowComponent extends BaseWindow implements OnInit {

  	@Input()
	public mode: "add" | "edit";

	@Input()
	public dispatcherId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

  	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public dateFormat: string;
 	public yearRange: string;

	public windowVisible: boolean;

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
    	this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.windowVisible = true;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
    	this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareForm();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			firstName: [null, [Validators.required, StringValidators.blank()]],
			lastName: [null, [Validators.required, StringValidators.blank()]],
			email: [null],
			password: [null, [Validators.required, StringValidators.blank()]],
			username: [null, [Validators.required, StringValidators.blank()]],
			mobile: [null, [Validators.required, StringValidators.blank()]]
		});
  	}

	public ngOnInit(): void {
		if (this.isAddMode()) {
			this.prepareForAdd();
		} else if (this.isEditMode()) {
			this.prepareForEdit();
		}
	}

	private isAddMode(): boolean {
		return this.mode === "add";
	}

	private isEditMode(): boolean {
		return this.mode === "edit";
	}

	private prepareForAdd(): void {
		this.lock();
		this.unlock();
	}

	private prepareForEdit(): void {
		this.lock();
		this.organizationService.getUserById(this.dispatcherId, {
			onSuccess: (user: UserModel): void => {
				this.prepareFormFromUserModel(user);
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	private prepareFormFromUserModel(user: UserModel): void {
		this.firstNameFormControl.setValue(user.firstName);
		this.lastNameFormControl.setValue(user.lastName);
		ObjectUtils.isNotNullOrUndefined(user.email) ? this.emailFormControl.setValue(user.email) : this.emailFormControl.setValue(null);
		this.passwordFormControl.setValue(null);
		this.usernameFormControl.setValue(user.username);
		this.mobileFormControl.setValue(user.mobile);
	}

	public onSaveAction(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		this.saveDispatcher();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private saveDispatcher(): void {
		let userModel = this.getUserModelFromForm();
		this.organizationService.saveUserAsDispatcher(userModel, {
			onSuccess: (userId: number) => {
				this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
				this.dataSaved.emit();
				this.closeWindow();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

  	private getUserModelFromForm(): UserModel {
		let user: UserModel = new UserModel();
		if (ObjectUtils.isNotNullOrUndefined(this.dispatcherId)) {
			user.id = this.dispatcherId;
		}
		user.firstName = this.firstNameFormControl.value;
		user.lastName = this.lastNameFormControl.value;
		user.password = this.passwordFormControl.value;
		user.username = this.usernameFormControl.value;
		user.email = this.emailFormControl.value;
		user.mobile = this.mobileFormControl.value;
		return user;
  	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public get firstNameFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "firstName");
	}

  	public get lastNameFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "lastName");
	}

  	public get emailFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "email");
	}

  	public get passwordFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "password");
	}

  	public get usernameFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "username");
	}

  	public get mobileFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "mobile");
	}

}
