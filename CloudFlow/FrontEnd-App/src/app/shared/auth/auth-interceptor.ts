import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { Router } from "@angular/router";
import { RouteConstants, StringUtils } from "@app/shared";
import { AuthManager } from "@app/shared/auth";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

	private router: Router;
	private authManager: AuthManager;

	constructor(router: Router, authManager: AuthManager) {
		this.router = router;
		this.authManager = authManager;
	}

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

		let authToken: string = this.authManager.getAuthToken();
		if (StringUtils.isNotBlank(authToken)) {			
			request = request.clone({
				setHeaders: {
					Authorization: "Bearer " + authToken
				}
			});
		}
		
		return next.handle(request).pipe(
			tap(
				(event: HttpEvent<any>) => {},
				(err: any) => {
					if (err instanceof HttpErrorResponse) {
						if (err.status === 401) {
							this.doWhen401();
						}
					}
				}
			)
		);
	}

	private doWhen401() {
		this.router.navigate([RouteConstants.AUTH_LOGIN]);
	}
}