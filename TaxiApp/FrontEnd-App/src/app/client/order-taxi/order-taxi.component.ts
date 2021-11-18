import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AclService, AppError, CarsService, ClientsService, ConfirmationUtils, DriversService, FormUtils, LoggedInUserModel, MessageDisplayer, ObjectUtils, OrganizationService, RidesService, StringUtils, StringValidators, TranslateUtils } from '@app/shared';
import { AuthManager } from '@app/shared/auth';
import { CarCategoryModel } from '@app/shared/model/cars';
import { ClientModel } from '@app/shared/model/clients/client.model';
import { DriverModel } from '@app/shared/model/drivers';
import { PaymentType, RideModel } from '@app/shared/model/rides/ride.model';
import { ListItemUtils } from '@app/shared/utils/list-item-utils';
import { SelectItem } from 'primeng/api';
import { Observable } from 'rxjs';
import 'rxjs/add/observable/interval';

@Component({
  selector: 'app-order-taxi',
  templateUrl: './order-taxi.component.html',
  styleUrls: ['./order-taxi.component.css']
})
export class OrderTaxiComponent implements OnInit {

	private clientService: ClientsService;
	private driversService: DriversService;
	private rideService: RidesService;
	private carsService: CarsService;
	private authManager: AuthManager;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;

	private loggedInUser: LoggedInUserModel;
	private loggedInClient: ClientModel;

	private formBuilder: FormBuilder;
	public form: FormGroup;

  	public categoryOptions: CarCategoryModel[];

	public currentOrderActive: boolean = false;
	public currentOrder: RideModel;

	public searchDriverText: string;
	private assignedDriver: DriverModel;

	public isEditMode: boolean;

  	constructor(clientsService: ClientsService, driversService: DriversService, ridesService: RidesService,
              	messageDisplayer: MessageDisplayer, formBuilder: FormBuilder, carsService: CarsService,
              	authManager: AuthManager, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils) {
		this.clientService = clientsService;
		this.driversService = driversService;
		this.rideService = ridesService;
		this.carsService = carsService;
		this.authManager = authManager;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;
  	}

  	ngOnInit() {
		this.prepareForm();
		this.init();
  	}

  	private async init(): Promise<void> {
		this.currentOrder = null;
		this.assignedDriver = null;
		this.isEditMode = false;
		this.searchDriverText = this.translateUtils.translateMessage("LOOKING_FOR_DRIVER");
    	await this.getCarCateogries();
		await this.getLoggedInClient();
		await this.getCurrentOrder();
		this.getDriverForDisplay();
		this.getIntervalCurrentOrder();
	}

  	private prepareForm(): void {
		this.form = this.formBuilder.group({
			startAdress: [null, [Validators.required, StringValidators.blank()]],
      		endAdress: [null, [Validators.required, StringValidators.blank()]],
			observations: [null, []],
      		carCategory: [null, []],
		});
  	}

	private getIntervalCurrentOrder(): void {
		setInterval(() => {
			this.getCurrentOrder();
		}, 10000);
	}

	private getCurrentOrder(): Promise<void> {
		return new Promise<void>((resolve,reject) => {
			this.rideService.getActiveRide(this.loggedInClient.id, {
				onSuccess: (ride: RideModel): void => {
					if (ObjectUtils.isNotNullOrUndefined(ride)) {
						this.currentOrder = ride;
						this.currentOrderActive = true;
						if (!this.isEditMode) {
							this.clearAndDisableFields();
						}
						if (ObjectUtils.isNotNullOrUndefined(ride.driverId)) {
							if (ObjectUtils.isNullOrUndefined(this.assignedDriver)) {
								this.getAssignedDriver(ride.driverId);
							} else {
								if (this.assignedDriver.id !== ride.driverId) {
									this.getAssignedDriver(ride.driverId);
								}
							}
						} else {
							this.assignedDriver = null;
							this.searchDriverText = this.translateUtils.translateMessage("LOOKING_FOR_DRIVER") + ".";
						}
					} else {
						this.currentOrderActive = false;
						this.form.enable();
					}
					resolve();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
					reject();
				}
			});
		});
	}

	private clearAndDisableFields(): void {
		this.form.reset();
		this.form.disable();
	}

	private getAssignedDriver(id: number): void {
		this.driversService.getDriverById(id, {
			onSuccess: (driver: DriverModel): void => {
				this.assignedDriver = driver;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

  	private getLoggedInClient(): Promise<void> {
		return new Promise<void>((resolve,reject) => {
			this.loggedInUser = this.authManager.getLoggedInUser();
			this.clientService.getClientByUserId(this.loggedInUser.id, {
				onSuccess: (client: ClientModel): void => {
					this.loggedInClient = client;
					resolve();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
					reject();
				}
			});
		});
	}

  	private getCarCateogries(): Promise<void> {
		this.categoryOptions = [];
		return new Promise<void>((resolve,reject) => {
			this.carsService.getAllCarCategories({
				onSuccess: (categories: CarCategoryModel[]): void => {
					ListItemUtils.sort(categories, "code");
					this.categoryOptions = categories;
					this.carCategoryFormControl.setValue(this.categoryOptions[0]);
					resolve();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
					reject();
				}
			});
		});
	}

  	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

  	public onOrderAction(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		let ride: RideModel = new RideModel();
		ride = this.getRideModel();
		this.rideService.saveRide(ride, {
			onSuccess: (id: Number): void => {
				this.messageDisplayer.displaySuccess("RIDE_REGISTERED_SUCCESSFULLY");
				this.getCurrentOrder();
				this.isEditMode = false;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getRideModel(): RideModel {
		let ride: RideModel = new RideModel();
		if (this.isEditMode) {
			ride.id = this.currentOrder.id;
		}
		ride.clientId = this.loggedInClient.id;
		ride.paymentType = PaymentType.CASH;
		ride.startAdress = this.startAdressFormControl.value;
		ride.endAdress = this.endAdressFormControl.value;
		ride.observations = this.observationsFormControl.value;
		ride.carCategoryId = this.carCategoryFormControl.value["id"];
		
		return ride;
	}

	public getDriverForDisplay(): void {
		setInterval(() => {
			if (ObjectUtils.isNotNullOrUndefined(this.currentOrder)) {
				if (ObjectUtils.isNullOrUndefined(this.currentOrder.driverId)) {
					this.getSearchDriverText();
				} else {
					if (ObjectUtils.isNotNullOrUndefined(this.assignedDriver)) {
						this.searchDriverText = this.assignedDriver.user.firstName + " " + this.assignedDriver.user.lastName;
					}
				}
			}
		}, 500);
	}

	public getCarCategoryForDisplay(carCategoryId: number): String {
		for (const category of this.categoryOptions) {
			if (category.id === carCategoryId) {
				return category.name;
			}
		}
		return null;
	}

	private getSearchDriverText(): void {
		if (this.searchDriverText.endsWith("...")) {
			this.searchDriverText = this.translateUtils.translateMessage("LOOKING_FOR_DRIVER") + ".";
		} else if (this.searchDriverText.endsWith("..")) {
			this.searchDriverText = this.searchDriverText + ".";
		} else if (this.searchDriverText.endsWith(".") || this.searchDriverText === this.translateUtils.translateMessage("LOOKING_FOR_DRIVER")) {
			this.searchDriverText = this.searchDriverText + ".";
		} else if (this.searchDriverText === this.translateUtils.translateMessage("LOOKING_FOR_DRIVER")) {
			this.searchDriverText = this.searchDriverText + ".";
		}
	}

  	public onCategoryChange(event: any): void {
		this.carCategoryFormControl.setValue(event.value);
	}

	public onEditCurrentOrder(): void {
		this.isEditMode = true;
		this.form.enable();
		this.startAdressFormControl.setValue(this.currentOrder.startAdress);
		this.endAdressFormControl.setValue(this.currentOrder.endAdress);
		this.observationsFormControl.setValue(this.currentOrder.observations);
		for (const category of this.categoryOptions) {
			if (category.id === this.currentOrder.carCategoryId) {
				this.carCategoryFormControl.setValue(category);
			}
		}
	}

	public onCancelCurrentOrder(): void {
		this.confirmationUtils.confirm("CONFIRM_CANCEL_ORDER", {
			approve: (): void => {
				this.currentOrder.canceled = true;
				this.rideService.saveRide(this.currentOrder, {
					onSuccess: (): void => {
						this.currentOrderActive = false;
						this.currentOrder = null
						this.form.reset();
						this.form.enable();
						this.messageDisplayer.displaySuccess("ORDER_CANCELED");
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

  	public get startAdressFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "startAdress");
	}

  	public get endAdressFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "endAdress");
	}

	public get observationsFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "observations");
	}

  	public get carCategoryFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "carCategory");
	}

}
