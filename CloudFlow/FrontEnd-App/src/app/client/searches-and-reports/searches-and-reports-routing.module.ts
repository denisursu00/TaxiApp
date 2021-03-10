import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SearchesAndReportsComponent } from "./searches-and-reports.component";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "",
		component: SearchesAndReportsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.SEARCHES_AND_REPORTS.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class SearchesAndReportsRoutingModule { }
