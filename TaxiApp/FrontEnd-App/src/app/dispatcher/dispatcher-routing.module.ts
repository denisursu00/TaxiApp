import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { RideComponent } from "./ride/ride.component";

const routes: Routes = [
	{
		path: "rides",
		component: RideComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.DISPATCHER.RIDES.permissions
		}
	},
	{
		path: "",
		redirectTo: "rides",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class DispatcherRoutingModule {
}
