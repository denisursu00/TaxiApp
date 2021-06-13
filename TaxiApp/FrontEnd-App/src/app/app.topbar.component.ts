import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AppComponent} from "./app.component";
import { TranslateUtils, AclService, StringUtils, MessageDisplayer, LoggedInUserModel, DispatcherPermissionEnum, DriverPermissionEnum } from "@app/shared";
import { AuthManager, AUTH_ACCESS } from "@app/shared/auth";
import { RouteConstants } from "./shared/constants/route.constants";

@Component({
	selector: "app-topbar",
	templateUrl: "./app.topbar.component.html"
})
export class AppTopBarComponent implements OnInit {
	
	private translateUtils: TranslateUtils;
	private authManager: AuthManager;
	public app: AppComponent;
	private router: Router;
	private messageDisplayer: MessageDisplayer;
	public homeRouterLink: string = RouteConstants.HOME;

	constructor(authManager: AuthManager, app: AppComponent, translateUtils: TranslateUtils, 
			router: Router, messageDisplayer: MessageDisplayer) {
		this.authManager = authManager;
		this.app = app;
		this.translateUtils = translateUtils;
		this.router = router;		
		this.messageDisplayer = messageDisplayer;
		let loggedInUser: LoggedInUserModel = this.authManager.getLoggedInUser();
		if (loggedInUser.permissions.includes(DispatcherPermissionEnum.MANAGE_RIDES)) {
			this.homeRouterLink = RouteConstants.DISPATCHER_RIDES;
		} else if (loggedInUser.permissions.includes(DriverPermissionEnum.PERSONAL_PAGE)) {
			this.homeRouterLink = RouteConstants.DRIVER_PERSONAL_PAGE;
		}
	}

	ngOnInit(): void  {
	}

	public get loggedInUserInfo(): string {
		let loggedInUserInfo: string = "";
		if (this.authManager.isAuthenticated()) {
			loggedInUserInfo = this.authManager.getLoggedInUser().firstName + " " + this.authManager.getLoggedInUser().lastName;
		}
		return loggedInUserInfo;
	}

	public onChangeLanguage(event: any, lang: string) {
		event.preventDefault();
		this.translateUtils.changeLanguage(lang);
	}

	public onLogout(event: any): void {
		event.preventDefault();
		this.authManager.logout();
		this.router.navigate([RouteConstants.AUTH_LOGIN]);
	}

}
