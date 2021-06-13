import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "admin",
		loadChildren: "./admin/admin.module#AdminModule"
	},
	{
		path: "dispatcher",
		loadChildren: "./dispatcher/dispatcher.module#DispatcherModule"
	},
	{
		path: "driver",
		loadChildren: "./driver/driver.module#DriverModule"
	},
	{
		path: "auth",
		loadChildren: "./auth/auth.module#AuthModule"
	},
	{
		path: "error",
		loadChildren: "./error/error.module#ErrorModule"
	},
	{
		path: "",
		redirectTo: "admin",
		pathMatch: "full"
	},
	{
		path: "**",
		redirectTo: "/error/page-not-found",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forRoot(routes, { useHash: true })],
	exports: [RouterModule]
})
export class AppRoutingModule { }
