import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model/app-error";
import { ApiPathConstants } from "../constants/api-path.constants";
import { CursValutarModel } from "../model";

@Injectable()
export class CursValutarService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getCursValutarCurent(callback: AsyncCallback<CursValutarModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_CURS_VALUTAR_CURENT, null, CursValutarModel, callback);
	}
}