import { Injectable } from "@angular/core";
import { ApiCaller } from "./../api-caller";
import { AppError } from "./../model";
import { ApiPathConstants } from "./../constants";
import { AsyncCallback } from "./../async-callback";
import { SecurityManagerModel } from "../model/security-manager.model";

@Injectable()
export class AclService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getSecurityManager(callback: AsyncCallback<SecurityManagerModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_SECURITY_MANAGER, null, SecurityManagerModel, callback);
	}
}