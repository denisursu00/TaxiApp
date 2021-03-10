import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { NomenclatorsDataComponent } from "./nomenclators-data";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "data",
		component: NomenclatorsDataComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.NOMENCLATORS.permissions
		}
	},
	{
		path: "",
		redirectTo: "data",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class NomenclatorsRoutingModule { }
