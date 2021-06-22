import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { MessageDisplayer, StringValidators, FormUtils, DateConstants, DateUtils, UiUtils, AppError, StringUtils, BaseWindow, OrganizationService, ObjectUtils, AclService, SecurityManagerModel, ClientsService, RidesService, CarsService } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { SelectItem, Dialog } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { UserModel } from "@app/shared/model/organization/user.model";
import { PaymentType, RideModel } from "@app/shared/model/rides/ride.model";
import { ClientModel } from "@app/shared/model/clients/client.model";
import { CarCategoryModel } from "@app/shared/model/cars";

@Component({
  selector: 'app-ride-window',
  templateUrl: './ride-window.component.html',
  styleUrls: ['./ride-window.component.css']
})
export class RideWindowComponent extends BaseWindow implements OnInit {

  	@Input()
	public mode: "add" | "edit";

	@Input()
	public rideId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

  	private organizationService: OrganizationService;
	private clientService: ClientsService;
	private rideService: RidesService;
	private aclService: AclService;
	private carsService: CarsService;
	private messageDisplayer: MessageDisplayer;

	private dispatcherInfo: SecurityManagerModel;
	public clients: UserModel[];
	public suggestions: UserModel[];

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public dateFormat: string;
 	public yearRange: string;

	public windowVisible: boolean;

	public categoryOptions: CarCategoryModel[];

	public constructor(organizationService: OrganizationService, clientsService: ClientsService, ridesService: RidesService,
						aclService: AclService, carsService: CarsService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
    	this.organizationService = organizationService;
		this.clientService = clientsService;
		this.rideService = ridesService;
		this.aclService = aclService;
		this.carsService = carsService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.init();
	}

	private async init(): Promise<void> {
		this.lock();
		this.windowVisible = true;
		this.clients = [];
		this.suggestions = [];
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
    	this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareForm();
		await this.getCarCateogries();
		await this.getClients();
		await this.getDispatcherInfo();
		this.unlock();
	}

	public ngOnInit(): void {
		if (this.isAddMode()) {
			this.prepareForAdd();
		} else if (this.isEditMode()) {
			this.prepareForEdit();
		}
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			clientPhone: [null, [Validators.required, StringValidators.blank()]],
			firstName: [null, [Validators.required, StringValidators.blank()]],
			lastName: [null, [Validators.required, StringValidators.blank()]],
			startAdress: [null, [Validators.required, StringValidators.blank()]],
			observations: [null, []],
			carCategory: [null, []],
		});
  	}

	private getClients(): Promise<void> {
		return new Promise<void>((resolve,reject) => {
			this.organizationService.getClients({
				onSuccess: (users: UserModel[]): void => {
					this.clients = users;
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

	private getDispatcherInfo(): Promise<void> {
		return new Promise<void>((resolve,reject) => {
			this.aclService.getSecurityManager({
				onSuccess: (securityManager: SecurityManagerModel): void => {
					this.dispatcherInfo = securityManager;
					resolve();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
					reject();
				}
			});
		});
	}

	private onComplete(query: string): UserModel[] {
		let sugges: UserModel[] = [];
		this.clients.forEach(client => {
			if (client.mobile.indexOf(query) === 0) {
				sugges.push(client);
			}
		});
		return sugges;
	}

	public completeClientPhone(event:any): void {
		this.suggestions = this.onComplete(event.query);
	}

	public onSelectClient(event: UserModel): void {
		this.lastNameFormControl.setValue(event.lastName);
		this.firstNameFormControl.setValue(event.firstName);
	}

	public onCategoryChange(event: any): void {
		this.carCategoryFormControl.setValue(event.value);
	}

	private isAddMode(): boolean {
		return this.mode === "add";
	}

	private isEditMode(): boolean {
		return this.mode === "edit";
	}

	private prepareForAdd(): void {
		this.lock();
		this.rideId = null;
		this.unlock();
	}

	private prepareForEdit(): void {
		this.lock();
		this.rideService.getRideById(this.rideId, {
			onSuccess: (ride: RideModel): void => {
				this.clientService.getClientById(ride.clientId, {
					onSuccess: (client: ClientModel) => {
						this.organizationService.getUserById(client.userId, {
							onSuccess: (user: UserModel) => {
								this.clientPhoneFormControl.setValue(user);
								this.firstNameFormControl.setValue(user.firstName);
								this.lastNameFormControl.setValue(user.lastName);
								this.startAdressFormControl.setValue(ride.startAdress);
								this.observationsFormControl.setValue(ride.observations);
								for (const category of this.categoryOptions) {
									if (category.id === ride.carCategoryId) {
										this.carCategoryFormControl.setValue(category);
									}
								}
								this.unlock();
							},
							onFailure: (appError: AppError) => {
								this.messageDisplayer.displayAppError(appError);
								this.unlock();
							}
						});
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
						this.unlock();
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	public onSaveAction(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		this.saveRide();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private saveRide(): void {
		this.lock();
		if (this.clientPhoneFormControl.value instanceof UserModel) {
			let clientUserModel: UserModel = this.clientPhoneFormControl.value;
			this.clientService.getClientByUserId(clientUserModel.id, {
				onSuccess: (client: ClientModel) => {
					let ride = this.getRideModel(client.id);
					this.rideService.saveRide(ride, {
						onSuccess: (rideId: number) => {
							this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
							this.dataSaved.emit();
							this.unlock();
							this.closeWindow();
						},
						onFailure: (appError: AppError) => {
							this.messageDisplayer.displayAppError(appError);
							this.unlock();
						}
					});
				},
				onFailure: (appError: AppError) => {
					this.messageDisplayer.displayAppError(appError);
					this.unlock();
				}
			});
		} else if (typeof this.clientPhoneFormControl.value === "string") {
			let userModel = this.getUserModelFromForm();
			this.organizationService.saveUserAsClient(userModel, {
				onSuccess: (userId: number) => {
					let clientModel: ClientModel = new ClientModel();
					clientModel.userId = userId;
					this.clientService.saveClient(clientModel, {
						onSuccess: (clientId: number) => {
							let ride = this.getRideModel(clientId);
							this.rideService.saveRide(ride, {
								onSuccess: (rideId: number) => {
									this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
									this.dataSaved.emit();
									this.unlock();
									this.closeWindow();
								},
								onFailure: (appError: AppError) => {
									this.messageDisplayer.displayAppError(appError);
									this.unlock();
								}
							});
						},
						onFailure: (appError: AppError) => {
							this.messageDisplayer.displayAppError(appError);
							this.unlock();
						}
					});
				},
				onFailure: (appError: AppError) => {
					this.messageDisplayer.displayAppError(appError);
					this.unlock();
				}
			});
		} else {
			this.messageDisplayer.displayError("Unknown data type!", false);
			this.unlock();
		}
	}

  	private getUserModelFromForm(): UserModel {
		let user: UserModel = new UserModel();
		user.mobile = this.clientPhoneFormControl.value;
		user.firstName = this.firstNameFormControl.value;
		user.lastName = this.lastNameFormControl.value;
		return user;
  	}

	private getRideModel(clientId: number): RideModel {
		let ride: RideModel = new RideModel();
		if (ObjectUtils.isNotNullOrUndefined(this.rideId)) {
			ride.id = this.rideId;
		}
		ride.startAdress = this.startAdressFormControl.value;
		ride.clientId = clientId;
		ride.dispatcherId = Number(this.dispatcherInfo.userIdAsString).valueOf();
		ride.paymentType = PaymentType.CASH;
		ride.observations = this.observationsFormControl.value;
		ride.carCategoryId = this.carCategoryFormControl.value["id"];
		return ride;
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

	public get clientPhoneFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "clientPhone");
	}

	public get firstNameFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "firstName");
	}

  	public get lastNameFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "lastName");
	}

	public get startAdressFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "startAdress");
	}

	public get observationsFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "observations");
	}

	public get carCategoryFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "carCategory");
	}

}
