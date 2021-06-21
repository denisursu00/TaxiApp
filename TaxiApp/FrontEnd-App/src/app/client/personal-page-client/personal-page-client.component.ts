import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppError, DateConstants, DateUtils, DriversService, FormUtils, LoggedInUserModel, MessageDisplayer, OrganizationService, StringValidators } from '@app/shared';
import { AuthManager } from '@app/shared/auth';
import { DriverModel } from '@app/shared/model/drivers';
import { UserModel } from '@app/shared/model/organization/user.model';

@Component({
  selector: 'app-personal-page-client',
  templateUrl: './personal-page-client.component.html',
  styleUrls: ['./personal-page-client.component.css']
})
export class PersonalPageClientComponent implements OnInit {

  private messageDisplayer: MessageDisplayer;
  private authManager: AuthManager;
  private organizationService: OrganizationService;

  private loggedInUserModel: LoggedInUserModel;
  public user: UserModel[];

  private mode: string;

  private formBuilder: FormBuilder;
	public form: FormGroup;

  public dateFormat: string;
 	public yearRange: string;


  constructor(messageDisplayer: MessageDisplayer, authManager: AuthManager, organizationService: OrganizationService, driversService: DriversService, formBuilder: FormBuilder) {
    this.messageDisplayer = messageDisplayer;
    this.authManager = authManager;
    this.organizationService = organizationService;
    this.formBuilder = formBuilder;
    this.prepareForm();
    this.user = [];
    this.setModeView();
    this.init();
  }

  ngOnInit() {
    this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
    this.yearRange = DateUtils.getDefaultYearRange();
    this.clearIntervals();
  }

  private async init(): Promise<void> {
    this.loggedInUserModel = this.authManager.getLoggedInUser();
    await this.getLoggedInUser();
  }

  private clearIntervals(): void {
    for (let i = 0; i < 9999; i++) {
      clearInterval(i);
    }
  }

  private prepareForm(): void {
    this.form = this.formBuilder.group({
			firstName: [null, [Validators.required, StringValidators.blank()]],
			lastName: [null, [Validators.required, StringValidators.blank()]],
			email: [null, [Validators.required, Validators.email]],
			mobile: [null, [Validators.required, StringValidators.blank()]]
		});
  }

  private getLoggedInUser(): Promise<void> {
    return new Promise<void>((resolve,reject) => {
      this.organizationService.getUserById(this.loggedInUserModel.id, {
        onSuccess: (user: UserModel) => {
          this.user.push(user);
          resolve();
        },
        onFailure: (appError: AppError) => {
          this.messageDisplayer.displayAppError(appError);
          reject();
        }
      });
    });
  }

  public onEditAction(event: any): void {
    this.setModeEdit();
    this.firstNameFormControl.setValue(this.user[0].firstName);
    this.lastNameFormControl.setValue(this.user[0].lastName);
    this.emailFormControl.setValue(this.user[0].email);
    this.mobileFormControl.setValue(this.user[0].mobile);
  }

  public onSaveAction(event: any): void {
    if (!this.isFormValid()) {
      this.messageDisplayer.displayWarn("REQUIRED_FIELDS_NOT_COMPLETED_OR_WRONG");
      return;
    }
    this.setModeView();
    this.user[0].firstName = this.firstNameFormControl.value;
    this.user[0].lastName = this.lastNameFormControl.value;
    this.user[0].email = this.emailFormControl.value;
    this.user[0].mobile = this.mobileFormControl.value;
    this.organizationService.saveUserAsClient(this.user[0], {
      onSuccess: (userId: Number) => {
        this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
      },
      onFailure: (appError: AppError) => {
        this.messageDisplayer.displayAppError(appError);
      }
    });
  }

  public isFormValid(): boolean {
    FormUtils.validateAllFormFields(this.form);
    return this.form.valid;
  }

  public onCancelAction(event: any): void {
    this.setModeView();
  }

  public get isModeEdit(): boolean {
    return this.mode === "edit";
  }

  public get isModeView(): boolean {
    return this.mode === "view";
  }

  private setModeView(): void {
    this.mode = "view";
  }

  private setModeEdit(): void {
    this.mode = "edit";
  }

  public getDateForDisplay(date: Date): String {
		return DateUtils.formatForDisplay(date);
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

  public get mobileFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "mobile");
	}

}
