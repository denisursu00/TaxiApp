import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { DispatcherPermissionEnum, DriverPermissionEnum, LoggedInUserModel, RouteConstants } from "@app/shared";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-page-not-found",
	templateUrl: "./page-not-found.component.html",
	styleUrls: ["./page-not-found.component.css"]
})
export class PageNotFoundComponent implements OnInit {

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
		} else if (loggedInUser.permissions.includes(DriverPermissionEnum.PERSONAL_PAGE)) {
			this.router.navigate([RouteConstants.DRIVER_PERSONAL_PAGE]).then(() => {});
		} else {
			this.router.navigate([RouteConstants.HOME]).then(() => {});
		}
	}
}
