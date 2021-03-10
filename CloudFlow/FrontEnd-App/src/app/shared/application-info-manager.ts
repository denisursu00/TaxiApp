import { Injectable } from "@angular/core";
import { Http, Response, Headers} from "@angular/http";
import { ObjectUtils, StringUtils, ArrayUtils } from "./utils";
import { AsyncCallback } from "./async-callback";
import { AppError } from "./model";
import { ApplicationInfoModel } from "./model/application-info.model";
import { AppConstantsService } from "./service";

@Injectable({providedIn: "root"})
export class ApplicationInfoManager {
	
	private appConstantsService: AppConstantsService;

	private _appInfo: ApplicationInfoModel;

	constructor(appConstantsService: AppConstantsService) {
		this.appConstantsService = appConstantsService;
	}

	public initialize(): Promise<any> {
		return new Promise((resolve, reject) => {			
			this.appConstantsService.getApplicationInfo({
				onSuccess: (infoModel: ApplicationInfoModel) => {
					this._appInfo = infoModel;
					resolve();
				},
				onFailure: () => {					
					reject();
				}
			});
		});
	}

	public isProduction(): boolean {
		return this._appInfo.production;
	}

	public getEnvironmentName(): string {
		return this._appInfo.environmentName;
	}
}