import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AppComponent} from "./app.component";
import { TranslateUtils, AclService, StringUtils, MessageDisplayer } from "@app/shared";
import { environment } from "../environments/environment";
import { AnimationStyleMetadata } from "@angular/animations";
import { AuthManager, AUTH_ACCESS } from "@app/shared/auth";
import { RouteConstants } from "./shared/constants/route.constants";
import { ApplicationInfoManager } from "@app/shared/application-info-manager";

@Component({
	selector: "app-topbar",
	templateUrl: "./app.topbar.component.html"
})
export class AppTopBarComponent implements OnInit {
	
	private translateUtils: TranslateUtils;
	private authManager: AuthManager;
	private applicationInfoManager: ApplicationInfoManager;
	public app: AppComponent;
	private router: Router;
	private messageDisplayer: MessageDisplayer;
	public homeRouterLink: string = RouteConstants.HOME;

	public isUserPrezenta: boolean = false;

	constructor(authManager: AuthManager, app: AppComponent, translateUtils: TranslateUtils, 
			router: Router, messageDisplayer: MessageDisplayer, applicationInfoManager: ApplicationInfoManager) {
		this.authManager = authManager;
		this.app = app;
		this.translateUtils = translateUtils;
		this.router = router;		
		this.messageDisplayer = messageDisplayer;
		this.applicationInfoManager = applicationInfoManager;
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

	
	public onChangePassword(event: any): void {
		event.preventDefault();
		this.router.navigate([RouteConstants.AUTH_CHANGE_PASSWORD]);
	}

	public get appVersion(): string {
		return environment.version;
	}

	public get isProduction(): boolean {
		return this.applicationInfoManager.isProduction();
	}

	public get appEnvironmentName(): string {
		return this.applicationInfoManager.getEnvironmentName();
	}
}
