import { Component, OnDestroy } from "@angular/core";
import { AppComponent } from "./app.component";
import { BreadcrumbService } from "./breadcrumb.service";
import { Subscription } from "rxjs";
import { MenuItem } from "primeng/primeng";
import { RouteConstants } from "@app/shared";

@Component({
	selector: "app-breadcrumb",
	templateUrl: "./app.breadcrumb.component.html"
})
export class AppBreadcrumbComponent implements OnDestroy {

	subscription: Subscription;

	items: MenuItem[];

	homeRouterLink: string = RouteConstants.HOME;
	
	constructor(public breadcrumbService: BreadcrumbService) {
		this.subscription = breadcrumbService.itemsHandler.subscribe(response => {
			this.items = response;
		});
	}

	ngOnDestroy() {
		if (this.subscription) {
			this.subscription.unsubscribe();
		}
	}
}
