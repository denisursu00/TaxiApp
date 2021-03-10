import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { PrezentaOnlineComponent } from "./prezenta-online.component";
import { AuthRouteGuard, AUTH_ACCESS } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "",
		component: PrezentaOnlineComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.PREZENTA.COMPLETARE.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class PrezentaOnlineRoutingModule { }
