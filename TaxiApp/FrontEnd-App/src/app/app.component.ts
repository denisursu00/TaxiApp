import { Component, OnInit } from "@angular/core";
import { Router, NavigationStart, NavigationEnd, ActivatedRoute} from "@angular/router";
import { environment } from "../environments/environment";
import { TranslateUtils, StringUtils, RoleEnum, ObjectUtils, AppEventMediator } from "@app/shared";
import { ActivatedRouteSnapshot } from "@angular/router";
import { AuthManager } from "@app/shared/auth";
import { AppMenuService } from "@app/app.menu.service";
import { BreadcrumbService } from "@app/breadcrumb.service";
import { AppUpdateMediator } from "@app/shared";
import { AppUpdateEvent, AppUpdatePostponementEvent } from "@app/shared/events";

@Component({
	selector: "app-root",
	templateUrl: "./app.component.html",
	styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {
	
	private translateUtils: TranslateUtils;
	private router: Router;

	menuMode = "static";

	topbarMenuActive: boolean;

	overlayMenuActive: boolean;

	staticMenuDesktopInactive: boolean;

	staticMenuMobileActive: boolean;

	layoutMenuScroller: HTMLDivElement;

	lightMenu = true;

	menuClick: boolean;

	topbarItemClick: boolean;

	activeTopbarItem: any;

	resetMenu: boolean;

	menuHoverActive: boolean;

	rightPanelActive: boolean;

	rightPanelClick: boolean;
	
	public simplePageLayout: boolean = false;
	public fullPageLayout: boolean = false;

	private authManager: AuthManager;
	private breadcrumbService: BreadcrumbService;
	private appVersionManager: AppUpdateMediator;

	public appUpdateDialogVisible: boolean = false;
	public appUpdateDialogHeader: string = "";

	constructor(translateUtils: TranslateUtils, router: Router, authManager: AuthManager,
			breadcrumbService: BreadcrumbService, appVersionManager: AppUpdateMediator) {
		this.translateUtils = translateUtils;
		this.router = router;
		this.authManager = authManager;
		this.breadcrumbService = breadcrumbService;
		this.appVersionManager = appVersionManager;
		this.initTranslate();
	}

	public ngOnInit(): void {
		this.router.events.subscribe((event: any) => {
			if(event instanceof NavigationEnd) {
				let navEnd: NavigationEnd = <NavigationEnd> event;
				// WORKAROUND cu acest timeout, intrucat componenta de breadcrumb inca nu-i 
				// initializata cand deja apare event-ul prima data. De vazut cand e timp..
				this.preparePageLayout(this.router.routerState.snapshot.root);
				setTimeout(() => {
					this.breadcrumbService.setItemsByRouteUrl(navEnd.url);
				}, 100);
			}
		});

		AppEventMediator.subscribe(AppUpdateEvent, {
			handle: (event: AppUpdateEvent) => {
				if (this.appUpdateDialogVisible) {
					return;
				}
				this.appUpdateDialogHeader = this.translateUtils.translateLabel("NEW_APPLICATION_VERSION") + " - " + event.newAppVersion;
				this.appUpdateDialogVisible = true;
			}
		});
	}

	private preparePageLayout(activatedRoute: ActivatedRouteSnapshot) {
		let pageLayout: string = this.getPageLayoutFromActivatedRouteLayout(activatedRoute);
		this.simplePageLayout = (ObjectUtils.isNotNullOrUndefined(pageLayout) && pageLayout === "simple");
		this.fullPageLayout = ObjectUtils.isNullOrUndefined(pageLayout) && this.authManager.isAuthenticated();
	}

	private getPageLayoutFromActivatedRouteLayout(activatedRouteSnapshot: ActivatedRouteSnapshot): string {
		let pageLayoutAsString: string = activatedRouteSnapshot.data && activatedRouteSnapshot.data["pageLayout"] ? activatedRouteSnapshot.data["pageLayout"] : null;
		if (activatedRouteSnapshot.firstChild) {
			pageLayoutAsString = this.getPageLayoutFromActivatedRouteLayout(activatedRouteSnapshot.firstChild) || pageLayoutAsString;
		}
		return pageLayoutAsString;
	}

	private initTranslate(): void {
		this.translateUtils.setDefaultLang("ro");
	}

	onLayoutClick() {
		if (!this.topbarItemClick) {
			this.activeTopbarItem = null;
			this.topbarMenuActive = false;
		}

		if (!this.rightPanelClick) {
			this.rightPanelActive = false;
		}

		if (!this.menuClick) {
			if (this.isHorizontal() || this.isSlim()) {
				this.resetMenu = true;
			}

			if (this.overlayMenuActive || this.staticMenuMobileActive) {
				this.hideOverlayMenu();
			}

			this.menuHoverActive = false;
		}

		this.topbarItemClick = false;
		this.menuClick = false;
		this.rightPanelClick = false;
	}

	onMenuButtonClick(event) {
		this.menuClick = true;
		this.topbarMenuActive = false;

		if (this.isOverlay()) {
			this.overlayMenuActive = !this.overlayMenuActive;
		}
		if (this.isDesktop()) {
			this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
		} else {
			this.staticMenuMobileActive = !this.staticMenuMobileActive;
		}

		event.preventDefault();
	}

	onMenuClick($event) {
		this.menuClick = true;
		this.resetMenu = false;
	}

	onTopbarMenuButtonClick(event) {
		this.topbarItemClick = true;
		this.topbarMenuActive = !this.topbarMenuActive;

		this.hideOverlayMenu();

		event.preventDefault();
	}

	onTopbarItemClick(event, item) {
		this.topbarItemClick = true;

		if (this.activeTopbarItem === item) {
			this.activeTopbarItem = null;
		} else {
			this.activeTopbarItem = item;
		}

		event.preventDefault();
	}

	onTopbarSubItemClick(event) {
		event.preventDefault();
	}

	onRightPanelButtonClick(event) {
		this.rightPanelClick = true;
		this.rightPanelActive = !this.rightPanelActive;
		event.preventDefault();
	}

	onRightPanelClick() {
		this.rightPanelClick = true;
	}

	isHorizontal() {
		return this.menuMode === "horizontal";
	}

	isSlim() {
		return this.menuMode === "slim";
	}

	isOverlay() {
		return this.menuMode === "overlay";
	}

	isStatic() {
		return this.menuMode === "static";
	}

	isMobile() {
		return window.innerWidth < 1025;
	}

	isDesktop() {
		return window.innerWidth > 1024;
	}

	isTablet() {
		const width = window.innerWidth;
		return width <= 1024 && width > 640;
	}

	hideOverlayMenu() {
		this.overlayMenuActive = false;
		this.staticMenuMobileActive = false;
	}

	public onAppUpdateConfirmed(): void {
		window.location.reload(true);
	}

	public onAppUpdatePostponed(): void {
		this.appUpdateDialogVisible = false;
		AppEventMediator.fire(new AppUpdatePostponementEvent());
	}
}