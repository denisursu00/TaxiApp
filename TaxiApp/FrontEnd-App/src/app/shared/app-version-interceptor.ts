import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { Router } from "@angular/router";
import { RouteConstants, StringUtils } from "@app/shared";
import { AppUpdateMediator } from "@app/shared";
import { HttpResponse } from "@angular/common/http";

@Injectable()
export class AppVersionInterceptor implements HttpInterceptor {

	private appUpdateMediator: AppUpdateMediator;

	constructor(router: Router, appUpdateMediator: AppUpdateMediator) {
		this.appUpdateMediator = appUpdateMediator;
	}

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(request).pipe(
			tap(
				(event: HttpEvent<any>) => {
					if (event instanceof HttpResponse) {
						let response: HttpResponse<any> = <HttpResponse<any>> event;
						let backendVersion: string = response.headers.get("X-BackEnd-App-Version");
						if (StringUtils.isNotBlank(backendVersion)) {
							this.appUpdateMediator.setBackendVersion(backendVersion);
						}
					}
				},
				(err: any) => {}
			)
		);
	}
}