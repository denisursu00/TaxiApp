import { Injectable } from "@angular/core";
import { AuthManager } from "@app/shared/auth";
import { ApplicationInfoManager } from "@app/shared/application-info-manager";

@Injectable()
export class AppInitializer {
	
	private authManager: AuthManager;
	private applicationInfoManager: ApplicationInfoManager;

	constructor(authManager: AuthManager, applicationInfoManager: ApplicationInfoManager) {
		this.authManager = authManager;
		this.applicationInfoManager = applicationInfoManager;
	}

	public initialize(): Promise<any> {
		return new Promise((resolve, reject) => {
			this.applicationInfoManager.initialize()
				.then(() => {
					this.authManager.establishAuth()
						.then(() => {
							resolve();
						}).catch(() => {
							resolve();
						});
				}).catch(() => {
					reject();
				});
		});
	}
}