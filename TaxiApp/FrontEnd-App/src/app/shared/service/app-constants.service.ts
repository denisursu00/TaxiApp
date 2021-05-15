import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model/app-error";
import { ApiPathConstants } from "../constants/api-path.constants";
import { Injectable } from "@angular/core";

@Injectable()
export class AppConstantsService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

}