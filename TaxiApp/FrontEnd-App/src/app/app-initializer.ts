import { Injectable } from "@angular/core";
import { AuthManager } from "@app/shared/auth";

@Injectable()
export class AppInitializer {
	
	private authManager: AuthManager;

	constructor(authManager: AuthManager) {
		this.authManager = authManager;
	}

	public initialize(): Promise<void> {
		return new Promise((resolve, reject) => {
			this.authManager.establishAuth()
				.then(() => {
					resolve();
				}).catch(() => {
					reject();
				});
		});
	}
}