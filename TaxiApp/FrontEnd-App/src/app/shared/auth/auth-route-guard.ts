import { Injectable } from "@angular/core";
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { AuthManager } from "./auth-manager";
import { CanActivateChild } from "@angular/router";
import { RouteConstants } from "./../constants";
import { ArrayUtils, ObjectUtils } from "./../utils";

@Injectable({ providedIn: "root"})
export class AuthRouteGuard implements CanActivate {

	private router: Router;
	private authManager: AuthManager;

	constructor(router: Router, authManager: AuthManager) {
		this.router = router;
		this.authManager = authManager;
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
		
		for (let i = 0; i < 9999; i++) {
			clearInterval(i);
		}

		let authenticated: boolean = this.authManager.isAuthenticated();

		if (!authenticated) {
			this.router.navigate([RouteConstants.AUTH_LOGIN]);
			return false;
		}

		let routePermissions: string[] = [];
		if (ObjectUtils.isNotNullOrUndefined(route.data)) {
			routePermissions = route.data.authPermissions;
		}
		if (ArrayUtils.isNotEmpty(routePermissions)) {
			let hasAuthority: boolean = this.authManager.hasAnyPermission(routePermissions);
			if (!hasAuthority) {
				this.router.navigate([RouteConstants.ERROR_ACCESS_DENIED]);
				return false;
			}
		}
		
		return true;
	}
}