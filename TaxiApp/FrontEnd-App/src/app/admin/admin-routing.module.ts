import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { ParametersComponent } from "./parameters/parameters.component";

const routes: Routes = [
	{
		path: "parameters",
		component: ParametersComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.PARAMETERS.permissions
		}
	},
	{
		path: "",
		redirectTo: "parameters",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AdminRoutingModule {
}
