import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError, ParameterModel } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Injectable } from "@angular/core";

@Injectable()
export class ParametersService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public saveParameter(parameterModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_PARAMETER, parameterModel, null, callback);
	}

	public getAllParameters(callback: AsyncCallback<ParameterModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_PARAMETERS, null, ParameterModel, callback);
	}

	public getParameterById(parameterId: number, callback: AsyncCallback<ParameterModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_PARAMETER_BY_ID, parameterId.toString());
		this.apiCaller.call(relativePath, null, ParameterModel, callback);
	}

	public deleteParameterById(parameterId: number, callback: AsyncCallback<void, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_PARAMETER_BY_ID, parameterId.toString());
		this.apiCaller.call(relativePath, null, null, callback);
	}
}