import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppError, DateConstants, DateUtils, DriversService, FormUtils, LoggedInUserModel, MessageDisplayer, OrganizationService, StringValidators } from '@app/shared';
import { AuthManager } from '@app/shared/auth';
import { DriverModel } from '@app/shared/model/drivers';
import { UserModel } from '@app/shared/model/organization/user.model';

@Component({
  selector: 'app-personal-page-driver',
  templateUrl: './personal-page-driver.component.html',
  styleUrls: ['./personal-page-driver.component.css']
})
export class PersonalPageDriverComponent implements OnInit {

  private messageDisplayer: MessageDisplayer;
  private authManager: AuthManager;
  private organizationService: OrganizationService;
  private driversService: DriversService;

  private loggedInUserModel: LoggedInUserModel;
  public driver: DriverModel[];

  private mode: string;

  private formBuilder: FormBuilder;
	public form: FormGroup;

  public dateFormat: string;
 	public yearRange: string;


  constructor(messageDisplayer: MessageDisplayer, authManager: AuthManager, organizationService: OrganizationService, driversService: DriversService, formBuilder: FormBuilder) {
    this.messageDisplayer = messageDisplayer;
    this.authManager = authManager;
    this.organizationService = organizationService;
    this.driversService = driversService;
    this.formBuilder = formBuilder;
    this.prepareForm();
    this.driver = [];
    this.setModeView();
    this.init();
  }

  ngOnInit() {
    this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
    this.yearRange = DateUtils.getDefaultYearRange();
  }

  private async init(): Promise<void> {
    this.loggedInUserModel = this.authManager.getLoggedInUser();
    await this.getLoggedInDriver();
  }

  private prepareForm(): void {
    this.form = this.formBuilder.group({
			firstName: [null, [Validators.required, StringValidators.blank()]],
			lastName: [null, [Validators.required, StringValidators.blank()]],
			birthDate: [null, [Validators.required]],
			licenceNumber: [null, [Validators.required, StringValidators.blank()]],
			expiryDate: [null, [Validators.required]],
			lastMedExam: [null, [Validators.required]]
		});
  }

  private getLoggedInDriver(): Promise<void> {
    return new Promise<void>((resolve,reject) => {
      this.organizationService.getUserById(this.loggedInUserModel.id, {
        onSuccess: (user: UserModel) => {
          this.driversService.getDriverByUserId(user.id, {
            onSuccess: (driver: DriverModel) => {
              this.driver.push(driver);
              resolve();
            },
            onFailure: (appError: AppError) => {
              this.messageDisplayer.displayAppError(appError);
              reject();
            }
          });
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
    this.firstNameFormControl.setValue(this.driver[0].user.firstName);
    this.lastNameFormControl.setValue(this.driver[0].user.lastName);
    this.birthDateFormControl.setValue(this.driver[0].birthDate);
    this.licenceNumberFormControl.setValue(this.driver[0].licenceNumber);
    this.expiryDateFormControl.setValue(this.driver[0].expiryDate);
    this.lastMedExamFormControl.setValue(this.driver[0].lastMedExam);
  }

  public onSaveAction(event: any): void {
    if (!this.isFormValid()) {
      this.messageDisplayer.displayWarn("REQUIRED_FIELDS_NOT_COMPLETED");
      return;
    }
    this.setModeView();
    this.driver[0].user.firstName = this.firstNameFormControl.value;
    this.driver[0].user.lastName = this.lastNameFormControl.value;
    this.driver[0].birthDate = this.birthDateFormControl.value;
    this.driver[0].licenceNumber = this.licenceNumberFormControl.value;
    this.driver[0].expiryDate = this.expiryDateFormControl.value;
    this.driver[0].lastMedExam = this.lastMedExamFormControl.value;
    this.organizationService.saveUserAsDriver(this.driver[0].user, {
      onSuccess: (userId: Number) => {
        this.driversService.saveDriver(this.driver[0], {
          onSuccess: (driverId: Number) => {
            this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
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
