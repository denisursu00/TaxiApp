import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { DispatcherPermissionEnum, LoggedInUserModel, RouteConstants } from "@app/shared";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-access-denied",
	templateUrl: "./access-denied.component.html",
	styleUrls: ["./access-denied.component.css"]
})
export class AccessDeniedComponent implements OnInit {

	private router: Router;
	private authManager: AuthManager;

	constructor(router: Router, authManager: AuthManager) {
		this.router = router;
		this.authManager = authManager;
	}

	ngOnInit() {
	}

	onTurnOnToHomePageAction() {
		let loggedInUser: LoggedInUserModel = this.authManager.getLoggedInUser();
		if (loggedInUser.permissions.includes(DispatcherPermissionEnum.MANAGE_RIDES)) {
			this.router.navigate([RouteConstants.DISPATCHER_RIDES]).then(() => {});
		} else {
			this.router.navigate([RouteConstants.HOME]).then(() => {});
		}
		
	}

	onLogoutAction() {
		this.authManager.logout();
		this.router.navigate([RouteConstants.AUTH_LOGIN]).then(() => {});
	}

	public get logoutEnabled() {
		return this.authManager.isAuthenticated();
	}
}
