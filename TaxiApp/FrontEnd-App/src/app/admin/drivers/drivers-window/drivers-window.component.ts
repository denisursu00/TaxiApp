import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { MessageDisplayer, StringValidators, FormUtils, DateConstants, DateUtils, UiUtils, AppError, StringUtils, BaseWindow, DriversService, OrganizationService, ObjectUtils } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { SelectItem, Dialog } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { DriverModel } from "@app/shared/model/drivers";
import { UserModel } from "@app/shared/model/organization/user.model";

@Component({
  selector: 'app-drivers-window',
  templateUrl: './drivers-window.component.html',
  styleUrls: ['./drivers-window.component.css']
})
export class DriversWindowComponent extends BaseWindow implements OnInit {

  	@Input()
	public mode: "add" | "edit";

	@Input()
	public driverId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

	private driversService: DriversService;
  	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public dateFormat: string;
 	public yearRange: string;

	public windowVisible: boolean;

	public user: UserModel;

	public constructor(driversService: DriversService, organizationService: OrganizationService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.driversService = driversService;
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
			password: [null],
			username: [null],
			mobile: [null, [Validators.required, StringValidators.blank()]],
			birthDate: [null, [Validators.required]],
			licenceNumber: [null, [Validators.required, StringValidators.blank()]],
			expiryDate: [null, [Validators.required]],
			lastMedExam: [null, [Validators.required]]
		});
  }

	public ngOnInit(): void {
		this.user = null;
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
		this.driversService.getDriverById(this.driverId, {
			onSuccess: (driverModel: DriverModel): void => {
				this.prepareFormFromDriverModel(driverModel);
				this.user = driverModel.user;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	private prepareFormFromDriverModel(driverModel: DriverModel): void {
		this.firstNameFormControl.setValue(driverModel.user.firstName);
		this.lastNameFormControl.setValue(driverModel.user.lastName);
		ObjectUtils.isNotNullOrUndefined(driverModel.user.email) ? this.emailFormControl.setValue(driverModel.user.email) : this.emailFormControl.setValue(null);
		this.passwordFormControl.setValue(null);
		ObjectUtils.isNotNullOrUndefined(driverModel.user.username) ? this.usernameFormControl.setValue(driverModel.user.username) : this.usernameFormControl.setValue(null);
		this.mobileFormControl.setValue(driverModel.user.mobile);
		this.birthDateFormControl.setValue(driverModel.birthDate);
		this.licenceNumberFormControl.setValue(driverModel.licenceNumber);
		this.expiryDateFormControl.setValue(driverModel.expiryDate);
		this.lastMedExamFormControl.setValue(driverModel.lastMedExam);
	}

	public onSaveAction(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		this.saveDriver();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private saveDriver(): void {
		let userModel = this.getUserModelFromForm();
		let driverModel = this.getDriverModelFromForm();
		driverModel.user = userModel;
		this.organizationService.saveUserAsDriver(userModel, {
			onSuccess: (userId: number) => {
				userModel.id = userId;
				driverModel.user = userModel;
				this.driversService.saveDriver(driverModel, {
					onSuccess: (driverId: number) => {
						this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
						this.dataSaved.emit();
						this.closeWindow();
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

  	private getUserModelFromForm(): UserModel {
		let user: UserModel = new UserModel();
		if (this.isEditMode()) {
			user.id = this.user.id;
		}
		user.firstName = this.firstNameFormControl.value;
		user.lastName = this.lastNameFormControl.value;
		user.password = this.passwordFormControl.value;
		user.username = this.usernameFormControl.value;
		user.email = this.emailFormControl.value;
		user.mobile = this.mobileFormControl.value;
		return user;
  	}

	private getDriverModelFromForm(): DriverModel {
		let driver: DriverModel = new DriverModel();
		if (this.isEditMode()) {
			driver.id = this.driverId;
		}
		driver.birthDate = this.birthDateFormControl.value;
		driver.licenceNumber = this.licenceNumberFormControl.value;
		driver.expiryDate = this.expiryDateFormControl.value;
		driver.available = false;
		driver.lastMedExam = this.lastMedExamFormControl.value;
		driver.user = null;
		return driver;
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

  	public get birthDateFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "birthDate");
	}

  	public get licenceNumberFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "licenceNumber");
	}

  	public get expiryDateFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "expiryDate");
	}

  	public get lastMedExamFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "lastMedExam");
	}

}
