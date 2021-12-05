import { Component } from "@angular/core";
import { CarsService, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils, DateUtils, DriversService, TranslateUtils } from "@app/shared";
import { CarModel } from "@app/shared/model/cars";
import { DriverModel } from "@app/shared/model/drivers";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { Column } from "primeng/primeng";

@Component({
  selector: 'app-cars',
  templateUrl: './cars.component.html',
  styleUrls: ['./cars.component.css']
})
export class CarsComponent {

	private carsService: CarsService;
	private driversService: DriversService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private translateUtils: TranslateUtils;

	public cars: CarModel[];
	private drivers: DriverModel[];
	public selectedCar: CarModel;

	public carWindowVisible: boolean;
	public carWindowMode: "add" | "edit";
	public carWindowCarId: number;

	public deleteButtonDisabled: boolean;
	public editButtonDisabled: boolean;

	public scrollHeight: string;

	public columns: Column[];

	public constructor(carService: CarsService, driversService: DriversService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, translateUtils: TranslateUtils) {
		this.carsService = carService;
		this.driversService = driversService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.translateUtils = translateUtils;
		this.init();
	}

	public init(): void {
		this.columns = [];
		this.initiateColumns();
		this.carWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadCars();
	}

	private initiateColumns(): void {
		let carModel = new Column();
		carModel.header = this.translateUtils.translateLabel("CAR_MODEL");
		let carRegNumber = new Column();
		carRegNumber.header = this.translateUtils.translateLabel("CAR_REG_NUMBER");
		let lastTechControl = new Column();
		lastTechControl.header = this.translateUtils.translateLabel("CAR_LAST_TECH_CONTROL");
		let driver = new Column();
		driver.header = this.translateUtils.translateLabel("DRIVER");
		this.columns.push(carModel);
		this.columns.push(carRegNumber);
		this.columns.push(lastTechControl);
		this.columns.push(driver);
	}

	public loadCars(): void {
		this.drivers = [];
		this.carsService.getAllCars({
			onSuccess: async (cars: CarModel[]): Promise<void> => {
				await this.getDriversByCars(cars);
				this.cars = cars;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getDriversByCars(cars: CarModel[]): Promise<void> {
		return new Promise<void>((resolve,reject) => {
			for (const car of cars) {
				this.driversService.getDriverById(car.driverId, {
					onSuccess: (driver: DriverModel): void => {
						this.drivers.push(driver);
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
						reject();
					}
				});
			}
			resolve();
		});
	}

	public onAddCar(): void {
		this.carWindowMode = "add";
		this.carWindowVisible = true;
	}

	public onEditCar(): void {
		this.carWindowMode = "edit";
		this.carWindowCarId = this.selectedCar.id;
		this.carWindowVisible = true;
	}

	public onDeleteCar(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_CAR", {
			approve: (): void => {
				this.carsService.deleteCarById(this.selectedCar.id, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("DELETED_SUCCESSFULLY");
						this.selectedCar = null;
						this.changePerspective();
						this.loadCars();
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

	public getDriverForDisplay(driverId: number): String {
		for (const driver of this.drivers) {
			if (driver.id === driverId) {
				return driver.user.firstName + " " + driver.user.lastName;
			}
		}
	}

	public onCarSelected(event: any): void {
		this.changePerspective();
	}

	public onCarUnselected(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.deleteButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedCar);
		this.editButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedCar);
	}

	public onCarWindowClosed(): void {
		this.carWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.selectedCar = null;
		this.changePerspective();
	}

	public onCarWindowDataSaved(): void {
		this.selectedCar = null;
		this.changePerspective();
		this.loadCars();
	}

}
