import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { PageNotFoundComponent } from "./page-not-found";
import { AccessDeniedComponent } from "./access-denied";

const routes: Routes = [
	{
		path: "page-not-found",
		component: PageNotFoundComponent,
		data: {
			pageLayout: "simple"
		}
	},
	{
		path: "access-denied",
		component: AccessDeniedComponent,
		data: {
			pageLayout: "simple"
		}
	},
	{
		path: "",
		redirectTo: "page-not-found",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ErrorRoutingModule {
}
