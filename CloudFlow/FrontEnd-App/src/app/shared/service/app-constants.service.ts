import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { ReplacementProfilesOutOfOfficeConstantsModel } from "../model/constants/replacement-profiles-out-of-office-constants.model";
import { AppError } from "../model/app-error";
import { ApiPathConstants } from "../constants/api-path.constants";
import { Injectable } from "@angular/core";
import { ApplicationInfoModel } from "@app/shared/model/application-info.model";

@Injectable()
export class AppConstantsService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getReplacementProfilesOutOfOfficeConstants(callback: AsyncCallback<ReplacementProfilesOutOfOfficeConstantsModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_REPLACEMENT_PROFILES_OUT_OF_OFFICE_CONSTANTS, null, ReplacementProfilesOutOfOfficeConstantsModel, callback);
	}

	public getApplicationInfo(callback: AsyncCallback<ApplicationInfoModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_APPLICATION_INFO, null, ApplicationInfoModel, callback);
	}
}