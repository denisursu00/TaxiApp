import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Injectable } from "@angular/core";
import { CarModel } from "../model/cars/car.model";

@Injectable()
export class CarsService {

    private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

    public saveCar(carModel: CarModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.CAR_SAVE, carModel, Number, callback);
    }

    public getAllCars(callback: AsyncCallback<CarModel[], AppError>): void {
        this.apiCaller.call(ApiPathConstants.CAR_GET_ALL, null, CarModel, callback);
    }

    public getCarById(id: Number, callback: AsyncCallback<CarModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.CAR_GET_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, CarModel, callback);
    }

    public deleteCarById(id: Number, callback: AsyncCallback<null, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.CAR_DELETE_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, null, callback);
    }

}