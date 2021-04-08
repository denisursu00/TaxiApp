import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Injectable } from "@angular/core";
import { UserModel } from "../model/organization/user.model";

@Injectable()
export class OrganizationService {

    private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

    public saveUserAsDriver(user: UserModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.ORGANIZATION_SAVE_USER_AS_DRIVER, user, Number, callback);
    }

    public saveUserAsClient(user: UserModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.ORGANIZATION_SAVE_USER_AS_CLIENT, user, Number, callback);
    }

    public saveUserAsDispatcher(user: UserModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.ORGANIZATION_SAVE_USER_AS_DISPATCHER, user, Number, callback);
    }

    public getDriverById(id: Number, callback: AsyncCallback<UserModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.ORGANIZATION_GET_USER_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, UserModel, callback);
    }

}