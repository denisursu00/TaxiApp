import { Injectable } from "@angular/core";
import { ApiCaller } from "./../api-caller";
import { AppError } from "./../model";
import { ApiPathConstants } from "./../constants";
import { AsyncCallback } from "./../async-callback";
import { LoginRequestModel } from "../model/auth/login-request.model";
import { LoginResponseModel } from "../model/auth/login-response.model";
import { LoggedInUserModel } from "../model/auth/logged-in-user.model";
import { ApiPathUtils } from "../utils";
import { PasswordChangeModel } from "../model/organization/password-change.model";

@Injectable()
export class AuthService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public login(loginRequest: LoginRequestModel, callback: AsyncCallback<LoginResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTH_LOGIN, loginRequest, LoginResponseModel, callback);
	}

	public getLoggedInUser(callback: AsyncCallback<LoggedInUserModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTH_GET_LOGGED_IN_USER, null, LoggedInUserModel, callback);
	}

	public changePassword(passwordChangeModel: PasswordChangeModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTH_CHANGE_PASSWORD, passwordChangeModel, null, callback);
	}
}