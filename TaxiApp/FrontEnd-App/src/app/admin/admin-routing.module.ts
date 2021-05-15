import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { CarsComponent } from "./cars/cars.component";
import { DispatchersComponent } from "./dispatchers/dispatchers.component";
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
		path: "dispatchers",
		component: DispatchersComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.DISPATCHERS.permissions
		}
	},
	{
		path: "cars",
		component: CarsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.CARS.permissions
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
