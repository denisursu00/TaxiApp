import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { DashboardComponent } from "./dashboard.component";

const routes: Routes = [
	{
		path: "",
		component: DashboardComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.DASHBOARD.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class DashboardRoutingModule { }
