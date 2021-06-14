import { Injectable } from "@angular/core";
import { ApiCaller } from "./../api-caller";
import { AppError, RegisterRequestModel } from "./../model";
import { ApiPathConstants } from "./../constants";
import { AsyncCallback } from "./../async-callback";
import { LoginRequestModel } from "../model/auth/login-request.model";
import { LoginResponseModel } from "../model/auth/login-response.model";
import { LoggedInUserModel } from "../model/auth/logged-in-user.model";
import { ApiPathUtils } from "../utils";

@Injectable()
export class AuthService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public login(loginRequest: LoginRequestModel, callback: AsyncCallback<LoginResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTH_LOGIN, loginRequest, LoginResponseModel, callback);
	}

	public register(registerRequest: RegisterRequestModel, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTH_REGISTER, registerRequest, null, callback);
	}

	public getLoggedInUser(callback: AsyncCallback<LoggedInUserModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTH_GET_LOGGED_IN_USER, null, LoggedInUserModel, callback);
	}
	
}