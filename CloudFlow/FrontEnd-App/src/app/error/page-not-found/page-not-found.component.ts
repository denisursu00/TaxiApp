import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { RouteConstants } from "@app/shared";

@Component({
	selector: "app-page-not-found",
	templateUrl: "./page-not-found.component.html",
	styleUrls: ["./page-not-found.component.css"]
})
export class PageNotFoundComponent implements OnInit {

	private router: Router;

	constructor(router: Router) {
		this.router = router;
	}

	ngOnInit() {
	}

	onTurnOnToHomePageAction() {
		this.router.navigate([RouteConstants.HOME]).then(() => {});
	}
}
