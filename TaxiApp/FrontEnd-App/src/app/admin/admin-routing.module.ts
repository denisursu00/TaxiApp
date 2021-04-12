import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { CarsComponent } from "./cars/cars.component";
import { DriversComponent } from "./drivers/drivers.component";
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
		path: "drivers",
		component: DriversComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.DRIVERS.permissions
		}
	},
	{
		path: "cars",
		component: CarsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.DRIVERS.permissions
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
