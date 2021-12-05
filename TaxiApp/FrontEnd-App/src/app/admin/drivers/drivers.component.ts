import { Component } from "@angular/core";
import { DriversService, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils, DateUtils, OrganizationService, TranslateUtils } from "@app/shared";
import { DriverModel } from "@app/shared/model/drivers";
import { Column } from "primeng/primeng";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.css']
})
export class DriversComponent {

	private organizationService: OrganizationService;
  	private driversService: DriversService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private translateUtils: TranslateUtils;

	public drivers: DriverModel[];
	public selectedDriver: DriverModel;

	public driverWindowVisible: boolean;
	public driverWindowMode: "add" | "edit";
	public driverWindowDriverId: number;

	public deleteButtonDisabled: boolean;
	public editButtonDisabled: boolean;

	public scrollHeight: string;
	public columns: Column[];

	public constructor(organizationService: OrganizationService, driverService: DriversService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, translateUtils: TranslateUtils) {
		this.organizationService = organizationService;
		this.driversService = driverService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.translateUtils = translateUtils;
		this.init();
	}

	public init(): void {
		this.columns = [];
		this.initiateColumns();
		this.driverWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadDrivers();
	}

	private initiateColumns(): void {
		let firstName = new Column();
		firstName.header = this.translateUtils.translateLabel("DRIVER_FIRSTNAME");
		let lastName = new Column();
		lastName.header = this.translateUtils.translateLabel("DRIVER_LASTNAME");
		let licenceNum = new Column();
		licenceNum.header = this.translateUtils.translateLabel("DRIVER_LICENCE_NUMBER");
		let expiryDate = new Column();
		expiryDate.header = this.translateUtils.translateLabel("DRIVER_EXPIRY_DATE");
		let lastMedExam = new Column();
		lastMedExam.header = this.translateUtils.translateLabel("DRIVER_LAST_MED_EXAM");
		let mobile = new Column();
		mobile.header = this.translateUtils.translateLabel("DRIVER_MOBILE");
		this.columns.push(firstName);
		this.columns.push(lastName);
		this.columns.push(licenceNum);
		this.columns.push(expiryDate);
		this.columns.push(lastMedExam);
		this.columns.push(mobile);
	}

	public loadDrivers(): void {
		this.driversService.getAllDrivers({
			onSuccess: (drivers: DriverModel[]): void => {
				this.drivers = drivers;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onAddDriver(): void {
		this.driverWindowMode = "add";
		this.driverWindowVisible = true;
	}

	public onEditDriver(): void {
		this.driverWindowMode = "edit";
		this.driverWindowDriverId = this.selectedDriver.id;
		this.driverWindowVisible = true;
	}

	public onDeleteDriver(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_DRIVER", {
			approve: (): void => {
				this.driversService.deleteDriverById(this.selectedDriver.id, {
					onSuccess: (): void => {
						this.organizationService.deleteUserById(this.selectedDriver.user.id, {
							onSuccess: (): void => {
								this.messageDisplayer.displaySuccess("DELETED_SUCCESSFULLY");
								this.selectedDriver = null;
								this.changePerspective();
								this.loadDrivers();
							},
							onFailure: (appError: AppError): void => {
								this.messageDisplayer.displayAppError(appError);
							}
						});
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public getDateForDisplay(date: Date): String {
		return DateUtils.formatForDisplay(date);
	}

	public onDriverSelected(event: any): void {
		this.changePerspective();
	}

	public onDriverUnselected(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.deleteButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedDriver);
		this.editButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedDriver);
	}

	public onDriverWindowClosed(): void {
		this.driverWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.selectedDriver = null;
		this.changePerspective();
	}

	public onDriverWindowDataSaved(): void {
		this.selectedDriver = null;
		this.changePerspective();
		this.loadDrivers();
	}

}
