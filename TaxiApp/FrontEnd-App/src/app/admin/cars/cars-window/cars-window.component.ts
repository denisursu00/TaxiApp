import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { MessageDisplayer, StringValidators, FormUtils, DateConstants, DateUtils, UiUtils, AppError, StringUtils, BaseWindow, CarsService, OrganizationService, ObjectUtils, ConfirmationUtils, DriversService } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { SelectItem, Dialog } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { CarModel } from "@app/shared/model/cars";
import { UserModel } from "@app/shared/model/organization/user.model";
import { DriverModel } from "@app/shared/model/drivers";

@Component({
  selector: 'app-cars-window',
  templateUrl: './cars-window.component.html',
  styleUrls: ['./cars-window.component.css']
})
export class CarsWindowComponent extends BaseWindow implements OnInit {

  	@Input()
	public mode: "add" | "edit";

	@Input()
	public carId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

	private carsService: CarsService;
	private driversService: DriversService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public dateFormat: string;
 	public yearRange: string;

	public windowVisible: boolean;

	public driverOptions: SelectItem[];
	public selectedDriver: DriverModel;

	public constructor(carsService: CarsService, driversService: DriversService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.carsService = carsService;
		this.driversService = driversService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.driverOptions = [];
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
			carModel: [null, [Validators.required, StringValidators.blank()]],
			carRegNumber: [null, [Validators.required, StringValidators.blank()]],
			lastTechControl: [null, [Validators.required]],
			driver: [null, [Validators.required]]
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

	private async prepareForAdd(): Promise<void> {
		this.lock();
		await this.getDrivers();
		this.unlock();
	}

	private getDrivers(): Promise<void> {
		this.driverOptions = [];
		this.selectedDriver = null;
		return new Promise<void>((resolve,reject) => {
			this.driversService.getAllDrivers({
				onSuccess: (drivers: DriverModel[]): void => {
					for (const driver of drivers) {
						this.driverOptions.push({label: driver.user.firstName+" "+driver.user.lastName, value: driver.id});
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

	private async prepareForEdit(): Promise<void> {
		this.lock();
		await this.getDrivers();
		this.carsService.getCarById(this.carId, {
			onSuccess: (carModel: CarModel): void => {
				this.prepareFormFromCarModel(carModel);
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	private prepareFormFromCarModel(carModel: CarModel): void {
		this.carModelFormControl.setValue(carModel.model);
		this.carRegNumberFormControl.setValue(carModel.registrationNumber);
		this.lastTechControlFormControl.setValue(carModel.lastTechControl);
		this.driverFormControl.setValue(carModel.driverId);
	}

	public onSaveAction(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		this.saveCar();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private saveCar(): void {
		let carModel = this.getCarModelFromForm();
		this.carsService.saveCar(carModel, {
			onSuccess: (carId: number) => {
				this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
				this.dataSaved.emit();
				this.closeWindow();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getCarModelFromForm(): CarModel {
		let car: CarModel = new CarModel();
		if (this.isEditMode()) {
			car.id = this.carId;
		}
		car.model = this.carModelFormControl.value;
		car.registrationNumber = this.carRegNumberFormControl.value;
		car.lastTechControl = this.lastTechControlFormControl.value;
		car.driverId = this.driverFormControl.value;
		return car;
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

	public onDriverChange(event: any): void {
		this.driverFormControl.setValue(event.value);
	}

	public get carModelFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "carModel");
	}

  	public get carRegNumberFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "carRegNumber");
	}

  	public get lastTechControlFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "lastTechControl");
	}

  	public get driverFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "driver");
	}

}
