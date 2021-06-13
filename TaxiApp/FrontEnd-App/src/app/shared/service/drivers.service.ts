import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Injectable } from "@angular/core";
import { DriverModel } from "../model/drivers/driver.model";

@Injectable()
export class DriversService {

    private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

    public saveDriver(driverModel: DriverModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.DRIVER_SAVE, driverModel, Number, callback);
    }

    public getAllDrivers(callback: AsyncCallback<DriverModel[], AppError>): void {
        this.apiCaller.call(ApiPathConstants.DRIVER_GET_ALL, null, DriverModel, callback);
    }

    public getDriverById(id: Number, callback: AsyncCallback<DriverModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DRIVER_GET_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, DriverModel, callback);
    }

    public getDriverByUserId(id: Number, callback: AsyncCallback<DriverModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DRIVER_GET_BY_USER_ID, id.toString());
        this.apiCaller.call(relativePath, null, DriverModel, callback);
    }

    public deleteDriverById(id: Number, callback: AsyncCallback<null, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DRIVER_DELETE_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, null, callback);
    }

}