import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Injectable } from "@angular/core";
import { RideModel } from "../model/rides/ride.model";

@Injectable()
export class RidesService {

    private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

    public saveRide(rideModel: RideModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.RIDE_SAVE, rideModel, Number, callback);
    }

    public getAllRides(callback: AsyncCallback<RideModel[], AppError>): void {
        this.apiCaller.call(ApiPathConstants.RIDE_GET_ALL, null, RideModel, callback);
    }

    public getRidesForDriver(driverId: number, callback: AsyncCallback<RideModel[], AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.RIDE_GET_RIDES_FOR_DRIVER, driverId.toString());
        this.apiCaller.call(relativePath, null, RideModel, callback);
    }

    public getRideById(id: Number, callback: AsyncCallback<RideModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.RIDE_GET_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, RideModel, callback);
    }

    public getActiveRide(clientId: number, callback: AsyncCallback<RideModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.RIDE_GET_ACTIVE_RIDE, clientId.toString());
        this.apiCaller.call(relativePath, null, RideModel, callback);
    }

}