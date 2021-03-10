import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "client",
		loadChildren: "./client/client.module#ClientModule"
	},
	{
		path: "admin",
		loadChildren: "./admin/admin.module#AdminModule"
	},
	{
		path: "prezenta-online",
		loadChildren: "./prezenta-online/prezenta-online.module#PrezentaOnlineModule"
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
		redirectTo: "client",
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
