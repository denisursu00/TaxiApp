import { Injectable } from "@angular/core";
import { StringUtils } from "./utils";
import { environment } from "./../../environments/environment";
import { AppEventMediator } from "./app-event-mediator";
import { AppUpdateEvent, AppUpdatePostponementEvent } from "./events";

@Injectable({providedIn: "root"})
export class AppUpdateMediator {
	
	private static readonly POSTPONE_TIMEOUT_AS_MILLISECONDS: number = 300000; // 5 minutes (5*60*1000)

	private frontendVersion: string = environment.version;
	private backendVersion: string;

	private postponedAppUpdate: boolean = false;

	constructor() {
		AppEventMediator.subscribe(AppUpdatePostponementEvent, {
			handle: (event: AppUpdatePostponementEvent) => {
				this.postponedAppUpdate = true;
				setTimeout((): void => {
					this.postponedAppUpdate = false;
				}, AppUpdateMediator.POSTPONE_TIMEOUT_AS_MILLISECONDS);
			}
		});
	}

	private manageUpdate(): void {
		if (this.postponedAppUpdate) {
			return;
		}
		if (StringUtils.isBlank(this.frontendVersion) || StringUtils.isBlank(this.backendVersion)) {
			return;
		}
		if (this.frontendVersion !== this.backendVersion) {
			if (!this.postponedAppUpdate) {
				AppEventMediator.fire(new AppUpdateEvent(this.backendVersion));
			}
		}
	}

	public setBackendVersion(backendVersion: string): void {
		this.backendVersion = backendVersion;
		this.manageUpdate();
	}
}