import { Injectable } from "@angular/core";
import { StringUtils } from "./../utils";

@Injectable({providedIn: "root"})
export class AuthDataStorage {

	private static readonly STORAGE_TOKEN_ITEM_NAME: string = "_cf_auth_token_";

	public saveToken(authToken: string, persistent : boolean) {
		if (persistent) {
			localStorage.setItem(AuthDataStorage.STORAGE_TOKEN_ITEM_NAME, authToken);
		} else {
			sessionStorage.setItem(AuthDataStorage.STORAGE_TOKEN_ITEM_NAME, authToken);
		}
	}

	public getToken(): string {
		let dataAsString: string = localStorage.getItem(AuthDataStorage.STORAGE_TOKEN_ITEM_NAME);
		if (StringUtils.isBlank(dataAsString)) {
			dataAsString = sessionStorage.getItem(AuthDataStorage.STORAGE_TOKEN_ITEM_NAME);
		}
		if (StringUtils.isBlank(dataAsString)) {
			return null;
		}
		return dataAsString;
	}

	public deleteToken(): void {
		localStorage.removeItem(AuthDataStorage.STORAGE_TOKEN_ITEM_NAME);
		sessionStorage.removeItem(AuthDataStorage.STORAGE_TOKEN_ITEM_NAME);
	}
}