import { Injectable } from "@angular/core";
import { Http, Response, Headers} from "@angular/http";
import { ObjectUtils, StringUtils, ArrayUtils } from "./../utils";
import { AuthService } from "./../service/auth.service";
import { AsyncCallback } from "./../async-callback";
import { AppError } from "./../model";
import { AuthDataStorage } from "./auth-data-storage";
import { LoggedInUserModel, LoginRequestModel, LoginResponseModel } from "../model/auth";

@Injectable({providedIn: "root"})
export class AuthManager {
	
	private authDataStorage: AuthDataStorage;
	private authService: AuthService;

	private _authToken: string;
	private _loggedInUser: LoggedInUserModel;

	constructor(authDataStorage: AuthDataStorage, authService: AuthService) {
		this.authDataStorage = authDataStorage;
		this.authService = authService;
		this._authToken = this.authDataStorage.getToken();
	}

	
	public login(loginRequest: LoginRequestModel, callback: AsyncCallback<void, AppError>): void {
		this.clearAuthData();
		this.authService.login(loginRequest, {
			onSuccess: (loginResponse: LoginResponseModel): void => {				
				this._authToken = loginResponse.authToken;
				this._loggedInUser = loginResponse.loggedInUser;
				this.authDataStorage.saveToken(this._authToken, loginRequest.rememberMe);
				callback.onSuccess(null);
			},
			onFailure: (error: AppError): void => {
				callback.onFailure(error);
			}
		});
	}

	private clearAuthData() {
		this.authDataStorage.deleteToken();
		this._authToken = null;
		this._loggedInUser = null;
	}

	public logout(): void {
		this.clearAuthData();
	}

	public isAuthenticated(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this._authToken) && ObjectUtils.isNotNullOrUndefined(this._loggedInUser);
	}

	public establishAuth(): Promise<any> {
		return new Promise((resolve, reject) => {			
			if (StringUtils.isBlank(this._authToken)) {
				this._loggedInUser = null;
				resolve();
			} else {
				this.authService.getLoggedInUser({
					onSuccess: (theLoggedInUser: LoggedInUserModel) => {
						this._loggedInUser = theLoggedInUser;
						resolve();
					},
					onFailure: () => {					
						resolve();
					}
				});
			}
		});
	}

	public hasPermission(permission: string): boolean {
		if (!this.isAuthenticated) {
			return false;
		}
		if (ArrayUtils.isEmpty(this._loggedInUser.permissions)) {
			return false;
		}
		return this._loggedInUser.permissions.includes(permission);
	}

	public hasOnlyOnePermission (permission: string): boolean {
		if (!this.isAuthenticated) {
			return false;
		}
		if (ArrayUtils.isEmpty(this._loggedInUser.permissions)) {
			return false;
		}
		if (this._loggedInUser.permissions.length > 1) {
			return false;
		}
		return this._loggedInUser.permissions.includes(permission);
	}

	public hasAnyPermission(permissions: string[]): boolean {
		if (!this.isAuthenticated) {
			return false;
		}
		if (ArrayUtils.isEmpty(this._loggedInUser.permissions)) {
			return false;
		}
		let has: boolean = false;
		permissions.forEach((permission: string) => {
			if (this._loggedInUser.permissions.includes(permission)) {
				has = true;
			}
		});
		return has;
	}

	public getLoggedInUser(): LoggedInUserModel {
		return this._loggedInUser;
	}

	public getAuthToken(): string {
		return this.authDataStorage.getToken();
	}
}